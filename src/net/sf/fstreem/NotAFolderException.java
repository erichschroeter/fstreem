package net.sf.fstreem;

import java.io.File;

/**
 * Thrown when an operation that should be executed on folders is attempted
 * on a file {@link FileSystemTreeNode FileSystemTreeNode}.
 */
public class NotAFolderException extends RuntimeException {
    private final File offendingFile;

    public NotAFolderException(File offendingFile) {
        this.offendingFile = offendingFile;
    }

    public NotAFolderException(String message, File offendingFile) {
        super(message);
        this.offendingFile = offendingFile;
    }

    public NotAFolderException(Throwable cause, File offendingFile) {
        super(cause);
        this.offendingFile = offendingFile;
    }

    public NotAFolderException(String message,
                               Throwable cause,
                               File offendingFile) {
        super(message, cause);
        this.offendingFile = offendingFile;
    }

    public File getOffendingFile() {
        return offendingFile;
    }

    public String getMessage() {
        StringBuffer buffer = new StringBuffer();
        if (null != super.getMessage()) {
            buffer.append(super.getMessage());
            buffer.append(": ");
        }

        buffer.append("Unable to get child of File: [");
        buffer.append(getOffendingFile());
        buffer.append(']');
        return buffer.toString();
    }
}
