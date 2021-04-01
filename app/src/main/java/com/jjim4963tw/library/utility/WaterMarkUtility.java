package com.jjim4963tw.library.utility;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WaterMarkUtility {

    /**
     * 浮水印文字
     */
    private String mText;
    /**
     * 字體顏色 十六進制：0xAEAEAEAE
     */
    private int mTextColor;
    /**
     * 字體大小，單位 SP
     */
    private float mTextSize;
    /**
     * 旋轉角度
     */
    private float mRotation;
    private static WaterMarkUtility sInstance;

    private WaterMarkUtility() {
        mText = "";
        mTextColor = 0xAEAEAEAE;
        mTextSize = 18;
        mRotation = -30;
    }

    public static WaterMarkUtility getInstance() {
        if (sInstance == null) {
            synchronized (WaterMarkUtility.class) {
                sInstance = new WaterMarkUtility();
            }
        }
        return sInstance;
    }

    /**
     * 設定浮水印文字
     *
     * @param text 文字內容
     * @return WaterMarkUtility 實體
     */
    public WaterMarkUtility setText(String text) {
        mText = text;
        return sInstance;
    }

    /**
     * 設定字體大小
     *
     * @param color 顏色，十六進制，例如：0xAEAEAEAE
     * @return WaterMarkUtility 實體
     */
    public WaterMarkUtility setTextColor(int color) {
        mTextColor = color;
        return sInstance;
    }

    /**
     * 設定文字大小
     *
     * @param size 大小，單位SP
     * @return WaterMarkUtility 實體
     */
    public WaterMarkUtility setTextSize(float size) {
        mTextSize = size;
        return sInstance;
    }

    /**
     * 設定旋轉角度
     *
     * @param degrees 度數
     * @return WaterMarkUtility 實體
     */
    public WaterMarkUtility setRotation(float degrees) {
        mRotation = degrees;
        return sInstance;
    }

    /**
     * 顯示浮水印
     *
     * @param activity 需要浮水印的頁面
     */
    public void show(Activity activity) {
        show(activity, mText);
    }

    /**
     * 顯示浮水印
     *
     * @param activity 需要浮水印的頁面
     * @param text     浮水印文字
     */
    public void show(Activity activity, String text) {
        WatermarkDrawable drawable = new WatermarkDrawable();
        drawable.textContent = text;
        drawable.textColor = mTextColor;
        drawable.textSize = mTextSize;
        drawable.degree = mRotation;

        ViewGroup rootView = activity.findViewById(android.R.id.content);
        FrameLayout layout = new FrameLayout(activity);
        layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackground(drawable);
        rootView.addView(layout);
    }

    private class WatermarkDrawable extends Drawable {
        private String textContent;
        private String textFont = null;
        private int textColor = 0xAE000000;
        private float degree = -30.0f;//旋轉角度
        private float textSize = 25;//字體大小，單位 SP
        private double alpha = 0.5d;//透明度

        public WatermarkDrawable() {
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            // 取得螢幕長寬
            int height = getBounds().bottom;
            int width = getBounds().right;

            // 將文字大小單位轉為SP
            float scaledSizeInPixels = spToPx(textSize);

            TextPaint textPaint = new TextPaint();
            textPaint.setColor(textColor);
            textPaint.setTextSize(scaledSizeInPixels);
            textPaint.setAntiAlias(true);
            if (textFont != null && !textFont.isEmpty()) {
                textPaint.setTypeface(Typeface.create(textFont, Typeface.BOLD));
            }

            // 取得實際文字長度
            float stringSize = textPaint.measureText(textContent);

            // 文字置中
            StaticLayout layout = new StaticLayout(textContent, textPaint, (int) stringSize, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);

            // 使用三角函數計算出旋轉角度後的寬高
            float textWidth = (float) Math.abs(Math.cos(Math.toRadians(degree)) * stringSize);
            float textHeight = (float) Math.abs(Math.tan(Math.toRadians(degree)) * textWidth);

            float halfWidth = width / 2.0f;
            float paddingHeight = (height - textHeight * 5) / 4.0f;

            canvas.translate(0, textHeight/1.5f);

            for (int i = 0; i < 5; i++) {
                canvas.save();
                if (i % 2 == 0) {
//                    canvas.translate(halfWidth - (textWidth / 2.0f), (paddingHeight + textHeight) * i);

                    canvas.translate((halfWidth / 2.0f) - (textWidth / 2.0f), (paddingHeight + textHeight) * i);
                    canvas.rotate(degree);
                    layout.draw(canvas);
                    canvas.restore();

                    canvas.save();
                    canvas.translate(halfWidth + (halfWidth / 2.0f) - (textWidth / 2.0f), (paddingHeight + textHeight) * i);
                } else {
                    canvas.translate(0, (paddingHeight + textHeight) * i);
                    canvas.rotate(degree);
                    layout.draw(canvas);
                    canvas.restore();

                    canvas.save();
                    canvas.translate(halfWidth - (textWidth / 2.0f), (paddingHeight + textHeight) * i);
                    canvas.rotate(degree);
                    layout.draw(canvas);
                    canvas.restore();

                    canvas.save();
                    canvas.translate(width - textWidth, (paddingHeight + textHeight) * i);
                }
                canvas.rotate(degree);
                layout.draw(canvas);
                canvas.restore();
            }
        }

        private int spToPx(float spValue) {
            float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        }

        @Override
        public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}
