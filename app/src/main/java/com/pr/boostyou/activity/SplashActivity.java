package com.pr.boostyou.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pr.boostyou.R;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.InstaProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

	private Button startBtnView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findViews();


		startBtnView.setOnClickListener(startBtnClickListener);


	}

	private void findViews() {
		startBtnView = findViewById(R.id.startBtnView);
	}







	View.OnClickListener  startBtnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			loadJsonForInstagram();
		}
	};

	private void loadJsonForInstagram() {
// https://www.instagram.com/aramsadoy/?__a=1
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService().getProfilePictureUrl("https://www.instagram.com/aramsadoy/?__a=1").enqueue(new Callback<InstaProfile>() {
			@Override
			public void onResponse(Call<InstaProfile> call, Response<InstaProfile> response) {

				Log.d("dwd","succes");
			}

			@Override
			public void onFailure(Call<InstaProfile> call, Throwable t) {
				Log.d("dwd","fail");

			}
		});

	}
}
