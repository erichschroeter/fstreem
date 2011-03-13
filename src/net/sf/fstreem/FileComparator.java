package net.sf.fstreem;

import java.io.File;
import java.util.Comparator;

/**
 * Compares to {@link java.io.File} objects together, for collation (sorting)
 * purposes.
 * <p>Instances of this class sort null, then folders then files, keeping
 * everything in {@link String#CASE_INSENSITIVE_ORDER CASE_INSENSITIVE_ORDER}.
 * </p>
 */
public class FileComparator implements Comparator {
    public static final Comparator INSTANCE = new FileComparator();

    public int compare(Object o1, Object o2) {
        if (null == o1 && null == o2) {
            return 0;
        } else if (null == o1) {
            return -1;
        } else if (null == o2) {
            return 1;
        }

        File f1 = (File) o1;
        File f2 = (File) o2;

        if (f1.isDirectory() && !f2.isDirectory()) {
            return -1;
        } else if (!f1.isDirectory() && f2.isDirectory()) {
            return 1;
        } else {
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }
}
