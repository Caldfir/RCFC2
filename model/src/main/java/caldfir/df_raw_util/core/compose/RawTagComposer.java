package caldfir.df_raw_util.core.compose;

import java.io.Closeable;
import java.io.IOException;

import caldfir.df_raw_util.core.primitives.TagNode;

public class RawTagComposer implements Closeable {

  private final FormatWriter writer;
  private String headerLine;
  private int titleDepth;

  public RawTagComposer(FormatWriter writer, String headerLine) {
    this.writer = writer;
    this.headerLine = headerLine;
    this.titleDepth = 1;
  }

  public void compose(TagNode root) throws IOException {
    writeHeader(root);
    writeTag(root, 0);
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
  }

  public void writeTag(TagNode tag, int depth) throws IOException {
    // if this is a "title" then add some whitespace
    if (depth <= titleDepth) {
      writer.newline();
    }

    // write this tag's content
    writer.indent(depth);
    writeTagBody(tag);
    writer.newline();

    // recurse on children
    for (int i = 0; i < tag.getNumChildren(); i++) {
      writeTag(tag.getChild(i), depth + 1);
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
