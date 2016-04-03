package caldfir.df_raw_util.core.parse;

import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caldfir.df_raw_util.core.primitives.Tag;

public class XmlTagParser extends TagParser {

  public static final Pattern XML_ATTRIBUTE_PATTERN =
      Pattern.compile("\\w*=(\\w*)");
  public static final Pattern XML_ELEMENT_PATTERN =
      Pattern.compile("<(/*)(\\w*)([^></]*)(/*)>");
  
  Tag parent;

  public XmlTagParser(Reader reader) {
    super(reader);
    this.parent = null;
  }

  @Override
  public Tag buildTag(String tagString) {
    Matcher elemMch = getPattern().matcher(tagString);
    if( !elemMch.matches() ) {
      return null;
    }

    // if this is an end-tag, then just update the previous tag to its parent,
    // and don't bother creating a tag
    boolean leadingSlash = !elemMch.group(1).isEmpty();
    if( leadingSlash && parent != null ) {
      parent = parent.getParent();
      return null;
    }

    // start the argument list with the element name
    LinkedList<String> args = new LinkedList<String>();
    String elementString = elemMch.group(2);
    args.add(elementString);

    // add the attributes to the tag arguments
    String attrString = elemMch.group(3);
    Scanner attrScn = new Scanner(attrString);
    while (attrScn.hasNext()) {
      String nextArg = attrScn.findInLine(attrPattern());
      if (nextArg == null) {
        break;
      }
      Matcher attrMch = attrPattern().matcher(nextArg);
      if( attrMch.matches() ) {
        args.add(attrMch.group(1));
      }
    }
    attrScn.close();

    // create the tag
    Tag tag = new Tag(args);
    
    // add this tag to the current parent tag
    if( parent != null ){
      parent.addChild(tag);
    }

    // if this is a start-tag, then it becomes the new parent
    boolean trailingSlash = !elemMch.group(4).isEmpty();
    if(!trailingSlash) {
      parent = tag;
    }
    
    return tag;
  }

  @Override
  protected Pattern getPattern() {
    return elemPattern();
  }

  protected Pattern elemPattern() {
    return XML_ELEMENT_PATTERN;
  }

  protected Pattern attrPattern() {
    return XML_ATTRIBUTE_PATTERN;
  }
}
