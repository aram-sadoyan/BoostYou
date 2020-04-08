package com.pr.boostyou.util;

import android.util.Log;

import com.pr.boostyou.MainApplication;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.Response2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.boostyou.util.AppConstants.TK;

public class AnalyticUtils {
	//private static AnalyticUtils INSTANCE = this;

//	public static AnalyticUtils() {
//		if (INSTANCE == null){
//			new INsta
//		}
//	}

	public static void sendLoginEvent(String userId){
		RestClient.getInstance(MainApplication.getInstance().getApplicationContext())
				.getInstaApiService().addLoginCount(userId, TK).enqueue(new Callback<Response2>() {
			@Override
			public void onResponse(Call<Response2> call, Response<Response2> response) {
				Log.d("analyticsevent","new login count added sendLoginEvent");
			}

			@Override
			public void onFailure(Call<Response2> call, Throwable t) {
				Log.d("analyticsevent","sendLoginEvent failed");
			}
		});
	}



}
