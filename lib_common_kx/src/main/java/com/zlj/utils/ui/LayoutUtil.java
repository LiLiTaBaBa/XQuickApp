package com.zlj.utils.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.zlj.kxapp.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 文件名：	LayoutUtil
 * 作　者：	jqli
 * 时　间：	2018/3/14 16:40
 * 描　述：	Layout适配工具
 *
 * </pre>
 */
public final class LayoutUtil {

    public static String filterEmoji(String source) {
        if (source != null) {
            Pattern emoji = Pattern
                    .compile(
                            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600" +
                                    "-\u27ff]",
                            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("");
                return source;
            }
            return source;
        }
        return source;
    }

    /**
     * 输入框文本变化监听
     *
     * @param editText
     * @param imgClear
     */
    public static void setTextClearWatcher(final EditText editText,
                                           final ImageView imgClear) {
        imgClear.setVisibility(View.GONE);
        imgClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                editText.setText("");
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (!"".equals(arg0.toString())) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                }

            }
        });
        editText.setOnFocusChangeListener((arg0, arg1) -> {
            if (arg1) {
                if (!TextUtils.isEmpty(editText.getEditableText()
                        .toString())) {
                    imgClear.setVisibility(View.VISIBLE);
                }
            } else {
                imgClear.setVisibility(View.GONE);
            }

        });
    }

    public static int getViewWidth(Context context,int width) {
        return DensityUtils.dip2px(context, width);
    }

    public static int getViewHeight(Context context,int height) {
        return DensityUtils.dip2px(context, height);
    }

    public static void setTextSize(TextView txtTitle, int size) {
        if (txtTitle == null) {
            return;
        }
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getViewHeight(txtTitle.getContext(),size));
    }

    public static void setEditTextSize(EditText txtTitle, int size) {
        if (txtTitle == null) {
            return;
        }
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getViewHeight(txtTitle.getContext(),size));
    }

    public static void setEditMultiLine(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setGravity(Gravity.TOP);
        editText.setSingleLine(false);
        editText.setHorizontallyScrolling(false);
    }

    public static void setBtnTextSize(Button btn, int size) {
        if (btn == null) {
            return;
        }
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getViewHeight(btn.getContext(),size));
    }

    public static void setPadding(View view, int paddingLeft, int paddingTop,
                                  int paddingRight, int paddingBottom) {
        if (view == null) {
            return;
        }
        if (paddingLeft != -1) {
            paddingLeft = getViewWidth(view.getContext(),paddingLeft);
        }
        if (paddingTop != -1) {
            paddingTop = getViewHeight(view.getContext(),paddingTop);
        }
        if (paddingRight != -1) {
            paddingRight = getViewWidth(view.getContext(),paddingRight);
        }
        if (paddingBottom != -1) {
            paddingBottom = getViewHeight(view.getContext(),paddingBottom);
        }
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static void setLinearLayoutParam(View view, int width, int height) {
        if (view == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setLinearLayoutParamEqual(View view, int width,
                                                 int height) {
        if (view == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
            layoutParams.height = layoutParams.width;
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
            layoutParams.width = layoutParams.height;
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setRelativeLayoutParam(View view, int width, int height) {
        if (view == null) {
            return;
        }
        android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout
                .LayoutParams) view
                .getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setRelativeLayoutParamEqual(View view, int width,
                                                   int height) {
        if (view == null) {
            return;
        }
        android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout
                .LayoutParams) view
                .getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
            layoutParams.height = layoutParams.width;
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
            layoutParams.width = layoutParams.height;
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setLinearLayoutParam(View view, int width, int height,
                                            int marginLeft, int marginTop, int marginRight, int
                                                    marginBottom) {
        if (view == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
        }

        if (marginLeft != -1) {
            layoutParams.leftMargin = getViewWidth(view.getContext(),marginLeft);
        }
        if (marginTop != -1) {
            layoutParams.topMargin = getViewHeight(view.getContext(),marginTop);
        }
        if (marginRight != -1) {
            layoutParams.rightMargin = getViewWidth(view.getContext(),marginRight);
        }
        if (marginBottom != -1) {
            layoutParams.bottomMargin = getViewHeight(view.getContext(),marginBottom);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setLinearLayoutParamEqual(View view, int width,
                                                 int height, int marginLeft, int marginTop, int
                                                         marginRight,
                                                 int marginBottom) {
        if (view == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
            layoutParams.height = layoutParams.width;
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
            layoutParams.width = layoutParams.height;
        }
        if (marginLeft != -1) {
            layoutParams.leftMargin = getViewWidth(view.getContext(),marginLeft);
        }
        if (marginTop != -1) {
            layoutParams.topMargin = getViewHeight(view.getContext(),marginTop);
        }
        if (marginRight != -1) {
            layoutParams.rightMargin = getViewWidth(view.getContext(),marginRight);
        }
        if (marginBottom != -1) {
            layoutParams.bottomMargin = getViewHeight(view.getContext(),marginBottom);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setRelativeLayoutParam(View view, int width, int height,
                                              int marginLeft, int marginTop, int marginRight, int
                                                      marginBottom) {
        if (view == null) {
            return;
        }
        android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout
                .LayoutParams) view
                .getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
        }

        if (marginLeft != -1) {
            layoutParams.leftMargin = getViewWidth(view.getContext(),marginLeft);
        }
        if (marginTop != -1) {
            layoutParams.topMargin = getViewHeight(view.getContext(),marginTop);
        }
        if (marginRight != -1) {
            layoutParams.rightMargin = getViewWidth(view.getContext(),marginRight);
        }
        if (marginBottom != -1) {
            layoutParams.bottomMargin = getViewHeight(view.getContext(),marginBottom);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setRelativeLayoutParamEqual(View view, int width,
                                                   int height, int marginLeft, int marginTop, int
                                                           marginRight,
                                                   int marginBottom) {
        if (view == null) {
            return;
        }
        android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout
                .LayoutParams) view
                .getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
            layoutParams.height = layoutParams.width;
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
            layoutParams.width = layoutParams.height;
        }
        if (marginLeft != -1) {
            layoutParams.leftMargin = getViewWidth(view.getContext(),marginLeft);
        }
        if (marginTop != -1) {
            layoutParams.topMargin = getViewHeight(view.getContext(),marginTop);
        }
        if (marginRight != -1) {
            layoutParams.rightMargin = getViewWidth(view.getContext(),marginRight);
        }
        if (marginBottom != -1) {
            layoutParams.bottomMargin = getViewHeight(view.getContext(),marginBottom);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setRadioGroupLayoutParam(View view, int width,
                                                int height, int marginLeft, int marginTop, int
                                                        marginRight) {
        if (view == null) {
            return;
        }
        android.widget.RadioGroup.LayoutParams layoutParams = (android.widget.RadioGroup
                .LayoutParams) view
                .getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
        }
        if (height != -1) {
            layoutParams.height = getViewHeight(view.getContext(),height);
        }
        if (marginLeft != -1) {
            layoutParams.leftMargin = getViewWidth(view.getContext(),marginLeft);
        }
        if (marginTop != -1) {
            layoutParams.topMargin = getViewHeight(view.getContext(),marginTop);
        }
        if (marginRight != -1) {
            layoutParams.rightMargin = getViewWidth(view.getContext(),marginRight);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setRadioGroupLayoutParamEqual(View view, int width,
                                                     int height, int marginLeft, int marginTop,
                                                     int marginRight) {
        if (view == null) {
            return;
        }
        android.widget.RadioGroup.LayoutParams layoutParams = (android.widget.RadioGroup
                .LayoutParams) view
                .getLayoutParams();
        if (width != -1) {
            layoutParams.width = getViewWidth(view.getContext(),width);
            layoutParams.height = layoutParams.width;
        }
        if (marginLeft != -1) {
            layoutParams.leftMargin = getViewWidth(view.getContext(),marginLeft);
        }
        if (marginTop != -1) {
            layoutParams.topMargin = getViewHeight(view.getContext(),marginTop);
        }
        if (marginRight != -1) {
            layoutParams.rightMargin = getViewWidth(view.getContext(),marginRight);
        }
        view.setLayoutParams(layoutParams);
    }


    /**
     * 修改分割线颜色
     *
     * @param context
     * @param datePicker
     */
    public static void setDatePickerDividerColor(Context context, DatePicker datePicker) {

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.main_color)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 动态修改tabLayout的宽度 通过反射
     *
     * @param Mtablayout
     * @param context
     * @param marginStart
     * @param marginEnd
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setTabWidth(TabLayout Mtablayout, Context context, float marginStart, float marginEnd)
            throws NoSuchFieldException, IllegalAccessException {
        Class<?> tablayout = Mtablayout.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(Mtablayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LayoutParams params =
                    new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            params.setMarginStart(dp2px(context, marginStart));
            params.setMarginEnd(dp2px(context, marginEnd));
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static int dp2px(Context context, float dipValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
