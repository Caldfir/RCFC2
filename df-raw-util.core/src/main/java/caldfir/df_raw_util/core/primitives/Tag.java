package caldfir.df_raw_util.core.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.tree.TreeNode;

public class Tag implements TreeNode {

  private Tag parent;
  private ArrayList<Tag> children;
  private ArrayList<String> args;

  public Tag() {
    parent = null;
    children = new ArrayList<Tag>();
    args = new ArrayList<String>();
  }

  public Tag(List<String> args) {
    this();
    this.args.addAll(args);
  }

  public Tag clone() {
    Tag t = new Tag();
    t.copyArgs(this);
    t.copyChildren(this);

    return t;
  }

  private void setParent(Tag p) {
    parent = p;
  }

  public void addChild(Tag c) {
    children.add(c);
    c.setParent(this);
  }

  public Tag getParent() {
    return parent;
  }

  public String tagName() {
    return args.get(0);
  }

  public int tagLength() {
    return args.size();
  }

  public String getArgument(int i) {
    return args.get(i);
  }

  public short getDepth() {
    if (parent == null) {
      return 0;
    }
    return (short) (parent.getDepth() + 1);
  }

  public Enumeration<Tag> children() {
    class TagEnumerator implements Enumeration<Tag> {

      private ArrayList<Tag> children;
      private int index;

      public TagEnumerator(ArrayList<Tag> children) {
        this.children = children;
        index = 0;
      }

      public boolean hasMoreElements() {
        return (index < children.size());
      }

      public Tag nextElement() {
        return children.get(index++);
      }

    }
    return new TagEnumerator(children);
  }

  public boolean getAllowsChildren() {
    return true;
  }

  public Tag getChildAt(int i) {
    return children.get(i);
  }

  public int getChildCount() {
    return children.size();
  }

  public int getIndex(TreeNode tNod) {
    Iterator<Tag> tIter = children.iterator();
    for (int i = 0; tIter.hasNext(); i++) {
      if (tNod == tIter.next()) {
        return i;
      }
    }
    return -1;
  }

  public boolean isLeaf() {
    if (children.size() == 0) {
      return true;
    }
    return false;
  }

  public void sortChildren(Comparator<Tag> tComp) {
    Collections.sort(children, tComp);
  }

  public void copyChildren(Tag t) {
    for (int i = 0; i < t.getChildCount(); i++) {
      Tag iTag = t.getChildAt(i).clone();
      addChild(iTag);
    }
  }
  
  public void copyArgs(Tag t){
    args.clear();
    args.addAll(t.args);
  }

  public Tag binarySearchChildren(Tag key, Comparator<Tag> tComp) 
      throws NoSuchElementException {
    int index = Collections.binarySearch(children, key, tComp);
    if (index >= 0 && tComp.compare(key, children.get(index)) == 0) {
      return children.get(index);
    }
    throw new NoSuchElementException();
  }
}
