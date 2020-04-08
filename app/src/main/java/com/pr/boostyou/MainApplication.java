package com.pr.boostyou;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.callback.PurchaseSkusDetailsCallBack;
import com.pr.boostyou.controller.CoinPurchaseController;
import com.pr.boostyou.payment.PaymentServiceAPI;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.boostyou.util.AppConstants.TK;

public class MainApplication extends Application {

	public static MainApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Fresco.initialize(this);
		requestPurchaseList();
	}


	private void requestPurchaseList() {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getPurchaseList(TK).enqueue(new Callback<List<PurchaseModel>>() {
			@Override
			public void onResponse(Call<List<PurchaseModel>> call, @NotNull Response<List<PurchaseModel>> response) {
				List<PurchaseModel> purchaseModels = response.body();
				if (purchaseModels != null && !purchaseModels.isEmpty()) {

					PaymentServiceAPI paymentServiceAPI = PaymentServiceAPI.getPaymentService(getApplicationContext());
					paymentServiceAPI.onPaimantValid(isAvailable -> {
						if (isAvailable){
							AppSettings.getInstance().setPurchaseModels(purchaseModels);
							setPurchaseListFromGoogle(purchaseModels);
						}
					});
				}

			}


			@Override
			public void onFailure(Call<List<PurchaseModel>> call, Throwable t) {

			}

		});
	}

	private void setPurchaseListFromGoogle(List<PurchaseModel> purchaseModels) {
		CoinPurchaseController.getInstance().reuestPurchases(purchaseModels, new PurchaseSkusDetailsCallBack() {
			@Override
			public void onSuccess(List<PurchaseModel> addedItems) {
				if (!addedItems.isEmpty()) {
					AppSettings.getInstance().setAddedItemsFinalGoogleWithPrices(addedItems);
				}

			}

			@Override
			public void onFailure() {

			}
		});
	}


	@Override
	public Context getApplicationContext() {
		return super.getApplicationContext();
	}

	public static MainApplication getInstance() {
		return instance;
	}
}
