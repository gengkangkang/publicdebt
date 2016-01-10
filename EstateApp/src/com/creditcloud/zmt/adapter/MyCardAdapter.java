package com.creditcloud.zmt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditcloud.model.BankAccount;
import com.creditcloud.model.FundAccount;
import com.creditcloud.zmt.utils.StatusString;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.activity.ProductDescripActivity;

public class MyCardAdapter extends BaseAdapter{
	
	private Context context;
	
	private List<FundAccount> card_list;
	
	private Map<String,Object> bankMap;
	
	public MyCardAdapter(Context context,List<FundAccount> card_list){
		this.context = context;
		this.card_list = card_list;
		bankMap = BaseActivity.preferenceStorageService.getBankName();
	}

	@Override
	public int getCount() {
		if(card_list != null){
			return card_list.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		return card_list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if(convertView == null){
			view = LayoutInflater.from(context).inflate(R.layout.listview_mycard, null);
			holder = new ViewHolder();
	
			holder.title = (TextView) view.findViewById(R.id.tv_mycardname);
			holder.icon = (ImageView) view.findViewById(R.id.iv_mycardicon);
		
			view.setTag(holder);
			
		}else{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		FundAccount account = card_list.get(position);
		BankAccount bankAccount = account.getAccount();
		if(bankMap != null){
			holder.title.setText((String)bankMap.get(bankAccount.getBank())+" "+"尾号"+bankAccount.getAccount().substring(bankAccount.getAccount().length()-4));
			
		}else{
			holder.title.setText(StatusString.getBankName(bankAccount.getBank())+" "+"尾号"+bankAccount.getAccount().substring(bankAccount.getAccount().length()-4));
		}
		int resId = StatusString.getBankIconResId(context, bankAccount.getBank());
		if(resId != 0){
		holder.icon.setBackgroundResource(resId);
		}
		return view;
	}
	
	class ViewHolder{
		
		private TextView title;
		
		private ImageView icon;
		
		private View view_bg;
	}

}
