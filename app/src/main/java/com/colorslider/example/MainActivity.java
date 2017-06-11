package com.colorslider.example;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.naz013.colorslider.ColorSlider;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ColorSlider.OnColorSelectedListener mListener = new ColorSlider.OnColorSelectedListener() {
        @Override
        public void onColorChanged(int position, int color) {
            updateView(color);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ColorSlider slider = (ColorSlider) findViewById(R.id.color_slider);
        slider.setListener(mListener);

        ColorSlider sliderGradient = (ColorSlider) findViewById(R.id.color_slider_gradient);
        sliderGradient.setListener(mListener);

        ColorSlider sliderArray = (ColorSlider) findViewById(R.id.color_slider_array);
        sliderArray.setListener(mListener);

        ColorSlider sliderHexArray = (ColorSlider) findViewById(R.id.color_slider_colors);
        sliderHexArray.setListener(mListener);

        updateView(slider.getSelectedColor());
    }

    private void updateView(@ColorInt int color) {
        toolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
}
