package caldfir.df_raw_util.core.parse;

import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caldfir.df_raw_util.core.primitives.Tag;

public class XmlTagParser extends TagParser{

  public static final Pattern XML_TAG_PATTERN = 
      Pattern.compile("<(/*)([^></]*)(/*)>");

  public XmlTagParser(Reader reader) {
    super(reader);
  }

  @Override
  public Tag buildTag(String tagString) {
    Matcher m = getPattern().matcher(tagString);
    
    // determine if this tag contains slashes
    boolean leadingSlash = !m.group(1).isEmpty();
    boolean trailingSlash = !m.group(3).isEmpty();
    //TODO use these to make tree-building easier
    
    // grab the main body
    String argString = m.group(2);
    
    //TODO
    return null;
  }

  @Override
  protected Pattern getPattern() {
    return XML_TAG_PATTERN;
  }
}
