package com.pr.boostyou.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

	private static final String TAG = "RestClient";
	private static RestClient thisInstance = null;

	public static final int CACHE_SIZE = 10 * 1024 * 1024;


	private InstaApiService watchApiService;

	public static final String TEST_BASE_URL = "https://api.instagram.com/";

	public RestClient(Context context) {
//		if (enableCaches) {
//			createCacheAbleInstance(context);
//		} else {
		createInstance2(context);
		//}
	}



	private void createInstance2(Context context) {

		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
		httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		//String baseUrl = AppConstants.DEFAULT_BASE_URL;

		//File cacheDir = new File(context.getCacheDir().getAbsolutePath() + "/cacchefr");
		//Cache cache = new Cache(cacheDir, CACHE_SIZE);

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				//.cache(cache)
				.addInterceptor(new LoggingInterceptor())
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(TEST_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.client(okHttpClient)
				.build();

		watchApiService = retrofit.create(InstaApiService.class);

	}


	class LoggingInterceptor implements Interceptor {
		@Override
		public Response intercept(Interceptor.Chain chain) throws IOException {
			Request request = chain.request();

			Log.d(TAG, String.format("Sending request %s on %s%n%s",
					request.url(), chain.connection(), request.headers()));
			long t1 = System.nanoTime();

			Response response = chain.proceed(request);

			long t2 = System.nanoTime();
			Log.d(TAG, String.format("Received response for %s with code %d in %.1fms%n%s",
					response.request().url(), response.code(), (t2 - t1) / 1e6d, response.headers()));

			return response;
		}
	}


	public static RestClient getInstance(Context context) {
		if (thisInstance == null) {
			thisInstance = new RestClient(context);
		}
		return thisInstance;
	}

	public InstaApiService getInstaApiService() {
		return watchApiService;
	}


	public boolean hasNetwork(Context context) {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			isConnected = true;
		}
		return isConnected;
	}
}
