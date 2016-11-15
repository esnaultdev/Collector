package net.aohayou.collector.collectiondetail.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.aohayou.collector.R;

import java.util.ArrayList;
import java.util.List;

public class TooltipOverlay extends FrameLayout {

    private List<TooltipInfo> tooltips;

    public TooltipOverlay(Context context) {
        super(context);
        init();
    }

    public TooltipOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TooltipOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        tooltips = new ArrayList<>();
    }

    public void toggleTooltip(@NonNull View target, @NonNull String text) {
        toggleTooltip(target, text, -1);
    }

    public void toggleTooltip(@NonNull View target, @NonNull String text,
                              @ColorInt int color) {
        if (hasActiveTooltip(target)) {
            hide();
        } else {
            add(target, text, color);
        }
    }

    private boolean hasActiveTooltip(@NonNull View target) {
        for (TooltipInfo tooltipInfo : tooltips) {
            if (tooltipInfo.getTarget() == target) {
                return !tooltipInfo.isHiding();
            }
        }
        return false;
    }

    private void add(@NonNull View target, @NonNull String text, @ColorInt int color) {
        // Hide other tooltips
        hide();

        final ElementTooltip tooltip = createTooltip();
        if (color != -1) {
            tooltip.setColor(color);
        }
        TooltipInfo info = new TooltipInfo(tooltip, target);
        tooltips.add(info);

        measureTooltip(tooltip);
        setupTooltipPosition(tooltip, target);
        animateAdd(tooltip);
    }

    @NonNull
    private ElementTooltip createTooltip() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ElementTooltip tooltip =
                (ElementTooltip) inflater.inflate(R.layout.formula_element_tooltip, this, false);
        addView(tooltip);

        // Setup starting attributes
        tooltip.setVisibility(INVISIBLE);
        tooltip.setTranslationZ(-tooltip.getElevation());

        return tooltip;
    }

    private void measureTooltip(@NonNull ElementTooltip tooltip) {
        int widthMeasureSpec = ViewGroup.LayoutParams.WRAP_CONTENT;
        int heightMeasureSpec = ViewGroup.LayoutParams.WRAP_CONTENT;
        tooltip.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setupTooltipPosition(@NonNull ElementTooltip tooltip, @NonNull View target) {
        int tooltipWidth = tooltip.getMeasuredWidth();
        int tooltipHeight = tooltip.getMeasuredHeight();

        // Default direction is top left corner
        boolean left = true;
        boolean top = true;
        int leftPos = target.getLeft();
        int topPos = target.getTop();

        if (target.getLeft() + tooltipWidth >= getRight() - getPaddingRight()) {
            left = false;
            leftPos = target.getRight() - tooltipWidth;
        }
        if (target.getTop() + tooltipHeight >= getBottom() - getPaddingBottom()) {
            top = false;
            topPos = target.getBottom() - tooltipHeight;
        }

        if (left && top) {
            tooltip.setDirection(ElementTooltip.Direction.TOP_LEFT);
        } else if (left){
            tooltip.setDirection(ElementTooltip.Direction.BOTTOM_LEFT);
        } else if (top) {
            tooltip.setDirection(ElementTooltip.Direction.TOP_RIGHT);
        } else {
            tooltip.setDirection(ElementTooltip.Direction.BOTTOM_RIGHT);
        }

        tooltip.setLeft(leftPos);
        tooltip.setTop(topPos);
        tooltip.setRight(leftPos + tooltipWidth);
        tooltip.setBottom(topPos + tooltipHeight);
    }

    private void animateAdd(@NonNull ElementTooltip tooltip) {
        float finalRadius = (float) Math.hypot(tooltip.getWidth(), tooltip.getHeight());
        Point c = tooltip.getPointerRelativePosition();
        Animator anim = ViewAnimationUtils.createCircularReveal(tooltip, c.x, c.y, 0, finalRadius);
        tooltip.setVisibility(VISIBLE);
        anim.setDuration(500).start();

        tooltip.animate().translationZ(0).setDuration(500).start();
    }

    public void hide() {
        for (TooltipInfo tooltipInfo : tooltips) {
            hide(tooltipInfo);
        }
    }

    private void hide(@NonNull TooltipInfo tooltipInfo) {
        if (tooltipInfo.isHiding()) {
            return;
        }
        tooltipInfo.setHiding(true);

        animateHide(tooltipInfo);
    }

    private void animateHide(@NonNull final TooltipInfo tooltipInfo) {
        ElementTooltip tooltip = tooltipInfo.tooltip;

        // Circular reveal
        float finalRadius = (float) Math.hypot(tooltip.getWidth(), tooltip.getHeight());
        Point c = tooltip.getPointerRelativePosition();
        Animator anim = ViewAnimationUtils.createCircularReveal(tooltip, c.x, c.y, finalRadius, 0);
        anim.setDuration(500).start();
        anim.addListener(new DefaultAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                remove(tooltipInfo);
            }
        });

        // Animate elevation
        tooltip.animate().translationZ(-tooltip.getElevation()).setDuration(500).start();
    }

    private void remove(@NonNull TooltipInfo tooltipInfo) {
        tooltipInfo.tooltip.setVisibility(INVISIBLE);
        tooltips.remove(tooltipInfo);
        removeView(tooltipInfo.tooltip);
    }

    public void translate(int dx, int dy) {
        for (TooltipInfo tooltipInfo : tooltips) {
            translateTooltip(tooltipInfo.tooltip, dx, dy);
        }
    }

    private static void translateTooltip(@NonNull ElementTooltip tooltip, int dx, int dy) {
        tooltip.setLeft(tooltip.getLeft() - dx);
        tooltip.setRight(tooltip.getRight() - dx);
        tooltip.setTop(tooltip.getTop() - dy);
        tooltip.setBottom(tooltip.getBottom() - dy);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // Do nothing as tooltips are positioned manually
    }


    private static class TooltipInfo {
        private ElementTooltip tooltip;
        private boolean hiding;
        private View target;

        public TooltipInfo(@NonNull ElementTooltip tooltip, @NonNull View target) {
            this.tooltip = tooltip;
            hiding = false;
            this.target = target;
        }

        public void setHiding(boolean hiding) {
            this.hiding = hiding;
        }

        public boolean isHiding() {
            return hiding;
        }

        @NonNull
        public View getTarget() {
            return target;
        }
    }


    private class DefaultAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
