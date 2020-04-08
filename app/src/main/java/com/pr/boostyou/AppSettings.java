package com.pr.boostyou;


import com.pr.boostyou.api.model.PurchaseModel;

import java.util.ArrayList;
import java.util.List;


public class AppSettings {

	private List<PurchaseModel> purchaseModels = new ArrayList<>();
	private List<PurchaseModel> addedItems = new ArrayList<>();

	private static AppSettings instance;

	public static AppSettings getInstance() {
		if (instance == null) {
			instance = new AppSettings();
		}
		return instance;
	}


	private AppSettings() {
//		if (context == null) {
//			setContext(SocialinV3.getInstance().getContext());
//		}
//		loadSharedPreferences(false);
	}







//	private void loadSharedPreferences(boolean force) {
//		if (context == null) {
//			context = SocialinV3.getInstance().getContext();
//		}
//		if (context != null) {
//			if (force) {
//				sharedPreferences = context.getSharedPreferences(SUBSCRIPTION_SHARED_PREFERENCES, MODE_PRIVATE);
//			} else {
//				new SharedPreferencesLoader().loadSharedPreference(context,
//						SUBSCRIPTION_SHARED_PREFERENCES,
//						MODE_PRIVATE,
//						loadedSharedPreferences -> sharedPreferences = loadedSharedPreferences);
//			}
//		}
//	}







	public List<PurchaseModel> getPurchaseModels() {
		return purchaseModels;
	}

	public List<PurchaseModel> getAddedItems() {
		return addedItems;
	}

	public void setPurchaseModels(List<PurchaseModel> purchaseModels) {
		this.purchaseModels.clear();
		this.purchaseModels.addAll(purchaseModels);
	}

	public void setAddedItemsFinalGoogleWithPrices(List<PurchaseModel> addedItems) {
		this.addedItems.clear();
		this.addedItems.addAll(addedItems);
	}

}
