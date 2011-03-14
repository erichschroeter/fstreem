package net.sf.fstreem;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

/**
 * {@link TreeModel TreeModel} implementation that <strong>does not</strong>
 * provide events for file system changes.
 * <p>If you want to refresh your tree following changes in the filesystem,
 * change the tree's model to a new instance of this class.</p>
 */
public class FileSystemTreeModel implements TreeModel {
    private final FileSystemTreeNode root;

    public FileSystemTreeModel(File root, Vector<FileFilter> filters) {
        this.root = FileSystemTreeNode.create(root, filters);
    }

    public FileSystemTreeModel(File root) {
        Vector<FileFilter> filters = new Vector<FileFilter>();
        this.root = FileSystemTreeNode.create(root, filters);
    }

    /**
     * Returns the root {@link FileSystemTreeNode FileSystemTreeNode} of this
     * model.
     *
     * @return The root {@link FileSystemTreeNode FileSystemTreeNode} of this
     * model.
     */
    public Object getRoot() {
        return root;
    }

    public int getChildCount(Object parent) {
        checkNodeType(parent);
        return ((FileSystemTreeNode) parent).getChildCount();
    }

    public boolean isLeaf(Object node) {
        checkNodeType(node);
        return ((FileSystemTreeNode) node).isFile();
    }

    public Object getChild(Object parent, int index) {
        checkNodeType(parent);
        return ((FileSystemTreeNode) parent).getChildAt(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (null == parent || null == child) {
            return -1;
        }

        checkNodeType(parent);
        checkNodeType(child);

        FileSystemTreeNode home = (FileSystemTreeNode) parent;
        FileSystemTreeNode target = (FileSystemTreeNode) child;
        for (int i = 0; i < home.getChildCount(); i++) {
            if (home.getChildAt(i).equals(target)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * IGNORED !  This method does nothing.
     *
     * @param path
     * @param newValue
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    /**
     * IGNORED !  This method does nothing.
     *
     * @param l
     */
    public void addTreeModelListener(TreeModelListener l) {
    }

    /**
     * IGNORED !  This method does nothing.
     *
     * @param l
     */
    public void removeTreeModelListener(TreeModelListener l) {
    }

    private void checkNodeType(Object node) {
        if (node instanceof FileSystemTreeNode) return;

        throw new IllegalArgumentException("Expected a " +
                                           FileSystemTreeNode.class.getName() +
                                           " instance, received a " +
                                           node.getClass().getName());
    }
}
