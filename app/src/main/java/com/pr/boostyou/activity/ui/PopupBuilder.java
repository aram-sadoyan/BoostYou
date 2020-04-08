package com.pr.boostyou.activity.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.pr.boostyou.R;
import com.pr.boostyou.adaper.PurchaseListPopupListener;
import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.api.model.PurchaseSkuDetail;
import com.pr.boostyou.callback.ActionCallback;
import com.pr.boostyou.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PopupBuilder {

	private boolean fixOrientation = false;
	private boolean needUnblockOrientation = true;
	private Dialog dialog;
	private WeakReference<Activity> activity;
	@StyleRes
	private int theme = R.style.InstaAppTheme_Light_Dialog;
	private View popupLayout;
	private ImageButton closeButton;
	private LinearLayout purchaseItemContainer;
	private TextView titleTextView;
	private TextView subtitleTextView;
	//private View actionButtonLayout;
	private TextView actionButtonText;
	private TextView actionButtonSecondText;
	private TextView secondaryButton;
	private RelativeLayout customViewContainer;
	private final View buttonsLayout = null;
	private PurchaseListPopupListener listener;
//	private MediaView mediaView;
//	private OnBoardingComponent onBoardingComponent;
	private String popupType = "";
	private String SECONDARY_BUTTON = "secondary_button";
	private String ACTION_BUTTON = "action_button";
	private String ACTION_BACK = "back";
	private String CLOSE = "close";
	private boolean showCloseButton;


	private List<PurchaseSkuDetail> purchaseSkuDetails = new ArrayList<>();
	@Nullable
	private ActionCallback actionCallback;
	@Nullable
	private OnBackPressedListener onBackPressedListener;

	/**
	 * @param activity please don't set applicationContext
	 * @param purchaseSkuDetails

	 */
//	@Deprecated
//	public PopupBuilder(Activity activity, List<PurchaseSkuDetail> purchaseSkuDetails) {
//		this(activity,purchaseSkuDetails);
//		this.purchaseSkuDetails = purchaseSkuDetails;
//	}

//	public PopupBuilder(Activity activity, int newTheme, String source, String sourceSid, String popupId) {
//		this(activity, newTheme, null, source, sourceSid, OnBoardingComponentService.generateTipsSid(), false);
//		setPopupId(popupId);
//	}

	public PopupBuilder(Activity activity, List<PurchaseModel> purchaseModels) {
		this.activity = new WeakReference<>(activity);
	//	this.fixOrientation = fixOrientation;
		// TextureView can only be used in a hardware accelerated window.
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		dialog = new Dialog(activity, theme) {
			@Override
			public void onWindowFocusChanged(boolean hasFocus) {
				super.onWindowFocusChanged(hasFocus);

			}

			@Override
			public void onDetachedFromWindow() {
				super.onDetachedFromWindow();
			}

			@Override
			public void onBackPressed() {
				if (onBackPressedListener != null) {

					onBackPressedListener.onBackPressed(this);
				} else {
					super.onBackPressed();
				}
			}
		};
		Window window = dialog.getWindow();
		if (window != null) {
			window.requestFeature(Window.FEATURE_NO_TITLE);
			window.setBackgroundDrawableResource(android.R.color.transparent);
		}
		dialog.setContentView(R.layout.layout_popup);
		// cache disabled need to implement more safe functionality
		// PicsartUtils.enableHttpResponseCache(activity);

//		popupLayout = dialog.findViewById(R.id.pop_up_layout);
//		buttonsLayout = dialog.findViewById(R.id.ll_buttons);
//		titleTextView = dialog.findViewById(R.id.tv_header);
//		subtitleTextView = dialog.findViewById(R.id.tv_content);
		//actionButtonLayout = dialog.findViewById(R.id.btn_positive);
//		actionButtonText = dialog.findViewById(R.id.btn_positive_tv);
//		actionButtonSecondText = dialog.findViewById(R.id.btn_positive_second_tv);
//		secondaryButton = dialog.findViewById(R.id.btn_negative);
//		customViewContainer = dialog.findViewById(R.id.rl_custom_view);
		closeButton = dialog.findViewById(R.id.btn_close);
		purchaseItemContainer = dialog.findViewById(R.id.purchase_item_container);
		closeButton.setOnClickListener(v -> {
			if (listener != null) {
				listener.onDismiss();
			}
			dismiss();

		});
		//setSecondaryButtonAction(null).setActionButtonAction(null);
		dialog.setOnDismissListener(dialog -> {
			unblockOrientation();
			if (listener != null) {
				listener.onDismiss();
			}
		});
		dialog.setOnCancelListener(dialogInterface -> {
			if (listener != null) {
				listener.onCancel();
			}

		});
		dialog.setOnShowListener(dialog -> {
			if (listener != null) {
				listener.onShow();
			}
		});
		// changed default value to false.
		dialog.setCanceledOnTouchOutside(false);
			Resources resources = dialog.getContext().getResources();
			// init default button gradient
			GradientDrawable drawable = new GradientDrawable(
					GradientDrawable.Orientation.BL_TR, new int[]{
					resources.getColor(R.color.colorAccent),
					resources.getColor(R.color.colorPrimary)
			}
			);
			drawable.setCornerRadius(dialog.getContext().getResources().getDimension(R.dimen.btn_corner_radius));
		//	actionButtonLayout.setBackground(drawable);

		for (PurchaseModel purchaseModel : purchaseModels){
			LayoutInflater inflater = LayoutInflater.from(activity);
			View v = inflater.inflate(R.layout.layout_purchase_card_item, purchaseItemContainer, false);
			TextView offerCoinTxtView = v.findViewById(R.id.offerTextView);
			TextView priceTxtView = v.findViewById(R.id.priceTextView);
			//todo set price with currencycode
			offerCoinTxtView.setText(String.valueOf(purchaseModel.getCoinCount()));
			PurchaseSkuDetail purchaseSkuDetail = purchaseModel.getPurchaseSkuDetail();
			if (purchaseSkuDetail == null){
				return;
			}
			priceTxtView.setText(String.valueOf(purchaseSkuDetail.getPrice()));
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("dwd","dwd " + purchaseModel.getPrcie().toString());
					listener.onButtonClick(purchaseModel);
					//todo get clicked item and stat purchase flow (billing flow)
				}
			});

			purchaseItemContainer.addView(v);
		}


	}


	public PopupBuilder setPopupId(String popupId) {
		return this;
	}

	public PopupBuilder setShowCloseButton(boolean showCloseButton) {
		this.showCloseButton = showCloseButton;
		if (closeButton != null) {
			closeButton.setVisibility(showCloseButton ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	/**
	 * Header text is big text like title
	 *
	 * @param text
	 */
	public PopupBuilder setTitleText(String text) {
		if (!TextUtils.isEmpty(text)) {
			titleTextView.setText(text);
			setHeaderVisibility(true);
		} else {
			setHeaderVisibility(false);
		}
		return this;
	}

//	/**
//	 * Set and init media data.
//	 *
//	 * @param mediaData
//	 */
//	public PopupBuilder setMediaData(MediaData mediaData) {
//		int rootViewWidth;
//		if (!isLandscape) {
//			rootViewWidth = (int) getDialog().getContext().getResources().getDimension(R.dimen.popup_width);
//		} else {
//			rootViewWidth = PicsartUtils.convertDpToPixel(496);
//			Window window = getDialog().getWindow();
//			if (window != null) {
//				window.setLayout(PicsartUtils.convertDpToPixel(496), PicsartUtils.convertDpToPixel(240));
//			}
//		}
//		if (mediaData != null) {
//			if (!isLandscape) {
//				mediaView.setMediaData(mediaData, rootViewWidth);
//				if (mediaData.isFullSize()) {
//					UiUtils.setImageViewColorFilter(closeButton, Color.WHITE, 0);
//				}
//			} else {
//				mediaView.setMediaData(mediaData, PicsartUtils.convertDpToPixel(192));
//			}
//		}
//		popupLayout.setPadding(0, 0, 0, popupLayout.getPaddingBottom());
//		return this;
//	}

	/**
	 * Set and init media data.
	 *
	 * @param view
	 */
	public PopupBuilder setCustomView(View view) {
		customViewContainer.removeAllViews();
		customViewContainer.addView(view);
		return this;
	}

	private void setContentVisibility(boolean visible) {
		subtitleTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	private void setHeaderVisibility(boolean visible) {
		titleTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

//	/**
//	 * Content text is small text like description
//	 *
//	 * @param text
//	 */
//	public PopupBuilder setSubtitleText(String text) {
//		if (!TextUtils.isEmpty(text)) {
//			setContentVisibility(true);
//			if (titleTextView.getVisibility() != View.VISIBLE) {
//				float padding = subtitleTextView.getContext().getResources().getDimension(R.dimen.subtitle_top_padding);
//				subtitleTextView.setPadding(0, (int) padding, 0, 0);
//			}
//			subtitleTextView.setText(text);
//		} else {
//			setContentVisibility(false);
//		}
//		return this;
//	}

	public PopupBuilder setFixOrientation(boolean fixOrientation) {
		this.fixOrientation = fixOrientation;
		return this;
	}

	/**
	 * Positive button is pink top button
	 *
	 * @param text
	 */
	public PopupBuilder setActionButtonText(String text) {
		actionButtonText.setText(text);
		return this;
	}

	public PopupBuilder setActionButtonSecondText(String text) {
		if (TextUtils.isEmpty(text)) {
			return this;
		}
		Activity activity = this.activity.get();
		if (activity == null) {
			return this;
		}
		ViewGroup.LayoutParams params = actionButtonText.getLayoutParams();
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		actionButtonText.requestLayout();
		actionButtonSecondText.setVisibility(View.VISIBLE);
		actionButtonSecondText.setText(text);
		int padding = Utils.convertDpToPixel(12);
//		actionButtonLayout.setPadding(actionButtonLayout.getPaddingLeft(), padding,
//				actionButtonLayout.getPaddingRight(), padding);
		return this;
	}

//	private PopupBuilder setActionButtonAction(final String action) {
//		actionButtonLayout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				Bundle extras = new Bundle();
//
//				if (actionCallback != null) {
//					//actionCallback.onActionClick(onBoardingComponent.getId(), action, extras);
//				} else {
//					if (!TextUtils.isEmpty(action) && activity.get() != null) {
//						//HookUtils.openHookDirect(activity.get(), Uri.parse(action), extras);
//					}
//				}
//				if (listener != null) {
//					listener.onButtonClick(true);
//				}
//
//			}
//		});
//		return this;
//	}

//	public PopupBuilder setActionButtonActionListener(@NonNull final OnButtonClickListener clickListener) {
//		actionButtonLayout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//					clickListener.onButtonClick(null);
////				dismiss();
//
//			}
//		});
//		return this;
//	}

	public PopupBuilder setSecondaryButtonActionListener(@NonNull final OnButtonClickListener clickListener) {
		secondaryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

					clickListener.onButtonClick(null);
//				dismiss();

			}
		});
		return this;
	}

	public void setSecondaryButtonVisibility(boolean visible, int paddingBottom) {
		secondaryButton.setVisibility(visible ? View.VISIBLE : View.GONE);
		if (visible) {
				popupLayout.setPadding(popupLayout.getPaddingLeft(), popupLayout.getPaddingTop(),
						popupLayout.getPaddingRight(), paddingBottom);

		}
	}

	public void hideSecondaryButton() {
		secondaryButton.setVisibility(View.GONE);
	}

//	public void setActionButtonVisibility(boolean visible, int paddingBottom) {
//		actionButtonLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
//		if (visible) {
//				popupLayout.setPadding(popupLayout.getPaddingLeft(), popupLayout.getPaddingTop(),
//						popupLayout.getPaddingRight(), paddingBottom);
//
//		}
//	}


	/**
	 * Negative button is white bottom button
	 *
	 * @param text
	 */
	public PopupBuilder setSecondaryButtonText(String text) {
		if (!TextUtils.isEmpty(text)) {
			setSecondaryButtonVisibility(true, Utils.convertDpToPixel(4));
			secondaryButton.setText(text);
		}
		return this;
	}

//	public PopupBuilder setSecondaryButtonAction(final String action) {
//		secondaryButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				if (!TextUtils.isEmpty(action) && activity.get() != null) {
//					//HookUtils.openHookDirect(activity.get(), action);
//				}
//				if (listener != null) {
//					listener.onButtonClick(false);
//				}
//
//
//			}
//		});
//		return this;
//	}

	public PopupBuilder setListeners(PurchaseListPopupListener listener) {
		this.listener = listener;
		return this;
	}

	public Dialog show() {

		Activity activity = this.activity.get();
		if (activity == null || activity.isFinishing()) {
			return null;
		}
		fixOrientation();
		dialog.show();

//			if (isLandscape) {
//				Window window = getDialog().getWindow();
//				if (window != null) {
//					window.setLayout(Utils.convertDpToPixel(328), Utils.convertDpToPixel(240));
//				}
//			}

		return dialog;
	}

	private void fixOrientation() {
		if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O && fixOrientation && activity.get() != null) {
			int landscape = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
			if (activity.get().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				landscape = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
			}
			needUnblockOrientation = activity.get().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
			activity.get().setRequestedOrientation(landscape);
		}
	}

	private void unblockOrientation() {
		if (fixOrientation && needUnblockOrientation && activity.get() != null && Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
			activity.get().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * get dialog doing some stuff
	 * no need to be public after show we return Dialog, also public dismiss call added.
	 *
	 * @return
	 */
	private Dialog getDialog() {
		return dialog;
	}

	public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
		getDialog().setCanceledOnTouchOutside(canceledOnTouchOutside);
	}


//	public PopupBuilder setActionCallback(@Nullable ActionCallback actionCallback) {
//		this.actionCallback = actionCallback;
//		return this;
//	}

	public PopupBuilder setOnBackPressedListener(@Nullable OnBackPressedListener onBackPressedListener) {
		this.onBackPressedListener = onBackPressedListener;
		return this;
	}


//	private void setStyle(OnBoardingComponentStyle style) {
//		popupLayout.setBackground(style.getBackgroundDrawable());
//		titleTextView.setTextColor(style.getTitleColor());
//		subtitleTextView.setTextColor(style.getSubtitleColor());
//		actionButtonLayout.setBackground(style.getActionButtonDrawable());
//		actionButtonText.setTextColor(style.getActionButtonTextColor());
//		actionButtonSecondText.setTextColor(style.getActionButtonSecondTextColor());
//		// do not set color, button need to be transparent (by Design guides).
//		// secondaryButton.setBackgroundDrawable(style.getSecondaryButtonDrawable());
//		secondaryButton.setTextColor(style.getSecondaryButtonTextColor());
//	}

	public interface OnButtonClickListener {
		void onButtonClick(@Nullable String action);
	}

	public interface OnBackPressedListener {
		void onBackPressed(@NonNull Dialog dialog);
	}

}
