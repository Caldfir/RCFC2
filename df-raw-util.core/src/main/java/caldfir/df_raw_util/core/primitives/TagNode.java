package caldfir.df_raw_util.core.primitives;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * The TagNode class is a tree-node containing a list of strings called
 * <i>arguments</i>. This is the simple base-unit used for representing
 * hierarchical data. The parent/child relationship encodes the structure of the
 * data, while the arguments hold the content.
 */
public class TagNode {

  private TagNode parent;
  private ArrayList<TagNode> children;
  private ArrayList<String> arguments;

  /**
   * Default constructor.
   */
  public TagNode() {
    parent = null;
    children = new ArrayList<TagNode>();
    arguments = new ArrayList<String>();
  }

  /**
   * Simple constructor taking only the list of arguments.
   */
  public TagNode(List<String> args) {
    this();
    this.setArguments(args);
  }

  /**
   * Creates a deep-copy of this tag, including all arguments and all children.
   * Note that this will recursively clone all ancestors of this node.
   */
  public TagNode clone() {
    TagNode t = new TagNode();
    t.copyArguments(this);
    t.copyChildren(this);

    return t;
  }

  /**
   * Removes the specified child tag. Breaks the parent-child link.
   */
  public void removeChild(TagNode c) {
    Iterator<TagNode> it = children.iterator();
    while (it.hasNext()) {
      if (it.next() == c) {
        c.parent = null;
        it.remove();
      }
    }
  }

  /**
   * Adds the specified TagNode as a child to this one. If the prospective child
   * already has a parent, then that link is first broken.
   */
  public void addChild(TagNode c) {
    children.add(c);
    if (c.parent != null) {
      c.parent.removeChild(c);
    }
    c.parent = this;
  }

  /**
   * Add the given collection to the children of this node. Note that if the
   * added elements already have parents, then that link will be broken prior to
   * adding the new children to this node.
   * 
   * @see TagNode#addChild(TagNode)
   * @see TagNode#copyChildren(Collection)
   */
  public void addChildren(Collection<TagNode> cs) {
    Iterator<TagNode> it = cs.iterator();
    while (it.hasNext()) {
      addChild(it.next());
    }
  }

  /**
   * Removes all children from the target and adds them as children to this
   * node.
   * 
   * @see TagNode#addChild(TagNode)
   * @see TagNode#copyChildren(TagNode)
   */
  public void stealChildren(TagNode parent) {
    addChildren(parent.children);
  }

  /**
   * Add a clone of each of the Tags in the given collection to this node.  The
   * state of the original Tags is preserved.  
   * 
   * @see TagNode#clone()
   * @see TagNode#addChildren(Collection)
   */
  public void copyChildren(Collection<TagNode> cs) {
    Iterator<TagNode> it = cs.iterator();
    while (it.hasNext()) {
      addChild(it.next().clone());
    }
  }

  /** 
   * Clones all children of the target and adds them as children of this node. 
   * 
   * @see TagNode#clone()
   * @see TagNode#copyChildren(Collection) 
   */
  public void copyChildren(TagNode parent) {
    copyChildren(parent.children);
  }

  /**
   * Sets this TagNode's argument list to the given list of strings.
   */
  public void setArguments(List<String> argsin) {
    arguments.clear();
    arguments.addAll(argsin);
  }

  /**
   * Sets this TagNode's argument list to match the target's arguments.
   */
  public void copyArguments(TagNode t) {
    setArguments(t.arguments);
  }

  /**
   * Equality is based on equality of all arguments. Children are ignored.
   */
  public boolean equals(TagNode other) {
    if (other.getNumArguments() != this.getNumArguments()) {
      return false;
    }

    for (int i = 0; i < this.getNumArguments(); i++) {
      if (other.getArgument(i) != this.getArgument(i)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Sort the children of this node according to the given Comparator.  Only the
   * direct children are sorted (the sort is non-recursive).
   */
  public void sortChildren(Comparator<TagNode> tComp) {
    Collections.sort(children, tComp);
  }

  /**
   * Determines the depth of this node in the tree.  A "root" node is one with
   * a null parent, and has a depth of 0.
   */
  public short getDepth() {
    if (parent == null) {
      return 0;
    }
    return (short) (parent.getDepth() + 1);
  }

  /**
   * Determines if this node is a leaf.  A leaf is a node with no children.
   */
  public boolean isLeaf() {
    if (getNumChildren() == 0) {
      return true;
    }
    return false;
  }

  public TagNode getParent() {
    return parent;
  }

  public TagNode getChild(int i) {
    return children.get(i);
  }

  public int getNumChildren() {
    return children.size();
  }

  public int getNumArguments() {
    return arguments.size();
  }

  /**
   * Returns the TagNode's first argument.
   */
  public String tagName() {
    return getArgument(0);
  }

  public String getArgument(int i) {
    return arguments.get(i);
  }
}
