package caldfir.df_raw_util.core.compose;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import caldfir.df_raw_util.core.primitives.Tag;

public class XmlTagComposer extends TagComposer {

  public static final String XML_HEADER =
      "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";

  public XmlTagComposer(Writer writer) {
    super(writer);
  }
  
  @Override
  public void writeHeader(Tag root) throws IOException{
    writeString(XML_HEADER);
    writeNewline();
  }

  @Override
  public void writeTag(Tag tag) throws IOException {
    // write the start-tag
    writeIndent(tag.getDepth());
    writeString(buildString(tag, false));
    writeNewline();

    // recurse on children
    for (int i = 0; i < tag.getNumChildren(); i++) {
      writeTag(tag.getChild(i));
    }

    // write the end-tag
    if (tag.getNumChildren() > 0) {
      writeIndent(tag.getDepth());
      writeString(buildString(tag, true));
      writeNewline();
    }
  }

  protected String buildString(Tag tag, boolean endTag) {
    StringWriter strWrite = new StringWriter();
    strWrite.write('<');

    // if the is an end-tag then write the endtag marker
    if (endTag) {
      strWrite.write('/');
    }

    // write the tag name
    strWrite.write(tag.tagName());

    // only start-tags have arguments or a oneline marker
    if (!endTag) {
      // write the children
      for (int i = 0; i < tag.getNumChildren(); i++) {
        strWrite.write(" arg");
        strWrite.write(i);
        strWrite.write('=');
        strWrite.write(tag.getArgument(i));
      }
      // if no children then write a oneline marker
      if (tag.getNumChildren() == 0) {
        strWrite.write('/');
      }
    }

    strWrite.write('>');

    return strWrite.toString();
  }

}
