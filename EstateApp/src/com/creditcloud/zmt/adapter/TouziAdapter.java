package com.creditcloud.zmt.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.core.utils.Text.StringUtils;
import com.android.core.view.RoundProgressBar;
import com.creditcloud.model.Loan;
import com.creditcloud.model.enums.LoanStatus;
import com.creditcloud.zmt.utils.StatusString;
import com.estate.R;
import com.estate.activity.ProductDescripActivity;

public class TouziAdapter extends BaseAdapter {

	private List<Loan> date;
	private Context context;
	private View view;
	private ViewHolder holder;

	public TouziAdapter(Context context) {
		this.context = context;
		date = new ArrayList<Loan>();
	}

	@Override
	public int getCount() {
		if (date != null) {
			return date.size();
		} else { return 0; }
	}

	@Override
	public Object getItem(int position) {
		return date.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.listview_touzi, null);
			holder = new ViewHolder();
			holder.view_bg    = (View) view.findViewById(R.id.view_bg);
			holder.title      = (TextView) view.findViewById(R.id.tv_touzi_title);
			holder.percent    = (TextView) view.findViewById(R.id.tv_touzi_percent);
			holder.limit      = (TextView ) view.findViewById(R.id.tv_touzi_limit);
			holder.percentBar = (RoundProgressBar) view.findViewById(R.id.rpb_touzi);
			holder.btn_invest = (TextView) view.findViewById(R.id.btn_touzi);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		// ��ҳ��ؼ���ֵ
		Loan loan = date.get(position);
		holder.view_bg.setTag(loan.getId());
		holder.view_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ProductDescripActivity.startAc(context, (String) arg0.getTag());
			}
		});
		holder.title.setText(loan.getTitle());
		holder.percent.setText(String.format(context.getString(R.string.font_percentfloat), loan.getRate() / 100.00));
		holder.limit.setText(String.format(context
				.getString(R.string.tab_touzi_limit), loan.getDuration()
				.getTotalDays(), StringUtils.addSeparateSign(loan
				.getLoanRequest().getInvestRule().getMinAmount())));
		
		if(loan.getStatus().equals(LoanStatus.SETTLED.name().toString()) || loan.getStatus().equals(LoanStatus.CLEARED.name().toString()) || loan.getStatus().equals(LoanStatus.FINISHED.name().toString())){
			holder.percentBar.setProgress((int) 100);
		}else{
			holder.percentBar.setProgress((int) loan.getInvestPercent());
		}
		if (loan.getStatus().equals("OPENED")) {
			holder.btn_invest.setText(context.getString(R.string.tab_touzi_touzi));
			holder.btn_invest.setEnabled(true);
			holder.btn_invest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {}
			});
		} else {
			if (loan.getStatus().equals("SCHEDULED")) {
				holder.btn_invest.setText(context.getString(R.string.nostart));
			} else {
				holder.btn_invest.setText(StatusString.getTouziStatus(loan.getStatus()));
			}
			holder.btn_invest.setEnabled(false);
		}
		return view;
	}

	public List<Loan> getDate() {
		return date;
	}

	public void setDate(List<Loan> date) {
		this.date = date;
	}

	class ViewHolder {
		private TextView title;
		private TextView percent;
		private TextView limit;
		private RoundProgressBar percentBar;
		private TextView btn_invest;
		private View view_bg;
	}
}
