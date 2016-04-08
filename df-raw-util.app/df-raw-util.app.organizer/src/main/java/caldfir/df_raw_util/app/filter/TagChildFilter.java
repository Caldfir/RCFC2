package caldfir.df_raw_util.app.filter;

import java.util.Stack;

import caldfir.df_raw_util.core.primitives.TagNode;

public abstract class TagChildFilter {

  protected abstract boolean predicate(TagNode child);

  /**
   * Finds the children of source which match the filter's predicate and returns
   * a Tag containing those children. Matching children are re-parented to the
   * tag that is returned.
   */
  public TagNode extractMatchingChildren(TagNode source) {
    TagNode result = new TagNode();
    result.copyArguments(source);

    // first we must collect the matching children, since adding them directly
    // modifies the underlying ordering of the tag children
    Stack<TagNode> matches = new Stack<TagNode>();
    for (int i = 0; i < source.getNumChildren(); i++) {
      TagNode child = source.getChild(i);
      if (predicate(child)) {
        matches.push(child);
      }
    }

    // move all the matching children from the source to the result
    while (!matches.isEmpty()) {
      result.addChild(matches.pop());
    }

    return result;
  }

  /**
   * Finds the children of source which match the filter's predicate and returns
   * a Tag containing those children. Matching children are cloned, so the
   * source remains unchanged.
   */
  public TagNode copyMatchingChildren(TagNode source) {
    TagNode result = new TagNode();

    result.copyArguments(source);
    for (int i = 0; i < source.getNumChildren(); i++) {
      TagNode child = source.getChild(i);
      if (predicate(child)) {
        result.addChild(child.clone());
      }
    }

    return result;
  }
}
