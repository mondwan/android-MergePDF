package com.practical_developer.mergepdf;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.practical_developer.mergepdf.file.FileItem;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MergeSettingActivity extends AppCompatActivity
        implements FileListFragment.FileListFragmentCallbacks {

    private ArrayList<Pair<Long, FileItem>> mFileListSource;
    private static final String MERGE_SETTING_TAG = "MergeSetting";

    /**
         *  Holds name of the output PDF
         */
    private EditText mEditText;

    public ArrayList<Pair<Long, FileItem>> getFileListSource() {
        return mFileListSource;
    }

    public boolean onDeleteFileItem(int pos) {
        boolean ret = true;

        try {
            mFileListSource.remove(pos);
        } catch (IndexOutOfBoundsException e) {
            ret = false;
        }

        return ret;
    }

    public int onAddFileItem(FileItem item) {
        Long id = (long) 0;
        boolean hasFound = true;

        // Loop from 0 until there is a single ID does not used
        while (hasFound) {
            hasFound = false;
            for (Pair<Long, FileItem> p : mFileListSource) {
                Long _id = p.first;
                if (_id.longValue() == id.longValue()) {
                    id++;
                    hasFound = true;
                    break;
                }
            }
        }

        int index = mFileListSource.size();

        Log.d(
            MERGE_SETTING_TAG,
            String.format(
                "File |%s| type |%s| with id |%d| at |%d|",
                item.getFileName(),
                item.getFileType(),
                id,
                index
            )
        );

        mFileListSource.add(new Pair<>(id, item));

        return index;
    }

    private void mergePDF() {
        String fileName = mEditText.getText().toString();
        Log.d(
            MERGE_SETTING_TAG,
            String.format(
                "Start merging PDF which named as |%s|",
                fileName
            )
        );
        ArrayList<PDDocument> pdfs = new ArrayList<>();

        if (fileName.length() == 0) {
            Toast.makeText(
                getApplicationContext(),
                "File name should not be empty",
                Toast.LENGTH_SHORT
            ).show();
        } else {
            for (Pair<Long, FileItem> p : mFileListSource) {
                FileItem i = p.second;
                Uri uri = i.getUri();
                String path = uri.getPath();
                Log.d(
                    MERGE_SETTING_TAG,
                    String.format("Read file at |%s|", path)
                );
                try {
                    File f = new File(new File(path).getAbsolutePath());
                    pdfs.add(PDDocument.load(f));
                } catch (IOException e) {
                    String err = String.format(
                        "Unable to read |%s|",
                        i.getFileName()
                    );
                    Toast.makeText(
                        getApplicationContext(),
                        err,
                        Toast.LENGTH_SHORT
                    ).show();
                    Log.e(MERGE_SETTING_TAG, err);
                    Log.e(MERGE_SETTING_TAG, e.getMessage());
                }
            }

            if (pdfs.size() > 0) {
                File outputDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ),
                    getApplicationContext().getString(R.string.app_name)
                );

                // Make sure we have create a directory for our PDF
                //noinspection ResultOfMethodCallIgnored
                outputDir.mkdirs();

                File outputPath = new File(
                    outputDir,
                    String.format("%s.pdf", fileName)
                );
                try {
                    PDDocument outPDF = new PDDocument();

                    PDFMergerUtility merger = new PDFMergerUtility();
                    for (PDDocument d : pdfs) {
                        merger.appendDocument(outPDF, d);
                    }
                    merger.mergeDocuments();

                    outPDF.save(outputPath);
                    outPDF.close();

                    for (PDDocument d : pdfs) {
                        d.close();
                    }
                } catch (IOException e) {
                    String err = String.format(
                        "Unable to create a new PDF |%s|",
                        outputPath.getPath()
                    );
                    Log.e(MERGE_SETTING_TAG, err);
                    Log.e(MERGE_SETTING_TAG, e.getMessage());
                }
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    "Stop as there are no files",
                    Toast.LENGTH_SHORT
                ).show();
            }

            // TODO: A new activity for loading and notify about operation complete
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize PDF Box
        PDFBoxResourceLoader.init(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize mFileListSource before inflating activity below
        mFileListSource = new ArrayList<>();

        // Define the layout
        setContentView(R.layout.activity_merge_setting);

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize reference
        mEditText = (EditText) findViewById(R.id.file_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.merge_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.merge_now) {
            this.mergePDF();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
