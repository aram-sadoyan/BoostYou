package com.pr.boostyou.payment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.api.model.PurchaseSkuDetail;
import com.pr.boostyou.callback.PaymantIsAvailable;
import com.pr.boostyou.callback.PaymentPurchaseCallBack;
import com.pr.boostyou.callback.PurchaseSkusDetailsCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GoogleInAppBillingPaymentService extends PaymentServiceAPI implements PurchasesUpdatedListener {
	private static GoogleInAppBillingPaymentService thisInstance = null;
	volatile private BillingClient billingClient;

	private AtomicBoolean initializationStarted = new AtomicBoolean();
	private PaymentPurchaseCallBack callBack = null;

	private boolean isSetupFinished = false;
	private boolean isPaymentAvailable = false;


	private Context context;


	private GoogleInAppBillingPaymentService(Context context) {
		//loadSharedPreferences(context);
		this.context = context;
		billingClient = BillingClient
				.newBuilder(context)
				.setListener(GoogleInAppBillingPaymentService.this)
				.enablePendingPurchases()
				.build();

	}


	@Override
	public void onPaimantValid(PaymantIsAvailable paymantIsAvailable) {
		if (!initializationStarted.getAndSet(true)) {
			initPayment(paymantIsAvailable);
		}
	}

	@Override
	public void requestPurchase(Activity activity, String purchaseSku, PaymentPurchaseCallBack callBack) {
		this.callBack = callBack;
		List<String> a = new ArrayList<>();
		//a.add("test_0.0.1_insta_coin");
		//a.add("get_3_coins");
		//a.add("newcoin");
		//a.add("android.test.purchased");
		a.add(purchaseSku);

		SkuDetailsParams params = SkuDetailsParams
				.newBuilder()
				.setType(BillingClient.SkuType.INAPP)
				.setSkusList(a)
				.build();
		billingClient.querySkuDetailsAsync(params, (billingResult, skuDetailsList) -> {
			if (skuDetailsList == null) {
				Log.d("dwd", "sku is null ");
				return;
			}

			if (skuDetailsList.size() > 0) {
				BillingFlowParams flowParams = BillingFlowParams.newBuilder()
						.setSkuDetails(skuDetailsList.get(0))
						.build();
				//	purchaseListeners.put(sku, callback);
				billingClient.launchBillingFlow(activity, flowParams);
			}

			Log.d("dwd", "sku size is " + skuDetailsList.size() + " biloing result " + billingResult.getResponseCode());
			//Toast.makeText(activity, skuDetailsList.size(), Toast.LENGTH_LONG).show();

			//	Log.d("dwd","dwd " + skuDetailsList.size());

		});
	}

	@Override
	public void getShopItemsPrices(List<PurchaseModel> purchaseModels, PurchaseSkusDetailsCallBack callBack) {
		if (isPaymentAvailable) {
			//List<PurchaseModel> purchaseModels = AppSettings.getInstance().getPurchaseModels();
			List<String> a = new ArrayList<>();
			for (PurchaseModel purchaseModel : purchaseModels) {
				a.add(purchaseModel.getSdkName());
			}

			SkuDetailsParams params = SkuDetailsParams
					.newBuilder()
					.setSkusList(a)
					.setType(BillingClient.SkuType.INAPP)
					.build();

			billingClient.querySkuDetailsAsync(params, (billingResult1, skuDetailsList) -> {
				if (skuDetailsList == null) {
					return;
				}
				if (skuDetailsList.size() > 0) {
					List<PurchaseSkuDetail> purchaseSkuDetails = new ArrayList<>();
					for (SkuDetails sku : skuDetailsList) {
						for (PurchaseModel purchaseModel : purchaseModels) {
							if (purchaseModel.getSdkName().equals(sku.getSku())) {
								purchaseModel.setPurchaseSkuDetail(new PurchaseSkuDetail(
										sku.getSku(),
										sku.getPriceCurrencyCode(),
										sku.getPrice(),
										sku.getPriceAmountMicros()));
							}

						}
					}

					callBack.onSuccess(purchaseModels);

				}
			});
		}

	}

	private void initPayment(PaymantIsAvailable paymantIsAvailable) {
		billingClient.startConnection(new BillingClientStateListener() {
			@Override
			public void onBillingSetupFinished(BillingResult billingResult) {
				isSetupFinished = true;
				isPaymentAvailable = billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;
				if (isPaymentAvailable){
					paymantIsAvailable.ispaymantAvailable(isPaymentAvailable);
				}
//				if (isPaymentAvailable) {
//					List<PurchaseModel> purchaseModels = AppSettings.getInstance().getPurchaseModels();
//					List<String> a = new ArrayList<>();
//					for (PurchaseModel purchaseModel : purchaseModels) {
//						a.add(purchaseModel.getSdkName());
//					}
//
//					SkuDetailsParams params = SkuDetailsParams
//							.newBuilder()
//							.setSkusList(a)
//							.setType(BillingClient.SkuType.INAPP)
//							.build();
//
//					billingClient.querySkuDetailsAsync(params, (billingResult1, skuDetailsList) -> {
//						if (skuDetailsList == null) {
//							return;
//						}
//						if (skuDetailsList.size() > 0) {
//							List<PurchaseSkuDetail> purchaseSkuDetails = new ArrayList<>();
//							for (SkuDetails sku : skuDetailsList) {
//								purchaseSkuDetails.add(new PurchaseSkuDetail(sku.getSku(),
//										sku.getPriceCurrencyCode(),
//										sku.getPrice(),
//										sku.getPriceAmountMicros()));
//							}
//						}
//					});
//				} else {
//					Log.d("dwd", "not available");
//				}
				initializationStarted.set(false);
			}

			@Override
			public void onBillingServiceDisconnected() {
				Log.d("dwd", "failed");
			}
		});
	}

	public static GoogleInAppBillingPaymentService getInstance(Context context) {
		if (thisInstance == null) {
			thisInstance = new GoogleInAppBillingPaymentService(context);
		}
		return thisInstance;
	}


	@Override
	public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
		int responseCode = billingResult.getResponseCode();
		Log.d("dwd", "onPurchasesUpdated= " + billingResult.getResponseCode() + " responseCode= " + responseCode);
		if (responseCode == BillingClient.BillingResponseCode.OK
				&& purchases != null) {

			Purchase purchase = purchases.get(0);
			allowMultiplePurchases(purchases);
			callBack.onPaymentCallBack(purchase.getPurchaseToken(), purchase.getSku(), purchase.getOrderId());

		} else {
			Log.d("dwd", "onPurchasesUpdated= purchaseIsEmpty");
			clearHistory();

		}

	}

	private void allowMultiplePurchases(List<Purchase> purchases) {
		Purchase purchase = purchases.get(0);
		if (purchase != null) {
			ConsumeParams consumeParams = ConsumeParams.newBuilder()
					.setPurchaseToken(purchase.getPurchaseToken())
					.setDeveloperPayload(purchase.getDeveloperPayload())
					.build();
			billingClient.consumeAsync(consumeParams, (billingResult, purchaseToken) -> {
				if (BillingClient.BillingResponseCode.OK == billingResult.getResponseCode()
						&& purchaseToken != null) {
					Log.d("dwd", "Succes Response2 code for multiplePurchsse Consume " + billingResult.getResponseCode());
				} else {
					Log.d("dwd", "Error Response2 code for multiplePurchsse Consume " + billingResult.getResponseCode());

				}
			});
		}

	}


	private void clearHistory() {
		final List<Purchase> purchasesList = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
		if (purchasesList != null && !purchasesList.isEmpty()) {

			for (Purchase purchase : purchasesList) {
				ConsumeParams consumeParams = ConsumeParams.newBuilder()
						.setPurchaseToken(purchase.getPurchaseToken())
						.setDeveloperPayload(purchase.getDeveloperPayload())
						.build();
				billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
					@Override
					public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
						if (BillingClient.BillingResponseCode.OK == billingResult.getResponseCode()
								&& purchaseToken != null) {
							Log.d("dwd", "onPurchases Updated consumeAsync, purchases token removed: " + purchaseToken);
							//allowMultiplePurchases(purchases);

						} else {
							Log.d("dwd", "onPurchases some troubles happened: " + billingResult.getResponseCode());

						}
					}
				});

			}
		}
	}

//	private fun clearHistory() {
//		billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
//				.forEach {
//			billingClient.consumeAsync(it.purchaseToken) { responseCode, purchaseToken ->
//				if (responseCode == BillingClient.BillingResponse.OK && purchaseToken != null) {
//					println("onPurchases Updated consumeAsync, purchases token removed: $purchaseToken")
//				} else {
//					println("onPurchases some troubles happened: $responseCode")
//				}
//			}
//		}
//	}


}
