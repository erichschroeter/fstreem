
import net.sf.fstreem.FileSystemTreeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;

public class SimpleTreeView extends JFrame {
    private final File root;

    private JPanel contentPane;
    private JTree tree;
    private JButton quit;
    private JButton refresh;

    public SimpleTreeView(File root) throws HeadlessException {
        super("SimpleTreeView - FileSystemTreeModel");
        this.root = root;
        initGui();
        refresh();

        refresh.setMnemonic('r');
        refresh.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class,
                this,
                "refresh"));

        quit.setMnemonic('q');
        quit.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class,
                this,
                "quit"));

        setContentPane(contentPane);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void initGui() {
        contentPane = new JPanel(new BorderLayout());
        tree = new JTree();
        refresh = new JButton("Refresh");
        quit = new JButton("Quit");

        Box rightPanel = Box.createVerticalBox();
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(Box.createVerticalStrut(4));
        rightPanel.add(refresh);
        rightPanel.add(Box.createVerticalStrut(4));
        rightPanel.add(quit);
        rightPanel.add(Box.createVerticalStrut(4));

        Box centerPanel = Box.createHorizontalBox();
        centerPanel.add(new JScrollPane(tree));
        centerPanel.add(Box.createHorizontalStrut(4));
        centerPanel.add(rightPanel);

        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        tree.setModel(new FileSystemTreeModel(root));
    }

    public void quit() {
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        File fileSystemRoot = new File(".").getCanonicalFile();
        new SimpleTreeView(fileSystemRoot).show();
    }
}
