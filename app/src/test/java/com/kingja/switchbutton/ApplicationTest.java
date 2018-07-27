package com.kingja.switchbutton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import lib.kingja.switchbutton.SwitchMultiButton;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ApplicationTest {

    private MainActivity a;
    private SwitchMultiButton smb;

    @Before
    public void setUp() {
        a = Robolectric.setupActivity(MainActivity.class);
        smb = a.getSwitchMultiButton();
    }

    @Test
    public void testGetSelectedButtons() {
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
        Integer in[] = new Integer[]{0, 1, 2, 3};

        for (Integer i : in) {
            smb.setSelectedTab(i);
        }

        Integer out[] = smb.getSelectedTabs();

        assertArrayEquals(in, out);

        smb.clearSelected();

        assertArrayEquals(smb.getSelectedTabs(), new Integer[]{});

        for (Integer i : in) {
            if (i % 2 == 1)
                smb.setSelectedTab(i);
        }

        assertArrayEquals(smb.getSelectedTabs(), new Integer[]{1, 3});
    }

    @Test
    public void testGetButtonStatus() {
        smb.clearSelected();

        Integer in[] = new Integer[]{0, 3};
        for (Integer i : in) {
            smb.setSelectedTab(i);
        }
        assertTrue(smb.getState(0));
        assertTrue(smb.getState(3));
        assertFalse(smb.getState(1));
        assertFalse(smb.getState(2));

        smb.clearSelected();

        assertFalse(smb.getState(0));
        assertFalse(smb.getState(3));

        smb.setSelectedTab(1);
        smb.setSelectedTab(2);

        assertTrue(smb.getState(1));
        assertTrue(smb.getState(2));
    }
}