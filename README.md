# SwitchButton
A smart switchable button,support multiple tabs. CLICK THE ***STAR***  if it's useful for you.

## Preview
![](https://github.com/KingJA/SwitchButton/blob/master/img/usage.gif)
## Custom attribute
| attribute | format | example  |
| :------------- |:-------------| :-----|
| strokeRadius | dimension      | app:strokeRadius="5dp" |
| strokeWidth | dimension      | app:strokeWidth="2dp" |
| textSize | dimension      | app:textSize="16sp" |
| selectedColor | color/reference     | app:selectedColor="@color/red" |
| disableColor | color/reference     | app:selectedColor="@color/gray" |
| selectedTab | integer     | app:selectedTab="1" |
| switchTabs | reference     | app:switchTabs="@array/switch_tabs" |
| typeface | string     | app:typeface="DeVinneTxtBT.ttf" |

![](https://github.com/KingJA/SwitchButton/blob/master/img/mark.png)
## Gradle
```xml
 compile 'lib.kingja.switchbutton:switchbutton:1.1.8'
```

## Usage
### step 1
```xml
<lib.kingja.switchbutton.SwitchMultiButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="8dp"
        app:strokeRadius="5dp"
        app:strokeWidth="1dp"
        app:selectedTab="0"
        app:selectedColor="#eb7b00"
        app:switchTabs="@array/switch_tabs"
        app:typeface="DeVinneTxtBT.ttf"
        app:textSize="14sp" />
```

### step 2
```java
//set switch tabs with ***app:switchTabs*** in xml 

SwitchMultiButton mSwitchMultiButton = (SwitchMultiButton) findViewById(R.id.switchmultibutton);
        mSwitchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Toast.makeText(MainActivity.this, tabText, Toast.LENGTH_SHORT).show();
            }
        });
        
//or set switch tabs in java code

SwitchMultiButton mSwitchMultiButton = (SwitchMultiButton) findViewById(R.id.switchmultibutton);
        mSwitchMultiButton.setText("点个Star", "狠心拒绝").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Toast.makeText(MainActivity.this, tabText, Toast.LENGTH_SHORT).show();
            }
        });
```
## [Changelog](ChangeLogs.md)


## Contact me
Any questions:Welcome to contact me.
* Email:kingjavip@gmail.com

## License

    Copyright 2017 KingJA

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
