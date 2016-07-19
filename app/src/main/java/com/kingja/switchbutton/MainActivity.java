package com.kingja.switchbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButton switchbutton = (SwitchButton) findViewById(R.id.switchbutton);
        assert switchbutton != null;
        switchbutton.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitch(boolean isLeft) {
                Log.e(TAG, "onSwitch: " + isLeft);
            }
        });
    }
}
