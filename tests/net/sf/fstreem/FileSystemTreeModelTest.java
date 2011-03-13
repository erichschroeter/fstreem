package net.sf.fstreem;

import java.io.File;
import java.io.IOException;

public class FileSystemTreeModelTest extends AbstractFileSystemTestCase {
    private FileSystemTreeModel model;

    protected void childSetUp() throws IOException {
        super.childSetUp();

        model = new FileSystemTreeModel(getTestRoot());
    }

    public void testKnowsItsRoot() {
        assertEquals(FileSystemTreeNode.create(getTestRoot()),
                     model.getRoot());
    }

    public void testKnowsChildCountAtRoot() throws IOException {
        createFile("file.txt");
        assertEquals(1, model.getChildCount(model.getRoot()));
    }

    public void testKnowsChildCountAtFirstLevel() throws IOException {
        File home = createFolder("john");
        createFolder("john/jim");
        createFolder("john/jack");
        createFile("john/parent.txt");

        assertEquals(3, model.getChildCount(FileSystemTreeNode.create(home)));
    }

    public void testMakesAllFilesLeaves() throws IOException {
        File peter = createFolder("peter");
        File resume = createFile("peter/resume.txt");
        File jamesfraser = createFile("jamesfraser.txt");

        assertTrue(model.isLeaf(FileSystemTreeNode.create(resume)));
        assertTrue(model.isLeaf(FileSystemTreeNode.create(jamesfraser)));
        assertFalse(model.isLeaf(FileSystemTreeNode.create(peter)));
    }

    public void testReturnsNodesForChildrenAtRoot() throws IOException {
        createFile("james.txt");
        createFile("sam.txt");

        FileSystemTreeNode root = FileSystemTreeNode.create(getTestRoot());
        assertEquals(root.getChildAt(0), model.getChild(model.getRoot(), 0));
        assertEquals(root.getChildAt(1), model.getChild(model.getRoot(), 1));
    }

    public void testReturnsNodesForChildrenAtFirstLevel() throws IOException {
        File home = createFolder("friends");
        createFile("friends/james.txt");
        createFile("friends/sam.txt");

        FileSystemTreeNode root = FileSystemTreeNode.create(home);
        FileSystemTreeNode modelRoot =
                (FileSystemTreeNode) model.getChild(model.getRoot(), 0);
        assertEquals(root.getChildAt(0), model.getChild(modelRoot, 0));
        assertEquals(root.getChildAt(1), model.getChild(modelRoot, 1));
    }

    public void testKnowsIndexAtRoot() throws IOException {
        createFolder("jamie");
        createFolder("kelly");
        createFolder("sarah");

        FileSystemTreeNode root = FileSystemTreeNode.create(getTestRoot());
        assertEquals(0,
                     model.getIndexOfChild(model.getRoot(),
                                           root.getChildAt(0)));
        assertEquals(1,
                     model.getIndexOfChild(model.getRoot(),
                                           root.getChildAt(1)));
        assertEquals(2,
                     model.getIndexOfChild(model.getRoot(),
                                           root.getChildAt(2)));
    }

    public void testReturnsNegativeIndexForUnknownChild() throws IOException {
        FileSystemTreeNode anne = FileSystemTreeNode.create(
                createFolder("anne"));

        assertEquals(-1, model.getIndexOfChild(anne, anne));
    }
}
