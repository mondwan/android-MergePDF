package com.practical_developer.mergepdf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Test cases on main activity
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
        new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.practical_developer.mergepdf", appContext.getPackageName());
    }

    @Test
    public void switchToMergeSetting() throws Exception {
        // Click the floating button in order to switch to merge setting
        onView(withId(R.id.startMerging)).perform(click());

        // Verify the title in the new activity
        String toolBarTitle =
            InstrumentationRegistry.getTargetContext().getString(
                R.string.merge_setting
            );
        onView(
            allOf(
                instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))
            )
        ).check(matches(withText(toolBarTitle)));
    }
}
