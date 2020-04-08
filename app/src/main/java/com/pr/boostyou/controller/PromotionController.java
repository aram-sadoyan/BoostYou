package com.pr.boostyou.controller;


import com.pr.boostyou.MainApplication;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.PromotionModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.boostyou.util.AppConstants.TK;


public class PromotionController {

	public static PromotionController instance;

	private PromotionsCallBack callBack;

	public static PromotionController getInstance() {
		if (instance == null) {
			instance = new PromotionController();
		}
		return instance;
	}


	public void preparePromotions() {
		RestClient.getInstance(MainApplication.getInstance().getApplicationContext())
				.getInstaApiService().getLikePromotionList(TK).enqueue(new Callback<PromotionModel>() {
			@Override
			public void onResponse(Call<PromotionModel> call, Response<PromotionModel> response) {
				PromotionModel likePromotion = response.body();
				if (likePromotion != null) {
					requestFollowerPromotions(likePromotion);
				} else {
					callBack.onPromotionFailed();
				}
			}

			@Override
			public void onFailure(Call<PromotionModel> call, Throwable t) {
				callBack.onPromotionFailed();
			}
		});


	}

	private void requestFollowerPromotions(PromotionModel likePromotion) {
		RestClient.getInstance(MainApplication.getInstance().getApplicationContext())
				.getInstaApiService().getFollowerPromotionList(TK)
				.enqueue(new Callback<PromotionModel>() {
					@Override
					public void onResponse(Call<PromotionModel> call, Response<PromotionModel> response) {
						PromotionModel followerPromotion = response.body();
						if (followerPromotion != null) {
							callBack.onPromotionReady(likePromotion, followerPromotion);
						} else {
							callBack.onPromotionFailed();
						}
					}

					@Override
					public void onFailure(Call<PromotionModel> call, Throwable t) {

						callBack.onPromotionFailed();
					}
				});
	}


//	private void savePromotions(String name, Map<String, String> inputMap) {
//		SharedPreferences pSharedPref = getApplicationContext()
//				.getSharedPreferences(name + "values", Context.MODE_PRIVATE);
//		if (pSharedPref != null) {
//			JSONObject jsonObject = new JSONObject(inputMap);
//			String jsonString = jsonObject.toString();
//			SharedPreferences.Editor editor = pSharedPref.edit();
//			editor.remove(name).apply();
//			editor.putString(name, jsonString);
//			editor.commit();
//		}
//	}

//	private Map<String, String> getPromotions(String name) {
//		Map<String, String> outputMap = new HashMap<String, String>();
//		SharedPreferences pSharedPref = getApplicationContext()
//				.getSharedPreferences(name + "values", Context.MODE_PRIVATE);
//		try {
//			if (pSharedPref != null) {
//				String jsonString = pSharedPref.getString(name, (new JSONObject()).toString());
//				JSONObject jsonObject = new JSONObject(jsonString);
//				Iterator<String> keysItr = jsonObject.keys();
//				while (keysItr.hasNext()) {
//					String key = keysItr.next();
//					String value = (String) jsonObject.get(key);
//					outputMap.put(key, value);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		return outputMap;
//	}


	public interface PromotionsCallBack {
		void onPromotionReady(PromotionModel likePromotion, PromotionModel FollowerPromotion);

		void onPromotionFailed();
	}


	public void setCallBack(PromotionsCallBack listener) {
		callBack = listener;
	}


}
