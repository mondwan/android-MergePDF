package com.practical_developer.mergepdf;

import com.practical_developer.mergepdf.file.FileItem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FileItemTest {
    private FileItem mFileItem = null;

    @Before
    public void setup() throws Exception {
        mFileItem = new FileItem("File 1", "File Type 1", null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("File1 File Type 1", mFileItem.toString());
    }

    @Test
    public void testGetFileName () throws Exception {
        assertEquals("File1", mFileItem.getFileName());
    }

    @Test
    public void testGetFileType () throws Exception {
        assertEquals("File Type 1", mFileItem.getFileType());
    }

    @Test
    public void testGetUri () throws Exception {
        assertNull(mFileItem.getUri());
    }
}