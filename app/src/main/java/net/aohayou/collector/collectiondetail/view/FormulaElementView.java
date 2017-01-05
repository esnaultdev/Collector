package net.aohayou.collector.collectiondetail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import net.aohayou.collector.R;


public class FormulaElementView extends View {
    private static Paint acquiredPaint;
    private static Paint missingPaint;
    private static Paint textPaint;

    private boolean acquired;
    private int number;
    private Rect numberBounds;

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
            acquiredPaint.setColor(ContextCompat.getColor(getContext(), colorRes));
        }
        if (missingPaint == null) {
            @ColorRes int colorRes = R.color.missing_element;
            missingPaint = new Paint();
            missingPaint.setColor(ContextCompat.getColor(getContext(), colorRes));
        }
        if (textPaint == null) {
            @ColorRes int colorRes = R.color.element_text_color;
            textPaint = new Paint();
            textPaint.setColor(ContextCompat.getColor(getContext(), colorRes));
            float textSize = getResources().getDimension(R.dimen.formula_element_text_size);
            textPaint.setTextSize(textSize);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        numberBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = acquired ? acquiredPaint : missingPaint;
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        String numberText = getFormattedNumber();
        textPaint.getTextBounds(numberText, 0, numberText.length(), numberBounds);
        canvas.drawText(numberText, canvas.getWidth()/2,
                canvas.getHeight()/2 - numberBounds.exactCenterY(), textPaint);
    }

    @NonNull
    private String getFormattedNumber() {
        if (number < 10000) {
            return String.valueOf(number);
        } else {
            return "...";
        }
    }

    /**
     * @return a two digits String of a number.
     * @param number the number to convert. Must be between 0 and 99.
     */
    @NonNull
    private String getTwoDigitsString(int number) {
        String result = String.valueOf(number);
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
        invalidate();
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
