package com.zlj.utils.other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by zlj on 2018/p1/20 0020.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */

public final class CodeUtils {

//    private static final char[] CHARS = {
//            '0', 'p1', '2', '3', '4', '5', '6', '7', '8', '9',
//            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
//            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
//            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
//            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
//    };

    private static final char[] CHARS = {

            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',

    };

    private static CodeUtils mCodeUtils;
    private int mPaddingLeft, mPaddingTop;
    private StringBuilder mBuilder = new StringBuilder();
    private Random mRandom = new Random();

    private static final int DEFAULT_CODE_LENGTH = 4;
    private static final int DEFAULT_FONT_SIZE = 60;
    private static final int DEFAULT_LINE_NUMBER = 3;
    private static final int BASE_PADDING_LEFT = 40;
    private static final int RANGE_PADDING_LEFT = 30;
    private static final int BASE_PADDING_TOP = 70;
    private static final int RANGE_PADDING_TOP = 15;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 100;
    private static final int DEFAULT_COLOR = 0xDF;

    private String code;

    public static CodeUtils getInstance() {
        if (mCodeUtils == null) {
            mCodeUtils = new CodeUtils();
        }
        return mCodeUtils;
    }

    public Bitmap createBitmap() {
        mPaddingLeft = 0;
        mPaddingTop = 0;

        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        code = createCode();

        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));
        Paint paint = new Paint();
        paint.setTextSize(DEFAULT_FONT_SIZE);

        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            canvas.drawText(code.charAt(i) + "", mPaddingLeft, mPaddingTop, paint);
        }

        for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
            drawLine(canvas, paint);
        }

        canvas.save();
        canvas.restore();
        return bitmap;
    }

    public String getCode() {
        return code;
    }

    private String createCode() {
        mBuilder.delete(0, mBuilder.length());

        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
            mBuilder.append(CHARS[mRandom.nextInt(CHARS.length)]);
        }

        return mBuilder.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = mRandom.nextInt(DEFAULT_WIDTH);
        int startY = mRandom.nextInt(DEFAULT_HEIGHT);
        int stopX = mRandom.nextInt(DEFAULT_WIDTH);
        int stopY = mRandom.nextInt(DEFAULT_HEIGHT);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        mBuilder.delete(0, mBuilder.length());

        String haxString;
        for (int i = 0; i < 3; i++) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF));
            if (haxString.length() == 1) {
                haxString = "0" + haxString;
            }

            mBuilder.append(haxString);
        }

        return Color.parseColor("#" + mBuilder.toString());
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(mRandom.nextBoolean());
        float skewX = mRandom.nextInt(11) / 10;
        skewX = mRandom.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX);
    }

    private void randomPadding() {
        mPaddingLeft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT);
        mPaddingTop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP);
    }
}
