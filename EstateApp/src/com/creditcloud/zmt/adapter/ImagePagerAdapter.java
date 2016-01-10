package com.creditcloud.zmt.adapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.creditcloud.api.cache.BitmapCache;
import com.creditcloud.api.cache.DiskCacheParams;
import com.creditcloud.api.cache.DiskLruCache;
import com.creditcloud.model.Banner;
import com.creditcloud.zmt.activity.MainActivity;
import com.estate.R;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

	private MainActivity context;
	private List<Banner> imageIdList;


	private LruCache<String, Bitmap> memoryCache;

	private DiskLruCache diskLruCache;

	public ImagePagerAdapter(MainActivity context, List<Banner> imageIdList) {
		this.context = context;
		this.imageIdList = imageIdList;
		
		try {
			File cacheDir = context.getDiskCacheDir(DiskCacheParams.DIR);  
		    if (!cacheDir.exists()) {  
		        cacheDir.mkdirs();  
		    }  
			diskLruCache = DiskLruCache.open(cacheDir, context.getAppVersion(), 1, DiskCacheParams.DEFAULT_DISKCACHE_SIZE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		memoryCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	@Override
	public int getCount() {
		// Infinite loop
		return imageIdList == null ? 0 : imageIdList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return imageIdList == null ? 0 : position;
	}


	public View getView(int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.viewpager_imageview, null);
			context.volleyHttpClient.loadingImage(holder.imageView,
					imageIdList.get(position).getContent(), 0, 0,
					new BitmapCache(diskLruCache, memoryCache));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		return view;
	}

	private static class ViewHolder {
		ImageView imageView;
	}




	public List<Banner> getImageIdList() {
		return imageIdList;
	}

	public void setImageIdList(List<Banner> imageIdList) {
		this.imageIdList = imageIdList;
	}
	
	
}