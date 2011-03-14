package net.sf.fstreem;

import java.io.File;
import java.io.IOException;

public class FileComparatorTest extends AbstractFileSystemTestCase {
    private final FileComparator comparator = new FileComparator();

    public void testComparesNameOnly() {
        File a = new File("xyz/a");
        File b = new File("abc/b");

        assertTrue(comparator.compare(a, b) < 0);
        assertTrue(comparator.compare(b, a) > 0);
        assertTrue(comparator.compare(a, a) == 0);
        assertTrue(comparator.compare(b, b) == 0);
    }

    public void testComparesCaseInsensitively() {
        File ua = new File("A");
        File a = new File("a");

        assertTrue(comparator.compare(ua, a) == 0);
        assertTrue(comparator.compare(a, ua) == 0);
    }

    public void testBringsNullForward() {
        File file = new File(".");
        assertTrue(comparator.compare(null, file) < 0);
        assertTrue(comparator.compare(file, null) > 0);
        assertTrue(comparator.compare(null, null) == 0);
    }

    public void testBringsFoldersForward() throws IOException {
        File alpha = createFile("alpha");
        File bingo = createFolder("bingo");

        assertTrue(comparator.compare(alpha, bingo) > 0);
        assertTrue(comparator.compare(bingo, alpha) < 0);
    }
}
