package com.practical_developer.mergepdf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;
import android.util.Log;

import com.practical_developer.mergepdf.file.FileItem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;

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
        onView(withId(R.id.create_pdf)).perform(click());
    }

    @Test
    public void getDefaultFileName() throws Exception {
        // TODO
    }

    private static void writeBytesToFile(
        InputStream is,
        File file
    ) throws IOException{
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int byteRead;

            fos = new FileOutputStream(file);

            while((byteRead=is.read(data)) > -1){
                fos.write(data, 0, byteRead);
            }
        }
        finally{
            if (fos!=null){
                fos.close();
            }
        }
    }

    private Instrumentation.ActivityResult pickPDF(
        String f
    ) throws IOException {
        // Short hand for accessing different context
        Context ourContext = InstrumentationRegistry.getContext();
        // Context targetContext = InstrumentationRegistry.getTargetContext();

        // Convert Asset to File by copying such file to our cache directory
        InputStream input = ourContext.getResources().getAssets().open(f);
        File f2 = new File(ourContext.getExternalCacheDir() +"/" + f);
        writeBytesToFile(input, f2);

        // Get an uri from such file object
        Intent i = new Intent();
        i.setData(Uri.fromFile(f2));

        return new Instrumentation.ActivityResult(Activity.RESULT_OK, i);
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
        onView(withId(R.id.add_source)).perform(click());

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

    @Test
    public void addPDFSource() throws Exception {
        // Stub Intent for GET_CONTENT by stubbing ACTION_CHOOSER for 2 reasons
        // 1. I cannot mock out Intent GET_CONTENT from ACTION_CHOOSER
        // 2. It is unnecessary to verify behaviour on choosing file browser
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(
            pickPDF(PDF_ONE)
        );

        // Click the add file source button
        onView(withId(R.id.add_source)).perform(click());

        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(
            pickPDF(PDF_TWO)
        );

        // Click the add file source button
        onView(withId(R.id.add_source)).perform(click());

        // Verify the contents
        onView(withText(PDF_ONE)).check(matches(isDisplayed()));
        onView(withText(PDF_TWO)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyFileListByDefault() throws Exception {
        onView(withId(R.id.empty_view)).check(
            matches(withText(R.string.empty_pdf_list))
        );
    }
}