package caldfir.df_raw_util.app.filter;

import caldfir.df_raw_util.core.primitives.Tag;

public abstract class TagChildFilter {
  
  protected abstract boolean predicate(Tag child);

  /** 
   * Finds the children of source which match the filter's predicate and 
   * returns a Tag containing those children.  Matching children are 
   * re-parented to the tag that is returned.  
   */
  public Tag extractMatchingChildren(Tag source ) {
    Tag result = new Tag();
    result.copyArgs(source);
    
    for( int i = 0; i < source.getNumChildren(); i++ ) {
      Tag child = source.getChild(i);
      if( predicate(child) ) {
        result.addChild(child);
      }
    }
    
    return result;
  }
  
  /**
   * Finds the children of source which match the filter's predicate and 
   * returns a Tag containing those children.  Matching children are cloned, 
   * so the source remains unchanged.  
   */
  public Tag copyMatchingChildren(Tag source ) {
    Tag result = new Tag();
    
    result.copyArgs(source);
    for( int i = 0; i < source.getNumChildren(); i++ ) {
      Tag child = source.getChild(i);
      if( predicate(child) ) {
        result.addChild(child.clone());
      }
    }
    
    return result;
  }
}
