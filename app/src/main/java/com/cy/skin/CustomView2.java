package com.cy.skin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.mylibrary.SkinManager;
import com.cy.mylibrary.SkinViewSupport;
import com.cy.mylibrary.utils.SkinResources;

/**
 * Created by chuyang on 2021/3/3.
 */
public class CustomView2 extends View implements SkinViewSupport {

    private int bg_color;
    private int bg_color_id;

    public CustomView2(Context context) {
        this(context, null);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.e("aaa","CustomView2 AttributeName : "+attrs.getAttributeName(i)+" AttributeValue : "+attrs.getAttributeValue(i));
        }
       String attributeValue=attrs.getAttributeValue(6);
        String substring = attributeValue.substring(1);
        bg_color_id = Integer.parseInt(substring);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView2);
        bg_color = typedArray.getInt(R.styleable.CustomView2_my_bg, 0);
        typedArray.recycle(); // 因为源码中是进行回收的
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(bg_color);
    }

    @Override
    public void applySkin() {
        bg_color = SkinResources.getInstance().getColor(bg_color_id);
        setBackgroundColor(bg_color);
    }
}
