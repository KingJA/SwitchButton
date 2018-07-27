/*
 *  Copyright (C) 2016 KingJA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.kingja.switchbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.lib.kingja.switchbutton.R;

import lib.kingja.switchbutton.SwitchMultiButton;

import static com.lib.kingja.switchbutton.R.id.switchmultibutton3;

/**
 * Description：A smart switchable button,support multiple tabs.
 * Create Time：2016/7/27 10:26
 * Author:KingJA
 * Email:kingjavip@gmail.com
 * update:add into Jenkins 16:23
 */
public class MainActivity extends AppCompatActivity {

    private String[] tabTexts1 = {"test1", "test2", "test3", "test4"};
    private String[] tabTexts4 = {"已经", "在家", "等你"};
    private SwitchMultiButton smb;
    private boolean showToast = false;

    private SwitchMultiButton.OnSwitchListener onSwitchListener = new SwitchMultiButton.OnSwitchListener() {
        @Override
        public void onSwitch(int position, String tabText) {
            //Toast.makeText(MainActivity.this, tabText, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smb = ((SwitchMultiButton) findViewById(R.id.switchmultibutton1));
        smb.setText(tabTexts1).setOnSwitchListener(onSwitchListener);

        Integer in[] = new Integer[]{0, 2};

        for (Integer i : in) {
            smb.setSelectedTab(i);
        }

        ((SwitchMultiButton) findViewById(R.id.switchmultibutton2)).setText("点个Star", "狠心拒绝").setOnSwitchListener(onSwitchListener);
        ((SwitchMultiButton) findViewById(switchmultibutton3)).setOnSwitchListener(onSwitchListener).setSelectedTab(1);
        ((SwitchMultiButton) findViewById(R.id.switchmultibutton4)).setText(tabTexts4).setOnSwitchListener(onSwitchListener);

        Button unselect = (Button) findViewById(R.id.button);
        Button select = (Button) findViewById(R.id.button2);
        Switch sw = (Switch) findViewById(R.id.switch1);

        unselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smb.clearSelected();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < tabTexts1.length; i++) {
                            final int ii[] = new int[1];
                            ii[0] = i;
                            smb.setSelectedTab(ii[0]);
                        }
                    }
                };

                new Thread(r).run();
            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showToast = isChecked;
            }
        });

        smb.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                if (showToast)
                    Toast.makeText(getBaseContext(), tabText + " pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public SwitchMultiButton getSwitchMultiButton() {
        return smb;
    }
}
