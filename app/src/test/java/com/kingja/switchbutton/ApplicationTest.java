package com.kingja.switchbutton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import lib.kingja.switchbutton.SwitchMultiButton;

import static org.junit.Assert.assertArrayEquals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ApplicationTest {
    @Test
    public void testGetSelectedButtons() {
        MainActivity a = Robolectric.setupActivity(MainActivity.class);
        SwitchMultiButton smb = a.getSwitchMultiButton();
        Integer in[] = new Integer[]{0, 2};
        StringBuilder sb = new StringBuilder();
        sb.append("in: ");

        for (Integer i : in) {
            smb.setSelectedTab(i);
            sb.append(i).append(" ");
        }

        sb.append("out: ");

        Integer out[] = smb.getSelectedTabs();

        for (Integer i : out) {
            sb.append(i).append(" ");
        }

        assertArrayEquals(sb.toString(), in, out);
    }

    @Test
    public void testSelectAndUnselectButtons() {

    }
}