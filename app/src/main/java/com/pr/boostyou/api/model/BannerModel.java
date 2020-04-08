package com.pr.boostyou.api.model;

import com.google.gson.annotations.SerializedName;

public class BannerModel {


	@SerializedName("url")
	private String url = "";
	@SerializedName("count")
	private String count;
	@SerializedName("price")
	private String price;

	public String getUrl() {
		return url;
	}

	public String getCount() {
		return count;
	}

	public String getPrice() {
		return price;
	}
}
