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
| selectedTab | integer     | app:selectedTab="1" |

![](https://github.com/KingJA/SwitchButton/blob/master/img/mark.png)
## Gradle
```java
 compile 'lib.kingja.switchbutton:switchbutton:1.1.2'
```

## Usage
### step 1
```java
<lib.kingja.switchbutton.SwitchMultiButton
        android:id="@+id/switchmultibutton"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        app:strokeRadius="5dp"
        app:strokeWidth="1dp"
        app:selectedTab="0"
        app:selectedColor="#eb7b00"
        app:textSize="14sp" />
```

### step 2
```java
SwitchMultiButton mSwitchMultiButton = (SwitchMultiButton) findViewById(R.id.switchmultibutton);
        assert mSwitchMultiButton != null;
        mSwitchMultiButton.setText(Arrays.asList("点个Star", "狠心拒绝")).setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Toast.makeText(MainActivity.this, tabText, Toast.LENGTH_SHORT).show();
            }
        });
```
## Changelog
**v1.1.2**
- Fix onRestoreInstanceState

**v1.1.1**
- Initial release 

## Contact me
Any questions,Welcome to contact me.
* email:kingjavip@gmail.com
* QQ:87049319
* Weixin:darabbbit
* [My blog](https://kingja.github.io)

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
