package com.practical_developer.mergepdf.file;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store essential content  of a "file" which will be presented in FileListFragment
 */
public class FileItem {
    private final String fileName;
    private final String fileType;
    private final Uri uri;

    public String getFileName () {
        return fileName;
    }

    public String getFileType () {
        return fileType;
    }

    public Uri getUri () {
        return uri;
    }

    public FileItem (String fileName, String fileType, Uri u) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.uri = u;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.fileName, this.fileType);
    }
}
