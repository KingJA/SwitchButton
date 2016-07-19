# SwitchButton
Check this [article on our blog](https://king.github.io)
## Custom attribute
## Usage
step 1
```java
<com.kingja.switchbutton.SwitchButton
        android:layout_width="match_parent"
        android:id="@+id/switchbutton"
        android:layout_height="30dp"
        app:left_text="天猫"
        app:right_text="京东"
        app:stroke_radius="5dp"
        app:text_size="16sp"
        app:selected_color="@color/red"
        app:selected="right" />
```

step 2
```java
SwitchButton mSwitchbutton = (SwitchButton) findViewById(R.id.switchbutton);
        assert mSwitchbutton != null;
        mSwitchbutton.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitch(boolean isLeft) {
                //do something
            }
        });
```
## Contact me
* email:kingjavip@gmail.com
* QQ:87049319
* Weixin:darabbbit

## License

    Copyright 2016 KingJA

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
