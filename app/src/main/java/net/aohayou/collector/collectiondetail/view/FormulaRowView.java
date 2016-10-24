package net.aohayou.collector.collectiondetail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import net.aohayou.collector.R;

public class FormulaRowView extends LinearLayout {

    private RowInfo rowInfo;

    public FormulaRowView(Context context) {
        super(context);
    }

    public FormulaRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormulaRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormulaRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setRowInfo(@NonNull RowInfo info) {
        int oldDisplayCount = (rowInfo != null) ? rowInfo.displayCount : 0;
        setDisplayCount(oldDisplayCount, info.displayCount);
        setAcquired(info.acquired);
        rowInfo = info;
    }

    private void setDisplayCount(int oldDisplayCount, int newDisplayCount) {
        if (oldDisplayCount < newDisplayCount) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = oldDisplayCount; i < newDisplayCount; i++) {
                addView(inflater.inflate(R.layout.formula_row_element_view, this, false));
            }
        } else if (oldDisplayCount > newDisplayCount) {
            removeViews(oldDisplayCount, oldDisplayCount - newDisplayCount);
        }
    }

    private void setAcquired(boolean[] acquired) {
        for (int i = 0; i < acquired.length; i++) {
            ((FormulaElementView) getChildAt(i)).setAcquired(acquired[i]);
        }
    }


    public static class RowInfo {
        public final int elementCount;
        public final int displayCount;
        public final boolean[] acquired;

        public RowInfo(int elementCount, int displayCount, boolean[] acquired) {
            this.elementCount = elementCount;
            this.displayCount = displayCount;
            this.acquired = acquired;
        }

        public RowInfo(int elementCount, boolean[] acquired) {
            this(elementCount, elementCount, acquired);
        }
    }
}
