package caldfir.df_raw_util.app.organizer;

import java.util.Enumeration;
import java.util.function.Supplier;

import caldfir.df_raw_util.core.primitives.Tag;

public class TagChildSupplier implements Supplier<Tag>, Enumeration<Tag> {
  
  private final Tag parent;
  private int index;

  public TagChildSupplier(Tag parent) {
    this.parent = parent;
    index = 0;
  }
  
  public int numElements() {
    return parent.getNumChildren();
  }

  @Override
  public Tag nextElement() {
    return parent.getChild(index++);
  }

  @Override
  public boolean hasMoreElements() {
    return numElements() > index;
  }

  @Override
  public Tag get() {
    return nextElement();
  }
  
  protected int getIndex() {
    return index;
  }

  protected void setIndex(int index) {
    this.index = index;
  }

  protected Tag getParent() {
    return parent;
  }
}
