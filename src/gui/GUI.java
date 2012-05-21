/*
 * Copyright (C) 2012 Alexander Glumoff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gui;

import dupa.DupFile;
import dupa.DupFinder;
import dupa.Duplicate;
import dupa.FileList;
import dupa.utils.DuplicatesList;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Alexander Glumoff
 */
public class GUI {

  private DupFinder dFinder;
  private mainWindow wnd;

  public void initGui() {
    initMainWindow();
    initDupFinder();
  }

  protected void initMainWindow() {
    wnd = new mainWindow();
    wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    clearTreeview((JTree) wnd.getDupsView());
    wnd.setFindAction(new AbstractAction("Find") {

      @Override
      public void actionPerformed(ActionEvent ae) {
        dFinder.find();
        fillTreeview((JTree) wnd.getDupsView(), dFinder.getDupsOnHash());
      }
    });

    wnd.setVisible(true);
  }

  protected void initDupFinder() {
    dFinder = new dupa.DupFinder();
    dFinder.addPath("/mnt/store/devel/dupa");
  }

  public void fillTreeview(JTree tree, DuplicatesList dupsList) {
    clearTreeview(tree);
    Iterator it = dupsList.keySet().iterator();
    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
    while (it.hasNext()) {
      String key = it.next().toString();
      Duplicate dup = dupsList.get(key);
      DefaultMutableTreeNode dupNode = new DefaultMutableTreeNode(dup);
      root.add(dupNode);
      Iterator it2 = dup.iterator();
      while (it2.hasNext()) {
        DupFile dupFile = (DupFile) it2.next();
        dupNode.add(new DefaultMutableTreeNode(dupFile));
      }
    }
    tree.updateUI();
  }

  public void clearTreeview(JTree tree) {
    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
    root.removeAllChildren();
    tree.updateUI();
    //treeModel.setRoot(null);
  }
}