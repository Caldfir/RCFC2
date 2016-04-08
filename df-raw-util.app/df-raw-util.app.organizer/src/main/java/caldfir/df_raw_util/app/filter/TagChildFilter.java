package caldfir.df_raw_util.app.filter;

import caldfir.df_raw_util.core.primitives.TagNode;

public abstract class TagChildFilter {
  
  protected abstract boolean predicate(TagNode child);

  /** 
   * Finds the children of source which match the filter's predicate and 
   * returns a Tag containing those children.  Matching children are 
   * re-parented to the tag that is returned.  
   */
  public TagNode extractMatchingChildren(TagNode source ) {
    TagNode result = new TagNode();
    result.copyArguments(source);
    
    for( int i = 0; i < source.getNumChildren(); i++ ) {
      TagNode child = source.getChild(i);
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
  public TagNode copyMatchingChildren(TagNode source ) {
    TagNode result = new TagNode();
    
    result.copyArguments(source);
    for( int i = 0; i < source.getNumChildren(); i++ ) {
      TagNode child = source.getChild(i);
      if( predicate(child) ) {
        result.addChild(child.clone());
      }
    }
    
    return result;
  }
}
