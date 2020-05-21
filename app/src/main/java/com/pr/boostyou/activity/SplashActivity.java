package com.pr.boostyou.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pr.boostyou.R;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.InstaProfile;
import com.pr.boostyou.api.model.PromotionModel;
import com.pr.boostyou.api.model.Response2;
import com.pr.boostyou.api.model.User;
import com.pr.boostyou.controller.PromotionController;
import com.pr.boostyou.util.AnalyticUtils;
import com.pr.boostyou.util.AppConstants;
import com.pr.boostyou.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.boostyou.util.AppConstants.AVATAR_URL_PREFFIX_END;
import static com.pr.boostyou.util.AppConstants.AVATAR_URL_PREFFIX_START;
import static com.pr.boostyou.util.AppConstants.TK;

public class SplashActivity extends AppCompatActivity {

	private InstaProfile instaProfile;
	private TextView startBtnView = null;
	private InterstitialAd interstitialAd;
	private LinearLayout bottomAdContainer = null;
	private LinearLayout topAdContainer = null;
	//private SimpleDraweeView splashIcView = null;
	private LinearLayout goBtnContainer = null;
	private TextView termsConditionTxtView = null;
	private TextView editTextuserName = null;
	private TextView titleTxtView = null;
	private ProgressBar progressBar = null;
	private ConstraintLayout parentView = null;
	private boolean isGoBtnClicked = false;

	private boolean needToShowAd = false;
	private User ourUser;
	private boolean isLogut = false;
	private boolean adisFailed = false;
	private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;


	private void findViews() {
		startBtnView = findViewById(R.id.startBtnView);
		startBtnView.setPaintFlags(startBtnView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
		bottomAdContainer = findViewById(R.id.bottomAdContainer);
		topAdContainer = findViewById(R.id.topAdContainer);
		termsConditionTxtView = findViewById(R.id.termsConditionTxtView);
		editTextuserName = findViewById(R.id.editTextView);
		editTextuserName.setHint("e.g @user_name");
		progressBar = findViewById(R.id.progress_bar);
		//splashIcView = findViewById(R.id.splashIcView);
		parentView = findViewById(R.id.parentView);
		titleTxtView = findViewById(R.id.titleTxtView);
		goBtnContainer = findViewById(R.id.go_btn_container);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findViews();
		hideMainViews();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isLogut = extras.getBoolean("isLogout", false);
		}
		if (isLogut) {
			Utils.saveUserName("");
		}


		termsConditionTxtView.setMovementMethod(LinkMovementMethod.getInstance());

		startBtnView.setOnClickListener(startBtnClickListener);
		goBtnContainer.setOnClickListener(startBtnClickListener);
		parentView.setOnClickListener(v -> hideKeyboard());

		if (Utils.isConnected(getApplicationContext())) {
			setUpBannerAd();
			setUpTopBannerAd();
			//createAndLoadInterstitialAd();

		} else {
			Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			return;
		}

		String logedInUserName = Utils.getUserName();
		//FIRST check if user is loged in
		if (!logedInUserName.isEmpty()) {
			progressBar.setVisibility(View.VISIBLE);
			hideMainViews();
			requestInstagramUser(logedInUserName);
		} else {

			showMainViews();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void setUpTopBannerAd() {
		onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				AdView adView = new AdView(getApplicationContext());
				adView.setAdUnitId("ca-app-pub-6630049462645854/7351569074");
				//adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");//test ad
				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				int targetAdWidth = (int) (topAdContainer.getWidth() / displayMetrics.density);
				int targetAdHeight = targetAdWidth * 50 / 320;

				AdSize adSize = new AdSize(targetAdWidth, targetAdHeight);
				//adView.setAdSize(AdSize.FULL_BANNER);
				adView.setAdSize(adSize);

				topAdContainer.addView(adView);

				AdRequest adRequest = new AdRequest.Builder()
						.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
						.addTestDevice("60E6C2BDF14E741ADCB236C34727F06C").build();
				adView.loadAd(adRequest);
				topAdContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		};
		bottomAdContainer.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	private void showMainViews() {
		editTextuserName.setVisibility(View.VISIBLE);
		titleTxtView.setVisibility(View.VISIBLE);
		goBtnContainer.setVisibility(View.VISIBLE);
	}


	private void hideMainViews() {
		editTextuserName.setVisibility(View.GONE);
		titleTxtView.setVisibility(View.GONE);
		goBtnContainer.setVisibility(View.GONE);
	}

	private void setUpBannerAd() {
		onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				AdView adView = new AdView(getApplicationContext());
				adView.setAdUnitId("ca-app-pub-6630049462645854/4855954596");
				//adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");//testad
				adView.setAdListener(new AdListener() {
					@Override
					public void onAdFailedToLoad(int i) {
						super.onAdFailedToLoad(i);
					}

				});

				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				int targetAdWidth = (int) (bottomAdContainer.getWidth() / displayMetrics.density);
				int targetAdHeight = targetAdWidth * 50 / 320;

				AdSize adSize = new AdSize(targetAdWidth, targetAdHeight);
				//adView.setAdSize(AdSize.FULL_BANNER);
				adView.setAdSize(adSize);

				bottomAdContainer.addView(adView);

				AdRequest adRequest = new AdRequest.Builder()
						.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
						.addTestDevice("60E6C2BDF14E741ADCB236C34727F06C").build();
				adView.loadAd(adRequest);
				bottomAdContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		};
		bottomAdContainer.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	private void requestInstagramUser(String logedInUserName) {
		// https://www.instagram.com/aramsadoy/?__a=1
		String instaProfileUrl = AVATAR_URL_PREFFIX_START + logedInUserName + AVATAR_URL_PREFFIX_END;
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService().getProfilePictureUrl(instaProfileUrl)
				.enqueue(new Callback<InstaProfile>() {
					@Override
					public void onResponse(Call<InstaProfile> call, Response<InstaProfile> response) {
						instaProfile = response.body();
						if (instaProfile == null) {
							//todo show wrong username key
							Utils.saveUserName("");
							isGoBtnClicked = false;
							progressBar.setVisibility(View.GONE);
							showErrorToast();
							return;
						}

						if (!instaProfile.getGraphql().getUser().getId().isEmpty()) {

							verifyUserByOurBackend(instaProfile);

						} else {
							Utils.saveUserName("");
							isGoBtnClicked = false;
							progressBar.setVisibility(View.GONE);
							showErrorToast();
						}

					}

					@Override
					public void onFailure(Call<InstaProfile> call, Throwable t) {
						//todo show wrong username key
						Utils.saveUserName("");
						isGoBtnClicked = false;
						progressBar.setVisibility(View.GONE);
						showErrorToast();

					}
				});
	}


	private void verifyUserByOurBackend(InstaProfile instaProfile) {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getUserById(instaProfile.getId(), TK).enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				User user = response.body();
				if (user != null) {
					if ("no user".equals(user.getStatus())) {
						insertNewUserToBackend(instaProfile);
					} else {
						ourUser = user;
						////LOGIN SUCCES FROM BACKEND
						AnalyticUtils.sendLoginEvent(instaProfile.getId());
						//
						startPostActivityOrFullscreenAd(instaProfile);

					}
				} else {
					Utils.saveUserName("");
					isGoBtnClicked = false;
					progressBar.setVisibility(View.GONE);
					showErrorToast();

				}
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Utils.saveUserName("");
				isGoBtnClicked = false;
				progressBar.setVisibility(View.GONE);
				showErrorToast();

			}
		});
	}

	private void startPostActivityOrFullscreenAd(InstaProfile instagramUser) {

		startPostActivity(instagramUser);

		//check for fullscreen ad every 2nd login
//		if (!checkFullScreenAd()) {
//			startPostActivity(instagramUser);
//		} else if (adisFailed) {
//			startPostActivity(instagramUser);
//		}
	}

	private void startPostActivity(InstaProfile instagramUser) {
		PromotionController promotionController = PromotionController.getInstance();
		promotionController.setCallBack(new PromotionController.PromotionsCallBack() {
			@Override
			public void onPromotionReady(PromotionModel likePromotion, PromotionModel followerPromotion) {
				Intent intent = new Intent(SplashActivity.this, PostsActivity.class);
				intent.putExtra(AppConstants.EXTRA_KEY_INSTAGRAM_USER, instagramUser);
				intent.putExtra(AppConstants.EXTRA_KEY_LIKE_PROMOTIONS, likePromotion);
				intent.putExtra(AppConstants.EXTRA_KEY_FOLLOWER_PROMOTIONS, followerPromotion);
				if (ourUser != null) {
					Utils.saveCoinsCount(ourUser.getCoins());
					intent.putExtra(AppConstants.EXTRA_KEY_OUR_USER, ourUser);
				}
				progressBar.setVisibility(View.GONE);
				startActivity(intent);
				finish();
			}

			@Override
			public void onPromotionFailed() {
				progressBar.setVisibility(View.GONE);
				isGoBtnClicked = false;
			}
		});

		promotionController.preparePromotions();
	}


	private boolean checkFullScreenAd() {
		SharedPreferences pSharedPref = getApplicationContext()
				.getSharedPreferences("loginValueCount", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			int c = pSharedPref.getInt("loginCount", 0);
			c++;
			pSharedPref.edit().putInt("loginCount", c).apply();
			if (c % 2 == 0) {
				if (interstitialAd.isLoaded()) {
					progressBar.setVisibility(View.GONE);
					interstitialAd.show();
				} else {
					needToShowAd = true;
					if (adisFailed && instaProfile != null) {
						startPostActivity(instaProfile);
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void createAndLoadInterstitialAd() {
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-6630049462645854/6879482704");
		//interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
		interstitialAd.loadAd(new AdRequest.Builder().build());

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				if (needToShowAd) {
					progressBar.setVisibility(View.GONE);
					interstitialAd.show();
				}

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				if (instaProfile != null) {
					startPostActivity(instaProfile);
				} else {
					adisFailed = true;
					isGoBtnClicked = false;
					showMainViews();
				}
			}

			@Override
			public void onAdOpened() {
			}

			@Override
			public void onAdLeftApplication() {

			}

			@Override
			public void onAdClosed() {
				if (instaProfile != null) {
					startPostActivity(instaProfile);
				} else {
					isGoBtnClicked = false;
					progressBar.setVisibility(View.GONE);
					showMainViews();
				}
			}
		});
	}


	View.OnClickListener startBtnClickListener = v -> {
		if (!Utils.isConnected(getApplicationContext())) {
			Toast.makeText(SplashActivity.this, "No Internet Connection",
					Toast.LENGTH_SHORT).show();
			return;
		}


		if (isGoBtnClicked) {
			return;
		}
		hideKeyboard();
		progressBar.setVisibility(View.VISIBLE);
		isGoBtnClicked = true;
		String txtFromEditText = String.valueOf(editTextuserName.getText());
		if (txtFromEditText.length() > 0 && String.valueOf(txtFromEditText.charAt(0)).equals("@")) {
			txtFromEditText = txtFromEditText.replace("@", "");
		}

		Utils.saveUserName(txtFromEditText);
		isLogut = false;
		requestInstagramUser(txtFromEditText);
	};

	private void insertNewUserToBackend(InstaProfile instagramUser) {
		String userId = instagramUser.getId();

		String userName = instagramUser.getGraphql().getUser().getUserName();
		insertInstaUserNameWithIdToBackend(userId,userName);

		RestClient.getInstance(getApplicationContext()).getInstaApiService().insertUserToBakend2(userId, TK)
				.enqueue(new Callback<Response2>() {
					@Override
					public void onResponse(Call<Response2> call, Response<Response2> response) {
						Response2 response2 = response.body();
						if (response2 != null && "done".equals(response2.status)) {
							/////INSERT SUCCES
							verifyUserByOurBackend(instagramUser);

						} else {
							Utils.saveUserName("");
							isGoBtnClicked = false;
							progressBar.setVisibility(View.GONE);
							showErrorToast();
						}

					}

					@Override
					public void onFailure(Call<Response2> call, Throwable t) {
						Utils.saveUserName("");
						isGoBtnClicked = false;
						progressBar.setVisibility(View.GONE);
						showErrorToast();
					}
				});
	}

	private void insertInstaUserNameWithIdToBackend(String userId, String userName) {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.insertInstagramNameAndIdToBackEnd(userId,userName,TK).enqueue(new Callback<Response2>() {
			@Override
			public void onResponse(Call<Response2> call, Response<Response2> response) {

			}

			@Override
			public void onFailure(Call<Response2> call, Throwable t) {

			}
		});
	}


	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null) {
			view = new View(SplashActivity.this);
		}
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private void showErrorToast() {
		Toast.makeText(SplashActivity.this, "Wrong Username", Toast.LENGTH_SHORT).show();
	}
}
