package caldfir.df_raw_util.core.primitives;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Tag {

  private Tag parent;
  private Vector<Tag> children;
  private ArrayList<String> args;

  public Tag() {
    parent = null;
    children = new Vector<Tag>();
    args = new ArrayList<String>();
  }

  public Tag(List<String> args) {
    this();
    this.copyArgs(args);
  }

  public Tag clone() {
    Tag t = new Tag();
    t.copyArgs(this);
    t.copyChildren(this);

    return t;
  }

  public void addChild(Tag c) {
    children.add(c);
    if(c.parent != null) {
      c.parent.removeChild(c);
    }
    c.parent = this;
  }
  
  public void removeChild(Tag c) {
    Iterator<Tag> it = children.iterator();
    while(it.hasNext()) {
      if(it.next() == c){
        it.remove();
      }
    }
  }

  public void addChildren(Collection<Tag> cs) {
    Iterator<Tag> it = cs.iterator();
    while(it.hasNext()) {
      addChild(it.next());
    }
  }

  public Tag getChild(int i) {
    return children.get(i);
  }

  public int getNumChildren() {
    return children.size();
  }

  public int getNumArguments() {
    return args.size();
  }

  public String tagName() {
    return getArgument(0);
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

  public void sortChildren(Comparator<Tag> tComp) {
    Collections.sort(children, tComp);
  }

  public void copyChildren(Tag t) {
    copyChildren(t.children);
  }

  public void copyChildren(Collection<Tag> cs) {
    Iterator<Tag> it = cs.iterator();
    while(it.hasNext()) {
      addChild(it.next().clone());
    }
  }
  
  public void copyArgs(Tag t){
    copyArgs(t.args);
  }
  
  public void copyArgs(List<String> argsin) {
    args.clear();
    args.addAll(argsin);
  }

  public boolean isLeaf() {
    if (children.size() == 0) {
      return true;
    }
    return false;
  }
  
  public Tag getParent() {
    return parent;
  }
  
  /**
   * Equality is based on equality of all arguments.  Children are ignored.
   */
  public boolean equals(Tag other) {
    if(other.getNumArguments() != this.getNumArguments()){
      return false;
    }
    
    for( int i = 0; i < this.getNumArguments(); i++ ) {
      if(other.getArgument(i) != this.getArgument(i)) {
        return false;
      }
    }
    
    return true;
  }
}
