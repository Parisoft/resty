package com.github.parisoft.resty.util;

import java.io.File;
import java.nio.file.Files;

public class FileUtils {

    public static String read(String filepath) throws Exception {
        final File jsonFile = new File(FileUtils.class.getResource(filepath).toURI().getPath());
        return new String(Files.readAllBytes(jsonFile.toPath()));
    }
}
