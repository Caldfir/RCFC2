package caldfir.df_raw_util.app.filter;

import java.util.function.Predicate;

import caldfir.df_raw_util.core.primitives.Tag;

public class TagPredicateChildFilter extends TagChildFilter {

  private Predicate<Tag> predicate;
  
  public TagPredicateChildFilter( Predicate<Tag> predicate ) {
    this.predicate = predicate;
  }
  
  @Override
  protected boolean predicate(Tag child) {
    return predicate.test(child);
  }

}
