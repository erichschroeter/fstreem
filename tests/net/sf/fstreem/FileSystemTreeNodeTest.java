package net.sf.fstreem;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileSystemTreeNodeTest extends AbstractFileSystemTestCase {
    public void testKnowsAboutItsFilesystemLocation() throws IOException {
        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        assertEquals("knows it's absolute location",
                     getTestRoot().getAbsolutePath(),
                     node.getFile().getAbsolutePath());
    }

    public void testKnowsNumberOfChildrenWhenChildCountIsZero()
            throws IOException {
        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        assertEquals(0, node.getChildCount());
    }

    public void testKnowsNumberOfChildrenWhenChildCountIsOne()
            throws IOException {
        createFile("file.txt");

        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        assertEquals(1, node.getChildCount());
    }

    public void testKnowsNumberOfChildrenWhenChildCountIsTwo()
            throws IOException {
        createFile("file0.txt");
        createFile("file1.txt");

        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        assertEquals(2, node.getChildCount());
    }

    public void testKnowsNumberOfChildrenWhenFoldersIncluded()
            throws IOException {
        createFolder("jam");
        createFile("jam/test.txt");

        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        assertEquals(1, node.getChildCount());
    }

    public void testKnowsWhetherItsADirectory() {
        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        assertFalse(node.isFile());
    }

    public void testKnowsWhetherItsAFile() throws IOException {
        File file = createFile("jam.txt");
        FileSystemTreeNode node = FileSystemTreeNode.create(file);
        assertTrue(node.isFile());
    }

    public void testReturnsChildrenAsNodes() throws IOException {
        File file = createFile("pack.txt");

        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        FileSystemTreeNode pack = node.getChildAt(0);
        assertEquals(file.getAbsolutePath(),
                     pack.getFile().getAbsolutePath());
    }

    public void testThrowsExceptionWhenAttemptingChildGetOnFileNode()
            throws IOException {
        File file = createFile("record.txt");
        FileSystemTreeNode node = FileSystemTreeNode.create(file);

        try {
            node.getChildAt(0);
        } catch (NotAFolderException e) {
            assertEquals("reports path to file that is not a directory",
                         file.getAbsolutePath(),
                         e.getOffendingFile().getAbsolutePath());
        }
    }

    public void testThrowsExceptionWhenAttemptingChildCountOnFileNode()
            throws IOException {
        File file = createFile("record.txt");
        FileSystemTreeNode node = FileSystemTreeNode.create(file);

        try {
            node.getChildCount();
        } catch (NotAFolderException e) {
            assertEquals("reports path to file that is not a directory",
                         file.getAbsolutePath(),
                         e.getOffendingFile().getAbsolutePath());
        }
    }

    public void testPreventsNegativeIndicesOnChildGet() throws IOException {
        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        try {
            node.getChildAt(-1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testPreventsOutOfBoundsIndicesOnChildGet()
            throws IOException {
        FileSystemTreeNode node = FileSystemTreeNode.create(getTestRoot());
        try {
            node.getChildAt(17);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testGeneratesCorrectNodeTypesAccordingToFileSystem()
            throws IOException {
        File dir0 = createFolder("dir0");
        File dir0File1 = createFile("dir0/file1.txt");
        File file0 = createFile("file0.txt");

        Set expectedContents = new HashSet();
        expectedContents.add(dir0.getAbsolutePath());
        expectedContents.add(dir0File1.getAbsolutePath());
        expectedContents.add(file0.getAbsolutePath());

        FileSystemTreeNode root = FileSystemTreeNode.create(getTestRoot());
        Set actualContents = new HashSet();
        recursivelyAddChildren(root, actualContents);

        assertEquals(
                "found expected files and folders through FileSystemTreeNode",
                expectedContents, actualContents);
    }

    private void recursivelyAddChildren(FileSystemTreeNode root,
                                        Set actualContents) {
        for (int i = 0; i < root.getChildCount(); i++) {
            FileSystemTreeNode child = root.getChildAt(i);
            if (!child.isFile()) {
                recursivelyAddChildren(child, actualContents);
            }

            actualContents.add(child.getFile().getAbsolutePath());
        }
    }

    public void testIsEqualToEqualNodeWithEqualPath() {
        FileSystemTreeNode root0 = FileSystemTreeNode.create(getTestRoot());
        FileSystemTreeNode root1 = FileSystemTreeNode.create(getTestRoot());

        assertEquals("equal to itself", root0, root0);
        assertEquals("equal to same path", root0, root1);
        assertEquals("equal to same path, reversed", root1, root0);

        assertEquals("equal hashCode, same",
                     root0.hashCode(), root0.hashCode());
        assertEquals("equal hashCode, equal nodes",
                     root0.hashCode(), root1.hashCode());
        assertEquals("equal hashCode, equal nodes, reversed",
                     root1.hashCode(), root0.hashCode());
    }

    public void testReturnsItemsInOrder() throws IOException {
        createFolder("jam");
        createFolder("jim");
        createFolder("john");
        createFile("atlanta.txt");
        createFile("zalak.txt");

        FileSystemTreeNode root = FileSystemTreeNode.create(getTestRoot());
        assertEquals("jam",
                     root.getChildAt(0).getFile().getName());
        assertEquals("jim",
                     root.getChildAt(1).getFile().getName());
        assertEquals("john",
                     root.getChildAt(2).getFile().getName());
        assertEquals("atlanta.txt",
                     root.getChildAt(3).getFile().getName());
        assertEquals("zalak.txt",
                     root.getChildAt(4).getFile().getName());
    }
}
