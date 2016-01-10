package com.creditcloud.zmt.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.creditcloud.zmt.utils.StatusString;
import com.estate.R;
import com.estate.entity.FundsDetail;

public class MoneyAdapter extends BaseAdapter {

	private Context context;
	private List<FundsDetail> dates;
	private DecimalFormat df = new DecimalFormat("0.##");
	private String double_format = "%1$.2f";
	
	public MoneyAdapter(Context context) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    /*
     * 数据格式：时间（yyyy-MM-dd hh:MM:ss) 类型（投资、充值、提现） 金额
     */
	@SuppressLint("SimpleDateFormat") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.listview_moneydetail, null);
			holder = new ViewHolder();

			holder.time  = (TextView) view.findViewById(R.id.tv_moneydetail_time);
			holder.title = (TextView) view.findViewById(R.id.tv_moneydetail_title);
			holder.price = (TextView) view.findViewById(R.id.tv_moneydetail_price);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		// 获取整体数据对象
		FundsDetail funds = dates.get(position);
		// 获取类型
		String type      = funds.getType();
		String status    = funds.getStatus();
		String operation = funds.getOperation();

		// INVEST投資 INVEST_REPAY兌付 DEPOSIT充值 WITHDRAW處理中 
		if (type != null && status != null && operation != null) {
			if (type.equals("INVEST") || type.equals("INVEST_REPAY") || type.equals("DEPOSIT")) {
				// 充值
				if (type.equals("DEPOSIT") && status.equals("SUCCESSFUL") && operation.equals("IN")) {
					// 设置资金显示
					holder.price.setText("+" + String.format(double_format, funds.getAmount()));
					holder.price.setTextColor(Color.rgb(227, 23, 13));
				}
				
				// 投资
				if (type.equals("INVEST") && status.equals("SUCCESSFUL")) {
					// 资金冻结
					if (operation.equals("FREEZE")) {
						holder.price.setText(String.format(double_format, funds.getAmount()));
						holder.price.setTextColor(Color.rgb(0, 0, 0));
					}
					// 资金转出成功
					if (operation.equals("OUT")) {
						holder.price.setText("-" + String.format(double_format, funds.getAmount()));
						holder.price.setTextColor(Color.rgb(34, 139, 34));
					}
					// 资金转出失败
					if (operation.equals("IN")) {
						holder.price.setText("+" + String.format(double_format, funds.getAmount()));
						holder.price.setTextColor(Color.rgb(227, 23, 13));
					}
				}
				
				// 兑付
				if (type.equals("INVEST_REPAY") && status.equals("SUCCESSFUL") && operation.equals("IN")) {
					// 设置资金显示
					holder.price.setText("+" + String.format(double_format, funds.getAmount()));
					holder.price.setTextColor(Color.rgb(227, 23, 13));
				}
				
				// 设置类型显示
				holder.title.setText(StatusString.getMoneyLabel(type));
				// 设置时间显示
		        String format = "HH:mm:ss";
		        SimpleDateFormat dateFormats = new SimpleDateFormat(format);
				holder.time.setText(dateFormats.format(funds.getTimeRecorded()));
			}
		}

		return view;
	}

	class ViewHolder {
		private TextView title;
		private TextView price;
		private TextView time;
	}

	public List<FundsDetail> getDates() {
		return dates;
	}

	public void setDates(List<FundsDetail> dates) {
		this.dates = dates;
	}
}
