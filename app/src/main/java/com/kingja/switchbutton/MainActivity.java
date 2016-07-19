package com.kingja.switchbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButton mSwitchbutton = (SwitchButton) findViewById(R.id.switchbutton);
        assert mSwitchbutton != null;
        mSwitchbutton.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitch(boolean isLeft) {
                //do something
            }
        });
    }
}
