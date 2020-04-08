package com.pr.boostyou.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.pr.boostyou.R;

public class PreSplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_splash);
		MobileAds.initialize(this, getString(R.string.admob_app_id));

		Intent intent = new Intent(PreSplashActivity.this, SplashActivity.class);
		startActivity(intent);
		finish();
	}
}
