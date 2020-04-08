package com.pr.boostyou.api.model;

import com.google.gson.annotations.SerializedName;

public class PurchaseModel {

	@SerializedName("count")
	private int coinCount = 0;

	@SerializedName("price")
	private String prcie = "";

	@SerializedName("id_sdk")
	private String sdkName = "";


	private PurchaseSkuDetail purchaseSkuDetail = null;


	public int getCoinCount() {
		return coinCount;
	}

	public String getPrcie() {
		return prcie;
	}

	public String getSdkName() {
		return sdkName;
	}

	public PurchaseSkuDetail getPurchaseSkuDetail() {
		return purchaseSkuDetail;
	}

	public void setPurchaseSkuDetail(PurchaseSkuDetail purchaseSkuDetail) {
		this.purchaseSkuDetail = purchaseSkuDetail;
	}
}
