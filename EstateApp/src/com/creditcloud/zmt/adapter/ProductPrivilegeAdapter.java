package com.creditcloud.zmt.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.core.utils.Text.ObjectUtils;
import com.android.core.view.RoundProgressBar;
import com.creditcloud.api.ApiConstants;
import com.creditcloud.model.enums.LoanStatus;
import com.creditcloud.utils.HttpDownloader;
import com.creditcloud.zmt.utils.LoadingProgressDialog;
import com.creditcloud.zmt.utils.StatusString;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.activity.ProductDescripActivity;
import com.estate.entity.Privilege;
import com.estate.utils.pdf.MuPDFActivity;

import java.io.File;
import java.util.List;

public class ProductPrivilegeAdapter extends BaseAdapter {

	private Context context;
	private List<Privilege> dates;
	private DownloadTask task;

	public ProductPrivilegeAdapter(Context context) {
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
			view = LayoutInflater.from(context).inflate(R.layout.listview_touzi, null);
			holder = new ViewHolder();
			holder.view_bg    = (View) view.findViewById(R.id.view_bg);
			holder.title      = (TextView) view.findViewById(R.id.tv_touzi_title);
			holder.limit      = (TextView) view.findViewById(R.id.tv_touzi_limit);
			holder.percent = (TextView) view.findViewById(R.id.tv_touzi_percent);
			holder.percentBar = (RoundProgressBar) view.findViewById(R.id.rpb_touzi);
			holder.btn_invest = (TextView) view.findViewById(R.id.btn_touzi);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		final Privilege detail = (Privilege) ObjectUtils.isEmpty(dates.get(position), Privilege.class);

		holder.view_bg.setTag(detail.getID());
		holder.view_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ProductDescripActivity.startAc(context, (String) arg0.getTag());
			}
		});
		
		holder.title.setText(detail.getTITLE());
		holder.percent.setText(String.format(context.getString(R.string.font_percentfloat), Integer.parseInt(detail.getRATE()) / 100.00));
		holder.limit.setText(String.format(context.getString(R.string.money_limit), detail.getDAYS()));
		
		if(detail.getSTATUS().equals(LoanStatus.SETTLED.name().toString()) || detail.getSTATUS().equals(LoanStatus.CLEARED.name().toString())){
			holder.percentBar.setVisibility(View.INVISIBLE);
		}else{
		    holder.percentBar.setProgress((int) (Double.parseDouble(detail.getBID_AMOUNT())/detail.getAMOUNT()*100));  // 进度=投标金额/总计额
		}
		if (detail.getSTATUS().equals("OPENED")) {
			holder.btn_invest.setText(context.getString(R.string.tab_touzi_touzi));
			holder.btn_invest.setEnabled(true);
			holder.btn_invest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

				}
			});
		} else {
			if (detail.getSTATUS().equals("SCHEDULED")) {
				holder.btn_invest.setText(context.getString(R.string.nostart));
			} else {
				holder.btn_invest.setText(StatusString.getTouziStatus(detail.getSTATUS()));
			}
			holder.btn_invest.setEnabled(false);
		}
		return view;
	}

	class ViewHolder {
		private View view_bg;
		private TextView title;
		private TextView limit;		
		private TextView percent;
		private RoundProgressBar percentBar;
		private TextView btn_invest;
	}

	public List<Privilege> getDates() {
		return dates;
	}

	public void setDates(List<Privilege> dates) {
		this.dates = dates;
	}

	class DownloadTask extends AsyncTask<String, Void, File> {

		private LoadingProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new LoadingProgressDialog(context, R.style.transparentFrameWindowStyle, "文件正在加载中，请稍等");
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
				file = downloader.downloadReturnFile(ApiConstants.URL_BASE
						+ "/user/" + params[0] + "/invest/" + params[1]
						+ "/contract", "zmit/", params[2]);
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
