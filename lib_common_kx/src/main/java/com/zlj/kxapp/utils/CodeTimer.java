package com.zlj.kxapp.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时的CodeTimer
 */
public final class CodeTimer extends CountDownTimer {
	private Builder builder;
	private boolean isStarting;

	private CodeTimer(Builder builder) {
		super((long)builder.time * 1000L, (long)(builder.intervalTime * 1000));
		this.builder = builder;
		this.resetCodeTv();
	}

	private void resetCodeTv() {
		this.builder.codeTv.setEnabled(true);
		this.builder.codeTv.setText(this.builder.startText);
		this.builder.codeTv.setTextColor(this.builder.startColor);
		if (this.builder.startBackground != null) {
			this.builder.codeTv.setBackground(this.builder.startBackground);
		}

		this.isStarting = false;
	}

	public void onTick(long millisUntilFinished) {
		this.builder.codeTv.setEnabled(false);
		if(this.builder.startDynamicText!=null&&this.builder.endDynamicText!=null){
			this.builder.codeTv.setText(this.builder.startDynamicText+millisUntilFinished / 1000L + this.builder.endDynamicText);
		}else{
			this.builder.codeTv.setText(millisUntilFinished / 1000L + this.builder.dynamicText);
		}
		this.builder.codeTv.setTextColor(this.builder.dynamicColor);
		if (this.builder.dynamicBackground != null) {
			this.builder.codeTv.setBackground(this.builder.dynamicBackground);
		}

		this.isStarting = true;
	}

	public void onFinish() {
		this.resetCodeTv();
		if(this.builder.onFinishListener!=null){
			this.builder.onFinishListener.onFinish();
		}
	}

	public boolean isStarting() {
		return this.isStarting;
	}

	public interface OnFinishListener{
		void onFinish();
	}

	public static class Builder {
		private TextView codeTv;
		private int time = 60;
		private int intervalTime = 1;
		private String startText = "获取验证码";
		private String dynamicText = " 秒后重新获取";
		private String startDynamicText = null;
		private String endDynamicText = null;
		private int startColor = Color.parseColor("#ff0000");
		private int dynamicColor = Color.parseColor("#999999");
		private Drawable startBackground = null;
		private Drawable dynamicBackground = null;
		private OnFinishListener onFinishListener;



		public Builder(TextView codeTv) {
			this.codeTv = codeTv;
		}

		public Builder setDynamicColor(int dynamicColor) {
			this.dynamicColor = dynamicColor;
			return this;
		}

		public Builder setCodeTv(TextView codeTv) {
			this.codeTv = codeTv;
			return this;
		}

		public Builder setDynamicText(String dynamicText) {
			this.dynamicText = dynamicText;
			return this;
		}

		public Builder setStartDynamicText(String startDynamicText) {
			this.startDynamicText = startDynamicText;
			return this;
		}

		public Builder setOnFinishListener(OnFinishListener onFinishListener) {
			this.onFinishListener = onFinishListener;
			return this;
		}

		public Builder setEndDynamicText(String endDynamicText) {
			this.endDynamicText = endDynamicText;
			return this;
		}

		public Builder setIntervalTime(int intervalTime) {
			this.intervalTime = intervalTime;
			return this;
		}

		public Builder setStartColor(int startColor) {
			this.startColor = startColor;
			return this;
		}

		public Builder setStartText(String startText) {
			this.startText = startText;
			return this;
		}

		public Builder setTime(int time) {
			this.time = time;
			return this;
		}

		public Builder setDynamicBackground(Drawable dynamicBackground) {
			this.dynamicBackground = dynamicBackground;
			return this;
		}

		public Builder setStartBackground(Drawable startBackground) {
			this.startBackground = startBackground;
			return this;
		}

		public CodeTimer builder() {
			return new CodeTimer(this);
		}
	}
}
