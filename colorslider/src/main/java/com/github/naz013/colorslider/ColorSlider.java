package com.github.naz013.colorslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.naz013.colorslider.R;

/**
 * Copyright 2017 Nazar Suhovich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ColorSlider extends View {

    private int[] mColors = new int[]{};
    private Rect[] mColorRects = new Rect[]{};
    private Rect[] mColorFullRects = new Rect[]{};
    private Paint mPaint;
    private Paint mSelectorPaint;
    private int mSelectedItem;
    private OnColorSelectedListener mListener;

    public ColorSlider(Context context) {
        super(context);
        init(context, null);
    }

    public ColorSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setHexColors(String[] hexColors) {
        if (hexColors != null && hexColors.length > 0) {
            this.convertToColors(hexColors);
            this.calculateRectangles();
            this.invalidate();
        }
    }

    public void setColor(@ColorInt int[] colors) {
        if (colors != null && colors.length > 0) {
            this.mColors = colors;
            this.calculateRectangles();
            this.invalidate();
        }
    }

    public void setGradient(@ColorInt int fromColor, @ColorInt int toColor, int steps) {
        if (fromColor != 0 && toColor != 0 && steps != 0) {
            this.calculateColors(fromColor, toColor, steps);
            this.calculateRectangles();
            this.invalidate();
        }
    }

    public void selectColor(@ColorInt int color) {
        for (int i = 0; i < this.mColors.length; i++) {
            if (this.mColors[i] == color) {
                this.mSelectedItem = i;
                this.invalidate();
                break;
            }
        }
    }

    public void setSelection(int position) {
        if (position >= mColors.length) {
            return;
        }
        this.mSelectedItem = position;
        this.invalidate();
    }

    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    @ColorInt
    public int getSelectedColor() {
        return this.mColors[this.mSelectedItem];
    }

    public void setListener(OnColorSelectedListener listener) {
        this.mListener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.mSelectorPaint = new Paint();
        this.mSelectorPaint.setStyle(Paint.Style.STROKE);
        this.mSelectorPaint.setColor(getResources().getColor(android.R.color.background_dark));
        this.mSelectorPaint.setStrokeWidth(2f);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return processTouch(event);
            }
        });

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorSlider, 0, 0);
            try {
                int id = a.getResourceId(R.styleable.ColorSlider_cs_colors, 0);
                int hexId = a.getResourceId(R.styleable.ColorSlider_cs_hex_colors, 0);
                if (id != 0) {
                    int[] ids = getResources().getIntArray(id);
                    if (ids.length > 0) {
                        this.mColors = new int[ids.length];
                        System.arraycopy(ids, 0, this.mColors, 0, ids.length);
                    }
                } else if (hexId != 0) {
                    String[] hex = getResources().getStringArray(hexId);
                    if (hex.length > 0) {
                        convertToColors(hex);
                    }
                }
                if (this.mColors.length == 0) {
                    int fromColor = a.getColor(R.styleable.ColorSlider_cs_from_color, 0);
                    int toColor = a.getColor(R.styleable.ColorSlider_cs_to_color, 0);
                    int steps = a.getInt(R.styleable.ColorSlider_cs_steps, 21);
                    if (fromColor != 0 && toColor != 0 && steps != 0) {
                        calculateColors(fromColor, toColor, steps);
                    }
                }
            } catch (Exception e) {
                Log.d("ColorSlider", "init: " + e.getLocalizedMessage());
            } finally {
                a.recycle();
            }
        }
        if (this.mColors.length == 0) {
            initDefaultColors();
        }
        this.mColorRects = new Rect[this.mColors.length];
        this.mColorFullRects = new Rect[this.mColors.length];
    }

    private void initDefaultColors() {
        this.mColors = new int[]{
                Color.parseColor("#F44336"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#9C27B0"),
                Color.parseColor("#673AB7"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#03A9F4"),
                Color.parseColor("#00BCD4"),
                Color.parseColor("#009688"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#8BC34A"),
                Color.parseColor("#CDDC39"),
                Color.parseColor("#FFEB3B"),
                Color.parseColor("#FFC107"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#FF5722"),
                Color.parseColor("#795548"),
                Color.parseColor("#9E9E9E"),
                Color.parseColor("#607D8B"),
                Color.parseColor("#FFFFFF")
        };
    }

    private void convertToColors(String[] hex) {
        this.mColors = new int[hex.length];
        for (int i = 0; i < hex.length; i++) {
            this.mColors[i] = Color.parseColor(hex[i]);
        }
    }

    private void calculateColors(int fromColor, int toColor, int steps) {
        float a1 = (float) Color.alpha(fromColor);
        float r1 = (float) Color.red(fromColor);
        float g1 = (float) Color.green(fromColor);
        float b1 = (float) Color.blue(fromColor);

        float a2 = (float) Color.alpha(toColor);
        float r2 = (float) Color.red(toColor);
        float g2 = (float) Color.green(toColor);
        float b2 = (float) Color.blue(toColor);

        float alphaStep = (a2 - a1) / (float) steps;
        float redStep = (r2 - r1) / (float) steps;
        float greenStep = (g2 - g1) / (float) steps;
        float blueStep = (b2 - b1) / (float) steps;

        this.mColors = new int[steps];
        for (int i = 0; i < steps; i++) {
            this.mColors[i] = Color.argb((int) (a1 + alphaStep * i), (int) (r1 + redStep * i),
                    (int) (g1 + greenStep * i), (int) (b1 + blueStep * i));
        }
    }

    private boolean processTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
        else if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
            updateView(event.getX(), event.getY());
            return true;
        }
        return false;
    }

    private void updateView(float x, float y) {
        for (int i = 0; i < this.mColorFullRects.length; i++) {
            Rect rect = this.mColorFullRects[i];
            if (rect != null) {
                if (rect.contains((int) x, (int) y) && i != this.mSelectedItem) {
                    this.mSelectedItem = i;
                    notifyChanged();
                    invalidate();
                    break;
                }
            }
        }
    }

    private void notifyChanged() {
        if (this.mListener != null) {
            this.mListener.onColorChanged(this.mSelectedItem, this.mColors[this.mSelectedItem]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mColorRects.length > 0) {
            drawSlider(canvas);
        }
    }

    private void drawSlider(Canvas canvas) {
        for (int i = 0; i < this.mColorRects.length; i++) {
            this.mPaint.setColor(this.mColors[i]);
            if (i == this.mSelectedItem) {
                canvas.drawRect(this.mColorFullRects[i], this.mPaint);
                canvas.drawRect(this.mColorFullRects[i], this.mSelectorPaint);
            } else {
                canvas.drawRect(this.mColorRects[i], this.mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        calculateRectangles();
    }

    private void calculateRectangles() {
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        float itemWidth = width / (float) this.mColors.length;
        float margin = height * 0.1f;
        for (int i = 0; i < this.mColors.length; i++) {
            this.mColorRects[i] = new Rect((int) (itemWidth * i), (int) margin, (int) (itemWidth * (i + 1)), (int) (height - margin));
            this.mColorFullRects[i] = new Rect((int) (itemWidth * i), 0, (int) (itemWidth * (i + 1)), (int) height);
        }
    }

    public interface OnColorSelectedListener {
        void onColorChanged(int position, @ColorInt int color);
    }
}
