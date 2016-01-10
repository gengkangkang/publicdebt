package com.estate;

import android.app.Application;
import cache.DataCache;

import com.android.core.utils.SharePreferenceStorageService;

public class GlobalPhone extends Application {

	/**
	 * universal 缓存
	 */
	// public static DiscCacheAware cache;
	// public static DisplayImageOptions options;

	private SharePreferenceStorageService sharePreference;
	
	private String banks;

	public final static int DEFAULT_DISKCACHE_SIZE = 10 * 1024 * 1024;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sharePreference = SharePreferenceStorageService.newInstance(this
				.getApplicationContext());

		
		/**
		 * universal 缓存
		 */
		DataCache.newInstance(getApplicationContext());
		// setupUIL();

	}

	public SharePreferenceStorageService getSharePreference() {
		return sharePreference;
	}

	public void setSharePreference(SharePreferenceStorageService sharePreference) {
		this.sharePreference = sharePreference;
	}

	public String getBanks() {
		return banks;
	}

	public void setBanks(String banks) {
		this.banks = banks;
	}

	/**
	 * universal 缓存
	 */
	// /**
	// * Universal Image Loader Setup
	// */
	// private void setupUIL() {
	// File cacheDir = StorageUtils.getOwnCacheDirectory(
	// getApplicationContext(), "CafeCar/cache");
	// options = new DisplayImageOptions.Builder()
	// .cacheOnDisc(true).cacheInMemory(true).build();
	// cache = new TotalSizeLimitedDiscCache(cacheDir, 50 * 1024 * 1024);
	// ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	// getApplicationContext()).threadPoolSize(3)
	// .threadPriority(Thread.NORM_PRIORITY - 1)
	// .denyCacheImageMultipleSizesInMemory()
	// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
	// // You can pass your own memory cache implementation
	// .discCache(cache)
	// // You can pass your own disc cache implementation
	// .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
	// .defaultDisplayImageOptions(options).build();
	// ImageLoader.getInstance().init(config);
	// }

	private boolean isDownload;

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	
	
}
