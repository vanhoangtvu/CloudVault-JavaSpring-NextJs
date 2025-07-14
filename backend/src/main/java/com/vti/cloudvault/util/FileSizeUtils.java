package com.vti.cloudvault.util;

public class FileSizeUtils {

    public static String toMB(long bytes) {
        double mb = bytes / (1024.0 * 1024.0);
        return String.format("%.2f MB", mb);
    }
}

