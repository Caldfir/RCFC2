package caldfir.df_raw_util.core.compose;

import java.io.Closeable;
import java.io.IOException;

import caldfir.df_raw_util.core.primitives.TagNode;

public class RawTagComposer implements Closeable {

  private FormatWriter writer;
  private String headerLine;

  public RawTagComposer(FormatWriter writer, String headerLine) {
    this.writer = writer;
    this.headerLine = headerLine;
  }

  public void compose(TagNode root) throws IOException {
    writeHeader(root);
    writeTag(root);
  }

  public void writeHeader(TagNode root) throws IOException {
    writer.write(headerLine);
    writer.newline();
    writer.newline();

    for (int i = 0; i < root.getNumChildren(); i++) {
      writer.indent();
      writer.write(root.getChild(i).getArgument(1));
      writer.newline();
    }
    writer.newline();
  }

  public void writeTag(TagNode tag) throws IOException {
    // write this tag's content
    writer.indent(tag.getDepth());
    writeTagBody(tag);
    writer.newline();

    // recurse on children
    for (int i = 0; i < tag.getNumChildren(); i++) {
      writeTag(tag.getChild(i));
    }
  }

  protected void writeTagBody(TagNode tag) throws IOException {
    writer.write('[');
    writer.write(tag.tagName());
    for (int i = 1; i < tag.getNumArguments(); i++) {
      writer.write(':');
      writer.write(tag.getArgument(i));
    }
    writer.write(']');
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
