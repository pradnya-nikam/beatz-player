package com.beatzplayer;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 * User: pradnya
 * Date: 7/8/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mp3FileFilter implements FilenameFilter {
    @Override
    public boolean accept(File file, String name) {
        return name.endsWith(".mp3");
    }
}
