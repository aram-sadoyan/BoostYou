package com.pr.boostyou.callback;

import com.pr.boostyou.api.model.PurchaseModel;

import java.util.List;

public interface PurchaseSkusDetailsCallBack {
	void onSuccess(List<PurchaseModel> addedItems);

	void onFailure();

}
