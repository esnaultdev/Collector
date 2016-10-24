package net.aohayou.collector.collectiondetail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;


public class FormulaElementView extends View {
    private static Paint colorPaint;
    private boolean acquired;

    public FormulaElementView(Context context) {
        super(context);
        init();
    }

    public FormulaElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FormulaElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormulaElementView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (colorPaint == null) {
            int colorResource = blue.aodev.materialspecs.R.color.material_color_green_primary;
            colorPaint = new Paint();
            colorPaint.setColor(getContext().getResources().getColor(colorResource));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (acquired) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), colorPaint);
        }
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
        invalidate();
    }
}
