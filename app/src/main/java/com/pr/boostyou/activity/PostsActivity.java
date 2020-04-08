package com.pr.boostyou.activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pr.boostyou.AppSettings;
import com.pr.boostyou.R;
import com.pr.boostyou.activity.ui.FrescoLoader;
import com.pr.boostyou.activity.ui.PopupBuilder;
import com.pr.boostyou.activity.ui.ZoomCenterCardLayoutManager;
import com.pr.boostyou.adaper.CustomProgressDialog;
import com.pr.boostyou.adaper.PostAdapter;
import com.pr.boostyou.adaper.PurchaseListPopupListener;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.BannerModel;
import com.pr.boostyou.api.model.BossLikeModel;
import com.pr.boostyou.api.model.InstaProfile;
import com.pr.boostyou.api.model.OfferCardItem;
import com.pr.boostyou.api.model.PromotionModel;
import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.api.model.Response2;
import com.pr.boostyou.api.model.User;
import com.pr.boostyou.callback.PaymentPurchaseCallBack;
import com.pr.boostyou.payment.PaymentServiceAPI;
import com.pr.boostyou.util.AppConstants;
import com.pr.boostyou.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.boostyou.util.AppConstants.API_ACCESS_TOKEN;
import static com.pr.boostyou.util.AppConstants.EXTRA_KEY_FOLLOWER_PROMOTIONS;
import static com.pr.boostyou.util.AppConstants.EXTRA_KEY_INSTAGRAM_USER;
import static com.pr.boostyou.util.AppConstants.EXTRA_KEY_LIKE_PROMOTIONS;
import static com.pr.boostyou.util.AppConstants.SHARED;
import static com.pr.boostyou.util.AppConstants.TK;

public class PostsActivity extends AppCompatActivity {


	private PostAdapter postAdapter = null;
	RecyclerView horizontalRecView = null;
	LinearLayout offerContainer = null;
	LinearLayout addContainer = null;
	SimpleDraweeView avatarImgView = null;
	SimpleDraweeView bottomImgView = null;
	ImageView addCoinTxtView = null;
	TextView coinCountTxtView = null;
	TextView termsConditionTxtView = null;
	TextView noPostText = null;
	TextView usernameTxtView = null;
	TextView followCount = null;
	LinearLayout bannerContainer = null;
	SimpleDraweeView bannerImgView = null;
	SimpleDraweeView icDoneView = null;
	SimpleDraweeView logOutIcView = null;
	ProgressBar avatarLoadingIdTaskProgressBar = null;
	private View dotView = null;
	private boolean isProfileMode = true;
	private BannerModel bannerModel = null;

	List<OfferCardItem> likeCardPromotions = new ArrayList<>();
	List<OfferCardItem> followerCardPromotions = new ArrayList<>();

	private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

	private User ourUser = null;
	private InstaProfile instaProfile = null;

	private int selectedAdapterPosition = -1;

	Map<String, String> savedPostMap = new HashMap<String, String>();
	String userName = "";

	private CustomProgressDialog progressDialog = null;

	private Animation zoomToMed;


	private void findViews() {
		horizontalRecView = findViewById(R.id.horizontalRecView);
		offerContainer = findViewById(R.id.offerContainer);
		avatarImgView = findViewById(R.id.avatarImgView);
		addCoinTxtView = findViewById(R.id.addCoinTxtView);
		addContainer = findViewById(R.id.addContainer);
		bannerContainer = findViewById(R.id.bannerContainer);
		bannerImgView = findViewById(R.id.bannerImgView);
		dotView = findViewById(R.id.dotView);
		bottomImgView = findViewById(R.id.bottomImgView);
		noPostText = findViewById(R.id.noPostText);
		coinCountTxtView = findViewById(R.id.coinCountTxtView);
		usernameTxtView = findViewById(R.id.usernameTxtView);
		avatarLoadingIdTaskProgressBar = findViewById(R.id.avatarProgressBar);
		termsConditionTxtView = findViewById(R.id.termsConditionTxtView);
		logOutIcView = findViewById(R.id.logOutIcView);
		followCount = findViewById(R.id.followCount);
		icDoneView = findViewById(R.id.icDoneView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posts);
		progressDialog = new CustomProgressDialog(PostsActivity.this);
		progressDialog.setCancelable(false);

		findViews();
		setUpBannerAd();

		PromotionModel likePromotions = null;
		PromotionModel followerPromotions = null;


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			instaProfile = (InstaProfile) getIntent().getExtras()
					.getSerializable(EXTRA_KEY_INSTAGRAM_USER);
			likePromotions = (PromotionModel) getIntent().getExtras()
					.getSerializable(EXTRA_KEY_LIKE_PROMOTIONS);
			followerPromotions = (PromotionModel) getIntent().getExtras()
					.getSerializable(EXTRA_KEY_FOLLOWER_PROMOTIONS);
			ourUser = (User) getIntent().getExtras()
					.getSerializable(AppConstants.EXTRA_KEY_OUR_USER);
		}

		likeCardPromotions = Utils.getPromotionCardItems(likePromotions);
		followerCardPromotions = Utils.getPromotionCardItems(followerPromotions);

		savedPostMap = Utils.loadMap("likes");


		if (instaProfile != null) {
			userName = instaProfile.getUserName();

			setToolbarTexts(userName);
			initAvatarImage(instaProfile.getGraphql().getUser().getProfilePictureUrl(),
					instaProfile.getGraphql().getUser().getEdgeFollowedBy().getFollowCount());
			initBannerView();
			initRecyclerView(instaProfile);
			if (instaProfile.getGraphql().getUser().getEdgeOwnerToTimeLineMedia().getEdgesList().size() == 0) {
				horizontalRecView.setVisibility(View.INVISIBLE);
				noPostText.setVisibility(View.VISIBLE);
			} else {
				horizontalRecView.setVisibility(View.VISIBLE);
				noPostText.setVisibility(View.GONE);
			}
			isProfileMode = true;
			initOffersView(isProfileMode);
			getAndSaveLikePromotion();
			initBottomImageView();
			checkAvatarIdTaskProgress();
			termsConditionTxtView.setMovementMethod(LinkMovementMethod.getInstance());
		}


		addCoinTxtView.setOnClickListener(v -> {
			progressDialog.show();
			startPurchaseListDialog();
		});

		logOutIcView.setOnClickListener(v -> {

			DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						Intent intent = new Intent(PostsActivity.this, SplashActivity.class);
						intent.putExtra("isLogout", true);
						startActivity(new Intent(intent));
						finish();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(PostsActivity.this);
			builder.setMessage("Are you sure?").setPositiveButton("Logout", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		});

	}

	private void setToolbarTexts(String userName) {
		if (ourUser != null) {
			coinCountTxtView.setText(String.valueOf(Utils.getCoinsCount()));
			usernameTxtView.setText("@" + userName);
		}
	}

	private void initAvatarImage(String profilePicUrl, int followCount) {
		new FrescoLoader().loadWithParams(
				Uri.parse(profilePicUrl),
				avatarImgView, false);

		this.followCount.setText(String.valueOf(followCount));

		avatarImgView.setOnClickListener(v -> {
			if (!isProfileMode) {
				isProfileMode = true;
				initOffersView(isProfileMode);
			}
		});
	}


	private void initOffersView(boolean isProfileMode) {
		offerContainer.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(PostsActivity.this);
		List<OfferCardItem> offerCardItems;
		@DrawableRes int resId = R.drawable.ic_heart;

		if (isProfileMode) {
			//PROFILE CONFIG LIST
			dotView.setSelected(true);
			offerCardItems = followerCardPromotions;
			resId = R.drawable.follower;
		} else {
			//POST CONFIG LIST
			dotView.setSelected(false);
			offerCardItems = likeCardPromotions;
		}

		for (int i = 0; i < offerCardItems.size(); i++) {
			View v = inflater.inflate(R.layout.layout_offer_card_item, offerContainer, false);
			OfferCardItem offerCardItem = offerCardItems.get(i);
			TextView offerTxtView = v.findViewById(R.id.offerTxtView);
			TextView costCoinTxtView = v.findViewById(R.id.costCoinTxtView);
			ImageView leftIcView = v.findViewById(R.id.icLeftView);
			leftIcView.setBackgroundResource(resId);
			offerTxtView.setText(offerCardItem.getOfferCount());
			costCoinTxtView.setText(String.valueOf(offerCardItem.getCostCoinCount()));
			v.setTag(R.id.price_coin, offerCardItem.getCostCoinCount());
			v.setTag(offerCardItem.getOfferCount());
			offerContainer.addView(v);
			v.setOnClickListener(v1 -> {
				String promoteCount = (String) v.getTag();
				int priceCoin = (Integer) v.getTag(R.id.price_coin);

				DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
					switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							progressDialog.show();
							proceedPromotion(promoteCount, priceCoin);
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							//No button clicked
							progressDialog.dismiss();
							break;
					}
				};
				String str = isProfileMode ? "followers" : "likes";
				AlertDialog.Builder builder = new AlertDialog.Builder(PostsActivity.this);
				builder.setMessage("You will have " + promoteCount + " " + str +  " by " + priceCoin + " coins. Are You sure?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
			});
		}
	}


	private void proceedPromotion(String promoteCount, int priceCoin) {
		final String likesCountLocal = promoteCount;
		if (!isProfileMode && postAdapter == null) {
			return;
		}
		String currentPermalink = isProfileMode ?
				"https://www.instagram.com/" + userName
				: postAdapter.getSelectedPermanentLink(selectedAdapterPosition);
		final int taskType = isProfileMode ? 3 : 1;

		if (Utils.getCoinsCount() < priceCoin) {
			openUserChoisePurchaseDialog(promoteCount, priceCoin);
			return;
		}

		///check coin heriqcnel nafsyaki cherez backend
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService()
				.updateUserCoinMinus(instaProfile.getId(), TK, String.valueOf(priceCoin)).enqueue(new Callback<Response2>() {
			@Override
			public void onResponse(Call<Response2> call, Response<Response2> response) {
				if (response.body() != null) {
					if ("done".equals(response.body().status)) {
						makePost(taskType, currentPermalink, likesCountLocal, instaProfile.getId(), priceCoin);
					} else if ("anbavarar".equals(response.body().status)) {
						openUserChoisePurchaseDialog(promoteCount, priceCoin);
					}
				} else {
					//something went wrong
					progressDialog.dismiss();
				}
			}

			@Override
			public void onFailure(Call<Response2> call, Throwable t) {
				//something went wrong
				progressDialog.dismiss();
			}
		});

	}

	private void openUserChoisePurchaseDialog(String promoteCount, int priceCoin){
		DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					progressDialog.dismiss();
					startPurchaseListDialog();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					//No button clicked
					progressDialog.dismiss();
					break;
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(PostsActivity.this);
		builder.setMessage("Your ballance is insufficient. Please recharge.")
				.setPositiveButton("Recharge", dialogClickListener)
				.setNegativeButton("cancel", dialogClickListener).show();
	}

	private void makePost(int taskType, String currentPermalink, String likesCountLocal, String id, int priceCoin) {

		RestClient.getInstance(getApplicationContext())
				.getInstaApiService()
				.requestMakePost(
						TK,
						taskType,
						currentPermalink,
						likesCountLocal,
						id,
						priceCoin
				).enqueue(new Callback<BossLikeModel>() {
			@Override
			public void onResponse(Call<BossLikeModel> call, Response<BossLikeModel> response) {
				progressDialog.dismiss();
				showIcDoneWithAniomationAndHide();

				BossLikeModel bossLikeModel = response.body();
				if (bossLikeModel != null && bossLikeModel.getSuccess()) {
					String bossLikeTaskId = bossLikeModel.getData().getTask().getId();
					if (isProfileMode) {
						Map<String, String> followerMap = Utils.loadMap("follower");
						followerMap.put(userName, bossLikeTaskId);
						Utils.saveMap("follower", followerMap);

						checkAvatarIdTaskProgress();
					} else {
						Map<String, String> likesMap = Utils.loadMap("likes");
						likesMap.put(currentPermalink, bossLikeTaskId);
						Utils.saveMap("likes", likesMap);
					}

					progressDialog.dismiss();
					showIcDoneWithAniomationAndHide();

					/////TODO update user coin by getUser or local discrement
					insertCheckFromOurBackand(
							instaProfile.getId(),
							likesCountLocal,
							taskType,
							bossLikeModel.getData().getTask().getId()
					);
				}

			}

			@Override
			public void onFailure(Call<BossLikeModel> call, Throwable t) {
				progressDialog.dismiss();
			}
		});
	}

	private void insertCheckFromOurBackand(String id, String costCount, int taskType, String idTask) {
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService()
				.requestInsertCheck(TK, taskType, id, costCount, idTask).enqueue(new Callback<Response2>() {
			@Override
			public void onResponse(Call<Response2> call, Response<Response2> response) {
			}

			@Override
			public void onFailure(Call<Response2> call, Throwable t) {

			}
		});

	}


	private void showIcDoneWithAniomationAndHide() {
		icDoneView.setVisibility(View.VISIBLE);
		zoomToMed = AnimationUtils.loadAnimation(this, R.anim.zoom_to_medium);
		zoomToMed.setInterpolator(new LinearInterpolator());

		zoomToMed.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						icDoneView.setVisibility(View.GONE);
					}
				}, 300);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		icDoneView.startAnimation(zoomToMed);
	}

	private void initBannerView() {
		RestClient.getInstance(getApplicationContext()).
				getInstaApiService().getBanner(TK).enqueue(new Callback<BannerModel>() {
			@Override
			public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
				bannerModel = response.body();
				if (bannerModel == null || bannerModel.getUrl().isEmpty()) {
					return;
				}

				new FrescoLoader().loadWithParams(
						Uri.parse(AppConstants.BANNER_URL_PREFFIX + bannerModel.getUrl()),
						bannerImgView, false);


				bannerContainer.setOnClickListener(v -> {
					//todo banner click
					Log.d("dwd","wdwddw");
					Log.d("dwd","wdwddw");

				});

			}

			@Override
			public void onFailure(Call<BannerModel> call, Throwable t) {

			}
		});


	}

	private void initRecyclerView(InstaProfile instaProfile) {

		SharedPreferences sharedPref = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		String currentAccesToken = sharedPref.getString(API_ACCESS_TOKEN, null);

		postAdapter = new PostAdapter(
				PostsActivity.this,
				instaProfile.getGraphql().getUser().getEdgeOwnerToTimeLineMedia(),
				currentAccesToken,
				(adapterPosition) -> {
					if (isProfileMode && postAdapter.getItemCount() != 0) {
						isProfileMode = false;
						initOffersView(isProfileMode);
					}

				});
		horizontalRecView.setAdapter(postAdapter);

		horizontalRecView.setLayoutManager(new ZoomCenterCardLayoutManager(getApplicationContext(),
				LinearLayoutManager.HORIZONTAL, false));
		horizontalRecView.setItemViewCacheSize(10);
		horizontalRecView.setDrawingCacheEnabled(true);
		horizontalRecView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		horizontalRecView.setHasFixedSize(true);
		horizontalRecView.setOnFlingListener(null);
		PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
		pagerSnapHelper.attachToRecyclerView(horizontalRecView);

		final Handler handler = new Handler();
		handler.postDelayed(() -> horizontalRecView.smoothScrollToPosition(2), 500);


		horizontalRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (isProfileMode && postAdapter.getItemCount() != 2) {
					isProfileMode = false;
					initOffersView(isProfileMode);
				}

				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					ZoomCenterCardLayoutManager layoutManager = (ZoomCenterCardLayoutManager) recyclerView.getLayoutManager();
					if (layoutManager == null) {
						return;
					}
					int from = layoutManager.findFirstCompletelyVisibleItemPosition();
					selectedAdapterPosition = from;
				}
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		selectedAdapterPosition = 0;

	}

	private void getAndSaveLikePromotion() {
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService().getLikePromotionList(TK)
				.enqueue(new Callback<PromotionModel>() {
					@Override
					public void onResponse(Call<PromotionModel> call, Response<PromotionModel> response) {
					}

					@Override
					public void onFailure(Call<PromotionModel> call, Throwable t) {

					}
				});
	}

	private void initBottomImageView() {
		new FrescoLoader().loadWithParams(
				Uri.parse("https://www.qicharge.am/instalikes/footer/bottom.png"),
				bottomImgView, false);
	}

	private void checkAvatarIdTaskProgress() {
		String followIdTask = Utils.loadMap("follower").get(userName);
		if (followIdTask == null) {

			return;
		}
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getInfoTask(TK, followIdTask).enqueue(new Callback<BossLikeModel>() {
			@Override
			public void onResponse(Call<BossLikeModel> call, Response<BossLikeModel> response) {

				BossLikeModel bossLikeModel = response.body();
				if (bossLikeModel != null) {
					int count = Integer.parseInt(bossLikeModel.getData().getTask().getCount());
					int completeCount = Integer.parseInt(bossLikeModel.getData().getTask().getCountComplete());
					if (count == completeCount) {
						Utils.saveMap("follower", new HashMap<>());
						avatarLoadingIdTaskProgressBar.setVisibility(View.GONE);
					} else {
						//showing progress for idTask
						int completePercent = 100 * completeCount / count;
						avatarLoadingIdTaskProgressBar.setProgress(completePercent);
						avatarLoadingIdTaskProgressBar.setVisibility(View.VISIBLE);
					}

				}

			}

			@Override
			public void onFailure(Call<BossLikeModel> call, Throwable t) {

				avatarLoadingIdTaskProgressBar.setVisibility(View.GONE);
			}
		});

	}


	private void startPurchaseListDialog() {
		//getting price list and show dialog
		List<PurchaseModel> addedItems = AppSettings.getInstance().getAddedItems();

		if (addedItems.isEmpty()) {
			//todo set defautl values
		}

		PopupBuilder popupBuilder = new PopupBuilder(PostsActivity.this, addedItems);
		popupBuilder.setListeners(new PurchaseListPopupListener() {
			@Override
			public void onButtonClick(PurchaseModel purchaseModel) {
				//start billing flow by this purchaseModels sku
				PaymentServiceAPI paymentServiceAPI = PaymentServiceAPI.getPaymentService(getApplicationContext());
				paymentServiceAPI.requestPurchase(PostsActivity.this, purchaseModel.getSdkName(), new PaymentPurchaseCallBack() {
					@Override
					public void onPaymentCallBack(String purchaseToken, String sku, String orderId) {

						List<PurchaseModel> currentPurchaseList = AppSettings.getInstance().getPurchaseModels();
						for (PurchaseModel model : currentPurchaseList) {
							if (sku.equals(model.getSdkName())) {
								int currentCoinCount = Utils.getCoinsCount();
								currentCoinCount += model.getCoinCount();
								Utils.saveCoinsCount(currentCoinCount);
								coinCountTxtView.setText(String.valueOf(currentCoinCount));
								//todo continue if clicked cheriqcnel

								//TODO SEND TO BACKEND for new purchase
								updateUserCoinAdd(model.getCoinCount());
								break;
							}
						}
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				});
			}

			@Override
			public void onShow() {
				super.onShow();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});
		popupBuilder.show();


//		CoinPurchaseController.getInstance().reuestPurchases(purchaseModels1, new PurchaseSkusDetailsCallBack() {
//			@Override
//			public void onSuccess(List<PurchaseModel> addedItems) {
//				if (!addedItems.isEmpty()) {
//					purchaseModels.clear();
//					purchaseModels.addAll(addedItems);
//					PopupBuilder popupBuilder = new PopupBuilder(PostsActivity.this, purchaseModels);
//					popupBuilder.setListeners(new PurchaseListPopupListener() {
//						@Override
//						public void onButtonClick(PurchaseModel purchaseModel) {
//							//start billing flow by this purchaseModels sku
//							PaymentServiceAPI paymentServiceAPI = PaymentServiceAPI.getPaymentService(getApplicationContext());
//							paymentServiceAPI.requestPurchase(PostsActivity.this, purchaseModel.getSdkName(), new PaymentPurchaseCallBack() {
//								@Override
//								public void onPaymentCallBack(String purchaseToken, String sku, String orderId) {
//									Log.d("dwd", "kjh");///g// et coin count by sku and save it and continu promote if clicked cheriqcrela
//
//									List<PurchaseModel> currentPurchaseList = AppSettings.getInstance().getPurchaseModels();
//									for (PurchaseModel model : currentPurchaseList) {
//										if (sku.equals(model.getSdkName())) {
//											int currentCoinCount = Utils.getCoinsCount();
//											currentCoinCount += model.getCoinCount();
//											Utils.saveCoinsCount(currentCoinCount);
//											coinCountTxtView.setText(String.valueOf(currentCoinCount));
//											//todo continue if clicked cheriqcnel
//
//											//TODO SEND TO BACKEND for new purchase
//											updateUserCoinAdd(model.getCoinCount());
//											break;
//										}
//									}
//									if (progressDialog != null){
//										progressDialog.dismiss();
//									}
//								}
//							});
//						}
//
//						@Override
//						public void onShow() {
//							super.onShow();
//							if (progressDialog != null){
//								progressDialog.dismiss();
//							}
//						}
//					});
//					popupBuilder.show();
//
//				} else {
//					if (progressDialog != null){
//						progressDialog.dismiss();
//					}
//					//todo set default values (purchase list)
//				}
//			}
//
//			@Override
//			public void onFailure() {
//				//todo handle error case
//				if (progressDialog != null){
//					progressDialog.dismiss();
//				}
//			}
//		});


	}

	private void updateUserCoinAdd(int coinCount) {
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService().updateUserCoinAdd(instaProfile.getId(), TK, String.valueOf(coinCount))
				.enqueue(new Callback<Response2>() {
					@Override
					public void onResponse(Call<Response2> call, Response<Response2> response) {
						if (response.body() != null && "done".equals(response.body().status)) {

							///getUSer /// update coin
							getUser();

						}
					}

					@Override
					public void onFailure(Call<Response2> call, Throwable t) {

					}
				});
	}


	private void getUser() {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getUserById(instaProfile.getId(), TK).enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				User user = response.body();
				if (user != null) {
					//todo check this there is no need to check "no user" case
					String userId = instaProfile.getId();
					if ("no user".equals(user.getStatus())) {

					} else {
						ourUser = user;
						Utils.saveCoinsCount(user.getCoins());
						coinCountTxtView.setText(String.valueOf(user.getCoins()));
					}
				}

			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {

			}
		});
	}

	private void setUpBannerAd() {
		onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				AdView adView = new AdView(getApplicationContext());
				adView.setAdUnitId("ca-app-pub-6630049462645854/3323381077");
				//adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");//testad

				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				int targetAdWidth = (int) (addContainer.getWidth() / displayMetrics.density);
				int targetAdHeight = targetAdWidth * 50 / 320;

				AdSize adSize = new AdSize(targetAdWidth, targetAdHeight);
				//adView.setAdSize(AdSize.FULL_BANNER);
				adView.setAdSize(adSize);

				addContainer.addView(adView);

				AdRequest adRequest = new AdRequest.Builder()
						.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
						.addTestDevice("60E6C2BDF14E741ADCB236C34727F06C").build();
				adView.loadAd(adRequest);
				adView.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {

					}

					@Override
					public void onAdFailedToLoad(int i) {
						super.onAdFailedToLoad(i);

					}

					@Override
					public void onAdLeftApplication() {
						super.onAdLeftApplication();
					}
				});
				addContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		};
		addContainer.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}


	public interface OnItemClickListener {
		void onItemClick(int adapterPosition);
	}
}
