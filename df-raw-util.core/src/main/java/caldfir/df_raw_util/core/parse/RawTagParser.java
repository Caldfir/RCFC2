package caldfir.df_raw_util.core.parse;

import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class RawTagParser extends TagParser {

  public static final String RAW_TAG_DELIMITER = "\\:";
  public static final Pattern RAW_TAG_PATTERN =
      Pattern.compile("\\[([^\\]\\[]*)\\]");

  private RelationshipMap relMap;

  public RawTagParser(Reader reader, RelationshipMap relMap) {
    super(reader);
    this.relMap = relMap;
  }

  @Override
  protected Tag buildTag(String tagString) {
    Matcher m = getPattern().matcher(tagString);
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

  private Tag buildTag(LinkedList<String> args) {
    Tag result = new Tag(args);
    for (Tag before = peekPrev(); before != null; before = before.getParent()) {
      if (relMap.isParentOfChild(before.tagName(), result.tagName())) {
        before.addChild(result);
        break;
      }
    }
    
    return result;
  }

  @Override
  protected Pattern getPattern() {
    return RAW_TAG_PATTERN;
  }
}
