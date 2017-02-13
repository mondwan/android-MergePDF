package com.practical_developer.mergepdf;

import android.content.ClipData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Test cases on MergeSettingActivity
 */
@RunWith(AndroidJUnit4.class)
public class MergeSettingActivityTest {
    // Navigation from main activity is required for this activity
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
        new ActivityTestRule<>(MainActivity.class);

    @Before
    public void switchToMergeSetting() throws Exception {
        // Click the floating button in order to switch to merge setting
        onView(withId(R.id.createPDF)).perform(click());
    }

    @Test
    public void getDefaultFileName() throws Exception {
        // TODO
    }

    @Test
    public void testEmptyFileListByDefault() throws Exception {
        onView(withId(R.id.empty_view)).check(
            matches(withText(R.string.empty_pdf_list))
        );
    }
}