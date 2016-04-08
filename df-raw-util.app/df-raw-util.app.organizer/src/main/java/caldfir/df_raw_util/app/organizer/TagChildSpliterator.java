package caldfir.df_raw_util.app.organizer;

import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;

import caldfir.df_raw_util.core.primitives.TagNode;

public class TagChildSpliterator extends AbstractSpliterator<TagNode> {

  private final TagNode parent;
  private int index;

  public TagChildSpliterator(TagNode parent) {
    super(
        parent.getNumChildren(),
        Spliterator.SIZED | Spliterator.ORDERED | Spliterator.NONNULL);
    this.parent = parent;
    index = 0;
  }

  public int numElements() {
    return parent.getNumChildren();
  }

  public TagNode nextElement() {
    return parent.getChild(index++);
  }

  public boolean hasMoreElements() {
    return numElements() > index;
  }

  @Override
  public boolean tryAdvance(Consumer<? super TagNode> action) {
    if (hasMoreElements()) {
      action.accept(nextElement());
      return true;
    }

    return false;
  }

  protected int getIndex() {
    return index;
  }

  protected void setIndex(int index) {
    this.index = index;
  }

  protected TagNode getParent() {
    return parent;
  }
}
