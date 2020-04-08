package com.pr.boostyou.activity.ui;

import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;

public class CustomImageDecodeOptions extends ImageDecodeOptions {
	public int customDecodeWidth;
	public int customDecodeHeight;
	public String filePath;
	public int maxPixels;

	public CustomImageDecodeOptions(ImageDecodeOptionsBuilder b) {
		super(b);
	}

	public void setCustomDecodeWidthHeight(int width, int height, String rawFilePath) {
		this.customDecodeHeight = height;
		this.customDecodeWidth = width;
		this.filePath = rawFilePath;
	}

	public void setCustomDecodeMaxPixel(int maxPixels) {
		this.maxPixels = maxPixels;
	}
}