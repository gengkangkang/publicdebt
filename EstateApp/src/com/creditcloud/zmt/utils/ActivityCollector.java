package com.creditcloud.zmt.utils;

import java.util.ArrayList;
import java.util.List;

import com.creditcloud.zmt.activity.MainActivity;

import android.app.Activity;

/**
 * 
 * @author mengxc
 *
 */
public class ActivityCollector {

	public static List<Activity> activities = new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){
		activities.add(activity);
	}
	
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	public static void finishAll(){
		for(Activity activity : activities){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
	
	public static void finishAllExcept(){
		for(Activity activity : activities){
			if(!(activity instanceof MainActivity)){
			if(!activity.isFinishing()){
				activity.finish();
			}
			}
		}
	}
}
