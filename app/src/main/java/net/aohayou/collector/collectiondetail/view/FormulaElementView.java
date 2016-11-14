package net.aohayou.collector.collectiondetail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import net.aohayou.collector.R;
import net.aohayou.collector.util.ColorUtil;


public class FormulaElementView extends View {
    private static Paint acquiredPaint;
    private static Paint missingPaint;
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
        if (acquiredPaint == null) {
            @ColorRes int colorRes = R.color.acquired_element;
            acquiredPaint = new Paint();
            acquiredPaint.setColor(ColorUtil.getColor(getContext(), colorRes));
        }
        if (missingPaint == null) {
            @ColorRes int colorRes = R.color.missing_element;
            missingPaint = new Paint();
            missingPaint.setColor(ColorUtil.getColor(getContext(), colorRes));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = acquired ? acquiredPaint : missingPaint;
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
        invalidate();
    }

    public boolean isAcquired() {
        return acquired;
    }
}
