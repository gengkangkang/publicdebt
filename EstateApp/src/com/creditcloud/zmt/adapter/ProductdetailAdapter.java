package com.creditcloud.zmt.adapter;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.core.utils.Text.ObjectUtils;
import com.creditcloud.api.ApiConstants;
import com.creditcloud.utils.HttpDownloader;
import com.creditcloud.zmt.utils.LoadingProgressDialog;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.activity.ProductDescripActivity;
import com.estate.activity.ProductDetailActivity;
import com.estate.entity.ProductDetail;
import com.estate.utils.pdf.MuPDFActivity;

import java.io.File;
import java.util.List;

import cache.DataCache;

public class ProductdetailAdapter extends BaseAdapter {

    private ProductDetailActivity context;
    private List<ProductDetail> dates;
    private DownloadTask task;

    public ProductdetailAdapter(ProductDetailActivity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (dates == null) {
            return 0;
        } else {
            return dates.size();
        }
    }

    @Override
    public Object getItem(int arg0) {
        return dates.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_product, null);
            holder = new ViewHolder();
            holder.view_bg = (View) view.findViewById(R.id.view_bg);
            holder.title = (TextView) view.findViewById(R.id.tv_product_title);
            holder.rate = (TextView) view.findViewById(R.id.tv_product_rate);
            holder.price = (TextView) view.findViewById(R.id.tv_product_count);
            holder.limit = (TextView) view.findViewById(R.id.tv_product_limit);
            holder.checkAgree = (TextView) view
                    .findViewById(R.id.tv_checkmyagree);
            holder.layout = (LinearLayout) view
                    .findViewById(R.id.layout_product);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ProductDetail detail = (ProductDetail)
                ObjectUtils.isEmpty(dates.get(position), ProductDetail.class);

        holder.layout.setTag(detail.getLoanId());
        holder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	
            	 ProductDescripActivity.startAc(context,
                         (String) holder.layout.getTag());
               
            	 
            	 /*if (!detail.getLoanTitle().contains("银行理财")) {
                   
                } 
                else {
                    context.showToast("当前版本不支持银行理财产品查看");
                }*/
            }
        });

        holder.title.setText(detail.getLoanTitle());

        String rates = String.format(context.getString(R.string.money_rate), detail.getRate() / 100.0) + "%";
        int length = String.valueOf(detail.getRate() / 100.0).length();
        SpannableString ss = new SpannableString(rates);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 5, length + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.rate.setText(ss);
        holder.price.setText(String.format(context.getString(R.string.money_count), String.format("%1$.2f", detail.getAmount())));
        holder.limit.setText(String.format(context.getString(R.string.money_limit), detail.getDuration().getTotalDays()));

        if ("SETTLED".equals(detail.getStatus())) {//已经结息的产品才显示“查看相关协议按钮”
            holder.checkAgree.setVisibility(View.VISIBLE);
            holder.checkAgree.setTag(detail);
            if (detail.getLoanTitle().contains("银行理财")) {
                holder.checkAgree.setText("*如需查看详情或购买请登录www.cmiinv.com");
            }
            holder.checkAgree.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // ""http://120.132.68.177/api/v2/user/4D38EF70-E902-4CC7-B6D2-A46F081E197C/invest/90E2D8AA-2A89-47B3-9F15-C0DFB0D51231/contract"
                    if (!detail.getLoanTitle().contains("银行理财")) {
                        String name = BaseActivity.preferenceStorageService.getUserId()
                                + ((ProductDetail) holder.checkAgree.getTag()).getLoanTitle()
                                + "协议.pdf";
                        String userId = ((ProductDetail) holder.checkAgree.getTag()).getUserId();
                        String id = ((ProductDetail) holder.checkAgree.getTag()).getId();
//					LogUtils.paintE("userId = " + userId, this);
//					LogUtils.paintE("id = " + id, this);
//					LogUtils.paintE("name = " + name, this);
                        task = new DownloadTask();
                        task.execute(userId, id, name);
                    }
                }
            });

        } else {
            holder.checkAgree.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class ViewHolder {

        private TextView title;

        private TextView price;

        private TextView rate;

        private TextView limit;

        private TextView checkAgree;

        private LinearLayout layout;

        private View view_bg;
    }

    public List<ProductDetail> getDates() {
        return dates;
    }

    public void setDates(List<ProductDetail> dates) {
        this.dates = dates;
    }

    class DownloadTask extends AsyncTask<String, Void, File> {

        private LoadingProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new LoadingProgressDialog(context,
                    R.style.transparentFrameWindowStyle, "文件正在加载中，请稍等");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    if (task != null && !task.isCancelled()) {
                        task.cancel(true);
                    }
                }
            });
            dialog.show();
        }

        @Override
        protected File doInBackground(String... params) {
            File file;
            try {
                HttpDownloader downloader = new HttpDownloader();
                String fileUrl = ApiConstants.URL_BASE
                        + "/user/" + params[0] + "/invest/" + params[1]
                        + "/contract?access_token=" + DataCache.getDataCache().queryCache("access_token");
//				LogUtils.paintE("fileUrl = " + fileUrl, this);
                file = downloader.downloadReturnFile(fileUrl, "zmit/", params[2]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return file;
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                MuPDFActivity.startAc(context, MuPDFActivity.PRODUCT,
                        result.getAbsolutePath());
            } else {
                ((BaseActivity) context).showToast("文件加载失败");
            }
        }

    }

}
