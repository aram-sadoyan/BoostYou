package com.pr.boostyou.api;

import com.pr.boostyou.api.model.BannerModel;
import com.pr.boostyou.api.model.BossLikeModel;
import com.pr.boostyou.api.model.InstaProfile;
import com.pr.boostyou.api.model.PromotionModel;
import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.api.model.Response2;
import com.pr.boostyou.api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface InstaApiService {

	@GET
	Call<InstaProfile> getProfilePictureUrl(@Url String url);

	/////////////////// OUR BACKEND REQUESTS
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getUser.php")
	Call<User> getUserById(@Field("user_name") String id,
						   @Field("taqun_kod") String tK);


	///////////// https://www.qicharge.am/instalikes/getLikePrices.php
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getLikePrices.php")
	Call<PromotionModel> getLikePromotionList(@Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getFollowerPrices.php")
	Call<PromotionModel> getFollowerPromotionList(@Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/insertUser.php")
	Call<Response2> insertUserToBakend2(@Field("user_name") String id,
										@Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/firstPageBanner.php")
	Call<BannerModel> getBanner(@Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/updateUserCoinAdd.php")
	Call<Response2> updateUserCoinAdd(@Field("user_name") String id,
									  @Field("taqun_kod") String tK,
									  @Field("coins_offered") String offeredCoins);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/updateUserCoinMinus.php")
	Call<Response2> updateUserCoinMinus(@Field("user_name") String id,
									  @Field("taqun_kod") String tK,
									  @Field("coins_Minused") String coinsMinused);


	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getPrices.php")
	Call<List<PurchaseModel>> getPurchaseList(@Field("taqun_kod") String tK);

	////BOSSS LIKE REQUESTS    1- like 3 - follow
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/makePost.php")
	Call<BossLikeModel> requestMakePost(@Field("taqun_kod") String tK,
										@Field("task_type") int taskType,
										@Field("service_url") String serviceUrl,
										@Field("count") String count,
										@Field("user_name") String userId,
										@Field("price") int priceCoin
	);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/insertCheck.php")
	Call<Response2> requestInsertCheck
			(@Field("taqun_kod") String tK,
			 @Field("type") int taskType,
			 @Field("user_name") String userId,
			 @Field("cost") String count,
			 @Field("id_task") String idTask
			);


	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getInfoTask.php")
		////getInfotask
	Call<BossLikeModel> getInfoTask
	(@Field("taqun_kod") String tK,
	 @Field("id_task") String idTask
	);

	//////ANALYTICS
	//updateLoginCount.php
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/updateLoginCount.php")
	Call<Response2> addLoginCount(@Field("user_name") String userId,
								  @Field("taqun_kod") String tK);


}
