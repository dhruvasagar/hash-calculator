package hashcalculator.components;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.IOException;

import hashcalculator.Images;
import hashcalculator.utils.Logger;
import hashcalculator.HashCalculator;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class HelpContent extends JDialog {
  
  private static final long serialVersionUID=0L;

  private void addToolBar() {
    JToolBar toolBar=new JToolBar();
    JButton btn = new JButton("",Images.backGif);
    toolBar.add(btn);
    btn = new JButton("",Images.forwardGif);
    toolBar.add(btn);
    toolBar.addSeparator();
    btn=new JButton("",Images.homeGif);
    toolBar.add(btn);
    toolBar.addSeparator();
    btn=new JButton("Print");
    toolBar.add(btn);
    btn=new JButton("Exit");
    toolBar.add(btn);
    toolBar.setFloatable(false);
    add(toolBar, BorderLayout.NORTH);
  }

  public HelpContent(final HashCalculator hc) {
    super(hc);

    JSplitPane split;
    JTabbedPane tabs;
    JScrollPane areaPane;
    JScrollPane treeScroller;
    DefaultMutableTreeNode root;
    DefaultMutableTreeNode child;
    DefaultMutableTreeNode innerNode;

    final JTree contents;
    final JEditorPane htmlArea = new JEditorPane();

    UIManager.put("Tree.closedIcon", Images.emptyGif);
    UIManager.put("Tree.collapsedIcon", Images.plusGif);
    UIManager.put("Tree.expandedIcon", Images.minusGif);
    UIManager.put("Tree.leafIcon", Images.topicGif);
    UIManager.put("Tree.openIcon", Images.emptyGif);

    setModal(true);
    setTitle("Help Contents");
    setLocationRelativeTo(hc);
    setLayout(new BorderLayout());
    setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);

    addToolBar();

    root=new DefaultMutableTreeNode("Hash Calculator");

    innerNode =new DefaultMutableTreeNode("Notice");
    root.add(innerNode);

    innerNode=new DefaultMutableTreeNode("License");
    root.add(innerNode);

    innerNode=new DefaultMutableTreeNode("Getting Started");
    child=new DefaultMutableTreeNode("Accessibilty");
    child.add(new DefaultMutableTreeNode("blah blah"));
    innerNode.add(child);
    child=new DefaultMutableTreeNode("Starting");
    innerNode.add(child);
    child=new DefaultMutableTreeNode("Exiting");
    innerNode.add(child);
    root.add(innerNode);

    innerNode=new DefaultMutableTreeNode("Modes");
    child=new DefaultMutableTreeNode("Text Mode");
    innerNode.add(child);
    child=new DefaultMutableTreeNode("File Mode");
    innerNode.add(child);
    root.add(innerNode);

    contents=new JTree(root);
    contents.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    contents.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent arg0)
      {
        String helpDocBase = "help_contents/";
        String selection=contents.getSelectionPath().getLastPathComponent().toString();
        Logger.debug(selection);
        try {
          htmlArea.setPage(HelpContent.class.getClassLoader().getResource(helpDocBase + selection + ".htm"));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    treeScroller=new JScrollPane(contents);
    tabs=new JTabbedPane();
    tabs.addTab("Contents",treeScroller);
    tabs.addTab("Search",new JPanel());

    htmlArea.setEditable(false);
    areaPane=new JScrollPane(htmlArea);

    split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,tabs,areaPane);
    split.setDividerLocation(200);
    split.setDividerSize(1);
    add(split, BorderLayout.CENTER);

    setVisible(true);
  }

}
