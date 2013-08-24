package com.trio.triotictactoe.utils;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class ViewUtils {

	public static void applyAlphaToViewGroup(float initialAlpha, float finalAlpha, int transtionDuration, ViewGroup viewGroup) {
		AlphaAnimation alpha = new AlphaAnimation(initialAlpha, finalAlpha);
		alpha.setDuration(transtionDuration);
		alpha.setFillAfter(true);
		viewGroup.startAnimation(alpha);
	}

	public static void addImageToView(Activity activity, ViewGroup viewGroup, int imageWidth, int imageHeight, int imageViewId) {
		ImageView imageView = new ImageView(activity);
		imageView.setImageDrawable(activity.getResources().getDrawable(imageViewId));
		imageView.setLayoutParams(new ViewGroup.LayoutParams(imageWidth, imageHeight));
		viewGroup.addView(imageView);
	}

}
