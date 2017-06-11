# ColorSlider
Simple color picker library for Android

<img src="https://github.com/naz013/ColorSlider/raw/master/res/screenshow.png" width="400" alt="Screenshot">

Download
--------
Download latest version with Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.naz013:ColorSlider:1.0.1'
}
```

Usage
-----
Default (Material colors picker):
```xml
<com.github.naz013.colorslider.ColorSlider
        android:id="@+id/color_slider"
        android:layout_width="match_parent"
        android:layout_height="36dp" />
```
Gradient (Params: cs_from_color, cs_to_color, cs_steps):
via XML:
```xml
<com.github.naz013.colorslider.ColorSlider
        android:id="@+id/color_slider"
        android:layout_width="match_parent"
        app:cs_from_color="#F44336"
        app:cs_steps="500"
        app:cs_to_color="#40F44336"
        android:layout_height="36dp" />
```
in code:
```java
colorSlider.setGradient(@ColorInt int fromColor, @ColorInt int toColor, int steps)
```


Array of color resources (Params: cs_colors):
via XML:
```xml
<com.github.naz013.colorslider.ColorSlider
        android:id="@+id/color_slider"
        android:layout_width="match_parent"
        app:cs_colors="@array/colors"
        android:layout_height="36dp" />
```
in code:
```java
colorSlider.setColors(@ColorInt int[] colors)
```


String array of hex colors (Params: cs_hex_colors):
via XML:
```xml
<com.github.naz013.colorslider.ColorSlider
        android:id="@+id/color_slider"
        android:layout_width="match_parent"
        app:cs_hex_colors="@array/hex_colors"
        android:layout_height="36dp" />
```
in code:
```java
colorSlider.setHexColors(String[] hexColors)
```

Also you can set listener for color picker:
```java
colorSlider.setListener(new ColorSlider.OnColorSelectedListener() {
        @Override
        public void onColorChanged(int position, int color) {
            updateView(color);
        }
    })
```


License
-------

    Copyright 2017 Nazar Suhovich

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
