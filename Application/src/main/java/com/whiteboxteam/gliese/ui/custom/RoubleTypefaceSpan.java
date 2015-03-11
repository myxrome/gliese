package com.whiteboxteam.gliese.ui.custom;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 12.03.2015
 * Time: 0:55
 */
public class RoubleTypefaceSpan extends MetricAffectingSpan {

    private final Typeface typeface;

    public RoubleTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        apply(p);
    }

    private void apply(Paint paint) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~typeface.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(typeface);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        apply(tp);
    }

}
