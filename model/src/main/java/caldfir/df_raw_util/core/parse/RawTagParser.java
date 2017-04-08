package caldfir.df_raw_util.core.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.primitives.TagNode;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class RawTagParser extends TagParser {

  private static final Logger LOG = LoggerFactory.getLogger(RawTagParser.class);

  public static final String RAW_TAG_DELIMITER = "\\:";
  public static final Pattern RAW_TAG_PATTERN =
      Pattern.compile("\\[(([^\\]\\[]*))\\]");

  private RelationshipMap relMap;

  TagNode parent;

  public RawTagParser(File file, RelationshipMap relMap)
      throws FileNotFoundException {
    super(file);
    this.relMap = relMap;
    this.parent = null;
  }

  public RawTagParser(Reader reader, RelationshipMap relMap, String sourceName) {
    super(reader, sourceName);
    this.relMap = relMap;
    this.parent = null;
  }

  @Override
  protected TagNode buildTag(String tagString) {
    Matcher m = getPattern().matcher(tagString);
    if (!m.matches()) {
      return null;
    }
    String argString = m.group(1);

    Scanner parser = new Scanner(argString);
    parser.useDelimiter(RAW_TAG_DELIMITER);

    LinkedList<String> args = new LinkedList<String>();
    while (parser.hasNext()) {
      args.add(parser.next());
    }

    parser.close();

    return buildTag(args);
  }

  private TagNode buildTag(LinkedList<String> args) {
    // create the tag body
    TagNode result = new TagNode(args);

    // try to add this tag as a child to the previous tag or its ancestors
    for (TagNode before = parent; before != null; before = before.getParent()) {
      if (relMap.isParentOfChild(before.tagName(), result.tagName())) {
        before.addChild(result);
        break;
      }
    }

    // update the potential parent to be this tag
    if(parent == null || result.getParent() != null){
      parent = result;
    }
    // if we failed to add the child tag then print a message
    else if (LOG.isWarnEnabled()) {
      String sourceLine =
          String.format("%1$-25s", "[" + toString() + ":" + getLineNum() + "]");
      String unrecTag = 
          String.format("%1$-25s", result.tagName());
      LOG.warn("unknown tag: " + unrecTag + sourceLine);
    }

    return result;
  }

  @Override
  protected Pattern getPattern() {
    return RAW_TAG_PATTERN;
  }
}
