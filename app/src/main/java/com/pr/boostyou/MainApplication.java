package com.pr.boostyou;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.MobileAds;

public class MainApplication extends Application {

	public static MainApplication instance;


	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		//todo handle no network case


		MobileAds.initialize(this, initializationStatus ->
				Log.d("dwd","initializationStatus= " + initializationStatus.getAdapterStatusMap()));


		Fresco.initialize(this);


	}









	@Override
	public Context getApplicationContext() {
		return super.getApplicationContext();
	}

	public static MainApplication getInstance() {
		return instance;
	}
}
