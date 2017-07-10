package caldfir.df_raw_util.core.compose;

import java.io.Closeable;
import java.io.IOException;

import caldfir.df_raw_util.core.primitives.TagNode;

public class XmlTagComposer implements Closeable {

  public static final String XML_HEADER =
      "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";

  private FormatWriter writer;

  public XmlTagComposer(FormatWriter writer) {
    this.writer = writer;
  }

  public void compose(TagNode root) throws IOException {
    writeHeader(root);
    writeTag(root);
  }

  public void writeHeader(TagNode root) throws IOException {
    writer.write(XML_HEADER);
    writer.newline();
  }

  public void writeTag(TagNode tag) throws IOException {
    // write the start-tag
    writer.indent(tag.getDepth());
    writeTagBody(tag, false);
    writer.newline();

    // recurse on children
    for (int i = 0; i < tag.getNumChildren(); i++) {
      writeTag(tag.getChild(i));
    }

    // write the end-tag
    if (tag.getNumChildren() > 0) {
      writer.indent(tag.getDepth());
      writeTagBody(tag, true);
      writer.newline();
    }
  }

  protected void writeTagBody(TagNode tag, boolean endTag) throws IOException {
    writer.write('<');

    // if the is an end-tag then write the endtag marker
    if (endTag) {
      writer.write('/');
    }

    // write the tag name
    writer.write(tag.tagName());

    // only start-tags have arguments or a oneline marker
    if (!endTag) {
      // write the children
      for (int i = 0; i < tag.getNumChildren(); i++) {
        writer.write(" arg");
        writer.write(i);
        writer.write('=');
        writer.write(tag.getArgument(i));
      }
      // if no children then write a oneline marker
      if (tag.getNumChildren() == 0) {
        writer.write('/');
      }
    }

    writer.write('>');
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
