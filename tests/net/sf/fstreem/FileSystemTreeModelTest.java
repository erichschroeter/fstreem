package net.sf.fstreem;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Vector;

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
    
    public void testReturnsOnlyFilteredFiles() throws IOException {
    	/*
    	 * 	tmp/
    	 * 		file1.svg
    	 * 		files/
    	 * 			file1.svg
    	 * 			file2.svg
    	 * 			file1.sh
    	 * 			file1.txt
    	 * 		text/
    	 * 			file1.txt
    	 * 			file2.txt
    	 */
    	
    	createFile("file1.svg");
    	createFolder("files");
    	createFile("files/file1.svg");
    	createFile("files/file2.svg");
    	createFile("files/file1.sh");
    	createFile("files/file2.txt");
    	createFolder("text");
    	createFile("text/file1.txt");
    	createFile("text/file2.txt");
    	
    	Vector<FileFilter> filters = new Vector<FileFilter>();
    	FileFilter svgFilter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
		        String fileName = pathname.getName();
		        int i = fileName.lastIndexOf('.');
		        if (i > 0 && i < fileName.length() - 1)
		        {
		            String extension = fileName.substring(i+1).toLowerCase();
		            if(extension.equals("svg"))
		            {
		                return true;
		            }
		        }
		        return false;
			}
		};
		FileFilter directoryFilter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		};
		
    	filters.add(svgFilter);
    	filters.add(directoryFilter);
    	
    	FileSystemTreeNode root =  FileSystemTreeNode.create(getTestRoot(), filters);
    	System.out.println(root.getChildAt(0).location);
    	System.out.println(root.getChildAt(1).location);
    	System.out.println(root.getChildAt(2).location);
    	// root should should have 2 directories and 1 svg file
    	assertEquals(3, root.getChildCount());
    	// files directory should have 2 svg files
    	assertEquals(2, root.getChildAt(0).getChildCount());
    	// text directory should have 0 files
    	assertEquals(0, root.getChildAt(1).getChildCount());
    }
}
