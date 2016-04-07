package caldfir.df_raw_util.app.filter;

import java.util.NoSuchElementException;
import java.util.Set;

import caldfir.df_raw_util.core.primitives.Tag;

public class TagArg1ChildFilter extends TagChildFilter {

  private final Set<String> tagNames;

  public TagArg1ChildFilter(Set<String> tagNames) {
    this.tagNames = tagNames;
  }

  @Override
  protected boolean predicate(Tag child) {
    try {
      return tagNames.contains(child.getArgument(1));
    } catch (NoSuchElementException e) {
      return false;
    }
  }
}
