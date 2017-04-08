package caldfir.df_raw_util.app.filter;

import java.util.function.Predicate;

import caldfir.df_raw_util.core.primitives.TagNode;

public class TagPredicateChildFilter extends TagChildFilter {

  private Predicate<TagNode> predicate;
  
  public TagPredicateChildFilter( Predicate<TagNode> predicate ) {
    this.predicate = predicate;
  }
  
  @Override
  protected boolean predicate(TagNode child) {
    return predicate.test(child);
  }

}
