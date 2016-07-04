package com.app.sunshine.socratesdiaz.sunshine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by socratesdiaz on 7/4/16.
 */
public class MyView extends View {

    private ShapeDrawable mDrawable;

    public MyView(Context context) {
        super(context);

        int x = 0;
        int y = 0;
        int width = 100;
        int height = 100;

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0x00000000);
        mDrawable.setBounds(x, y, x+width, y+height);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}
