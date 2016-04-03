package caldfir.df_raw_util.app.organizer;

import java.util.Set;
import java.util.function.Predicate;

import caldfir.df_raw_util.core.primitives.Tag;

public class TagArg1ChildFilter extends TagChildFilter {

  private final Set<String> tagNames;
  private final Predicate<Tag> predicate;

  public TagArg1ChildFilter(Set<String> tagNames) {
    this.tagNames = tagNames;
    this.predicate = a -> this.tagNames.contains(a.getArgument(1));
  }
  
  public Tag selectMatchingChildren(Tag root) {
    return selectChildren(root,predicate);
  }
  
  public Tag selectNonMatchingChildren(Tag root) {
    return selectChildren(root,predicate.negate());
  }
}
