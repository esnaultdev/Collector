package net.aohayou.collector.collectiondetail.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import net.aohayou.collector.R;

public class TooltipOverlay extends RelativeLayout {

    private ElementTooltip tooltip;
    private boolean hidding;

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

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        tooltip = (ElementTooltip) inflater.inflate(R.layout.formula_element_tooltip, this, false);
        addView(tooltip);

        tooltip.setVisibility(INVISIBLE);
        tooltip.setTranslationZ(-tooltip.getElevation());
    }

    public void displayTooltip(@NonNull View view, @NonNull String text) {
        hidding = false;

        int tooltipWidth = tooltip.getMeasuredWidth();
        int tooltipHeight = tooltip.getMeasuredHeight();

        //Default direction is top left corner
        boolean left = true;
        boolean top = true;
        int leftPos = view.getLeft();
        int topPos = view.getTop();

        if (view.getLeft() + tooltipWidth >= getRight() - getPaddingRight()) {
            left = false;
            leftPos = view.getRight() - tooltipWidth;
        }
        if (view.getTop() + tooltipHeight >= getBottom() - getPaddingBottom()) {
            top = false;
            topPos = view.getBottom() - tooltipHeight;
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

        float finalRadius = (float) Math.hypot(tooltip.getWidth(), tooltip.getHeight());
        Point point = getPointerRelativePosition();
        Animator anim = ViewAnimationUtils.createCircularReveal(
                tooltip, point.x, point.y, 0, finalRadius);
        tooltip.setVisibility(VISIBLE);
        anim.setDuration(500).start();

        tooltip.animate().translationZ(0).setDuration(500).start();
    }

    public Point getPointerRelativePosition() {
        switch (tooltip.getDirection()) {
            case ElementTooltip.Direction.TOP_LEFT:
            default:
                return new Point(0, 0);
            case ElementTooltip.Direction.TOP_RIGHT:
                return new Point(tooltip.getMeasuredWidth(), 0);
            case ElementTooltip.Direction.BOTTOM_RIGHT:
                return new Point(tooltip.getMeasuredWidth(), tooltip.getMeasuredHeight());
            case ElementTooltip.Direction.BOTTOM_LEFT:
                return new Point(0, tooltip.getMeasuredHeight());
        }
    }

    public void hideTooltip() {
        if (hidding) {
            return;
        }
        hidding = true;
        float finalRadius = (float) Math.hypot(tooltip.getWidth(), tooltip.getHeight());
        Point point = getPointerRelativePosition();
        Animator anim = ViewAnimationUtils.createCircularReveal(
                tooltip, point.x, point.y, finalRadius, 0);
        anim.setDuration(500).start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tooltip.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        tooltip.animate().translationZ(-tooltip.getElevation()).setDuration(500).start();
    }

    public void translateOverlay(int dx, int dy) {
        tooltip.setLeft(tooltip.getLeft() - dx);
        tooltip.setRight(tooltip.getRight() - dx);
        tooltip.setTop(tooltip.getTop() - dy);
        tooltip.setBottom(tooltip.getBottom() - dy);
    }
}
