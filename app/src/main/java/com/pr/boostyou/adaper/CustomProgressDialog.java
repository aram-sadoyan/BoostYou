package com.pr.boostyou.adaper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.pr.boostyou.R;


public class CustomProgressDialog extends ProgressDialog {
	private View progressDialogView = null;

	private Dialog dialog = null;
	private OnCancelListener onCancelListener = null;
	private Message dismissMessage = null;
	private Context context = null;
	private OnDismissListener onDismissListener = null;
	private boolean keepDimBehind;

	private Integer color = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
	}

	public CustomProgressDialog(Context context) {
		super(context);
		init(context);
	}

	public CustomProgressDialog(Context context, boolean keepDimBehind) {
		super(context);
		init(context);
		this.keepDimBehind = keepDimBehind;
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		this.context = context;
		progressDialogView = inflater.inflate(R.layout.custom_progress_dialog_layout, null);
		//SimpleDraweeView gifContainer = progressDialogView.findViewById(R.id.custom_progress_dialog_progressbar);

//		Uri uri = new Uri.Builder()
//				.scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
//				.path(String.valueOf(R.drawable.loadinggif))
//				.build();
//
//
//		gifContainer.setImageURI(uri);
		//new FrescoLoader().loadWithParams(uri, gifContainer,true, null);
	}

	@Override
	public void setMessage(CharSequence message) {
	}

	@Override
	public void setDismissMessage(Message msg) {
		dismissMessage = msg;
	}

	@Override
	public void show() {
		if (dialog != null && dialog.isShowing() && isValidContext()) {
			return;
		}

		init(context);
		dialog = new Dialog(context);
		if (!keepDimBehind) {
			dialog.getWindow().clearFlags(WindowManager.LayoutParams.MATCH_PARENT);
		}


		if (color != null){
			setProgressBarColor(color);
		}
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (onCancelListener != null) dialog.setOnCancelListener(onCancelListener);
		if (dismissMessage != null) dialog.setDismissMessage(dismissMessage);
		if (onDismissListener != null) dialog.setOnDismissListener(onDismissListener);
		dialog.setContentView(progressDialogView);
		dialog.setCancelable(isCancelable);
		dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
		dialog.show();
	}


//	@Override
//	protected Dialog onCreateDialog(int id) {
//		//all other dialog stuff (which dialog to display)
//
//		//this line is what you need:
//		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		return dialog;
//	}

	private void setProgressBarColor(int color){
		((ProgressBar)progressDialogView).getIndeterminateDrawable().mutate().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP);
	}

	public void setColor(int color){
		this.color = color;
	}


	private boolean isCancelable = true;

	@Override
	public void setCancelable(boolean flag) {
		if (dialog != null) dialog.setCancelable(flag);
		isCancelable = flag;
	}

	private boolean isCanceledOnTouchOutside = true;

	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		if (dialog != null) dialog.setCanceledOnTouchOutside(cancel);
		isCanceledOnTouchOutside = cancel;
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		if (dialog != null) dialog.setOnDismissListener(listener);
		onDismissListener = listener;
	}

	@Override
	public void setOnCancelListener(OnCancelListener listener) {
		if (dialog != null) dialog.setOnCancelListener(listener);
		this.onCancelListener = listener;
	}

	@Override
	public void dismiss() {
		if (dialog != null) dialog.dismiss();
	}

	@Override
	public void cancel() {
		if (dialog != null) dialog.cancel();
	}

	@Override
	public boolean isShowing() {
		if (dialog == null) {
			return false;
		}
		return dialog.isShowing();
	}

	public static CustomProgressDialog show(Context context, CharSequence title,
											CharSequence message, boolean indeterminate, boolean cancelable,
											OnCancelListener cancelListener) {
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.setIndeterminate(indeterminate);
		dialog.show();
		return dialog;
	}

	public static CustomProgressDialog show(Context context, String title, String message) {
		return show(context, title, message, false);
	}

	public static CustomProgressDialog show(Context context, String title, String message,
											boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static CustomProgressDialog show(Context context, String title, String message,
											boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}

	private boolean isValidContext() {
		if (context == null || (context instanceof Activity && ((Activity) context).isFinishing())) {
			return false;
		}
		return true;
	}
}
