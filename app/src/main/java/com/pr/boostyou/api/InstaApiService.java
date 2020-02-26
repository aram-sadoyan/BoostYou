package com.pr.boostyou.api;

import com.pr.boostyou.api.model.InstaProfile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface InstaApiService {



	@GET
	Call<InstaProfile> getProfilePictureUrl(@Url String url);




}
