package com.practical_developer.mergepdf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ClipData;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasCategories;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Test cases on MergeSettingActivity
 */
@RunWith(AndroidJUnit4.class)
public class MergeSettingActivityTest {

    String PDF_ONE = "one.pdf";
    String PDF_TWO = "two.pdf";

    // Navigation from main activity is required for this activity
    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule =
        new IntentsTestRule<>(MainActivity.class);

    @Before
    public void switchToMergeSetting() throws Exception {
        // Click the floating button in order to switch to merge setting
        onView(withId(R.id.createPDF)).perform(click());
    }

    @Test
    public void getDefaultFileName() throws Exception {
        // TODO
    }

    private Instrumentation.ActivityResult pickPDF(String f) {
        return null;
    }

    @Test
    public void showFileDialog() throws Exception {
        // By default Espresso Intents does not stub any Intents. Stubbing
        // needs to be setup before every test run. In this case all external
        // Intents will be blocked.
        intending(not(isInternal())).respondWith(
            new Instrumentation.ActivityResult(
                Activity.RESULT_CANCELED,
                null
            )
        );

        // Click the add file source button
        onView(withId(R.id.addSource)).perform(click());

        // Verify there is intent for selecting a file browser
        // and there is an intent for select a pdf
        intended(allOf(
            hasAction(Intent.ACTION_CHOOSER),
            hasExtra(
                is(Intent.EXTRA_INTENT),
                allOf(
                    hasAction(Intent.ACTION_GET_CONTENT),
                    hasType("application/pdf"),
                    hasCategories(hasItem(equalTo(Intent.CATEGORY_OPENABLE)))
                )
            ),
            hasExtra(Intent.EXTRA_TITLE, "Select a PDF")
        ));
    }

//    @Test
//    public void addPDFSource() throws Exception {
//        // @TODO
//        // Stub Intent for GET_CONTENT
//        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(
//            pickPDF(PDF_ONE)
//        );
//
//        // Click add file
//        // Verify the contents
//    }

    @Test
    public void testEmptyFileListByDefault() throws Exception {
        onView(withId(R.id.empty_view)).check(
            matches(withText(R.string.empty_pdf_list))
        );
    }
}