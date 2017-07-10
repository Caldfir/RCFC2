package caldfir.df_raw_util.core.compose;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

import caldfir.df_raw_util.core.primitives.TagNode;

public abstract class TagComposer implements Closeable {

  private final Writer writer;
  private static final String INDENT = "\t";

  public TagComposer(Writer writer) {
    this.writer = writer;
  }

  public abstract void writeHeader(TagNode root) throws IOException;

  public abstract void writeTag(TagNode tag) throws IOException;

  public void compose(TagNode root) throws IOException {
    writeHeader(root);
    writeTag(root);
  }

  protected void writeString(String s) throws IOException {
    writer.write(s);
  }

  protected void writeNewline() throws IOException {
    writeNewline(1);
  }

  protected void writeNewline(int depth) throws IOException {
    for (int i = 0; i < depth; i++) {
      writer.write(System.lineSeparator());
    }
  }

  protected void writeIndent() throws IOException {
    writeIndent(1);
  }

  protected void writeIndent(TagNode tag) throws IOException {
    writeIndent(tag.getDepth());
  }

  protected void writeIndent(int depth) throws IOException {
    for (int i = 0; i < depth; i++) {
      writer.write(INDENT);
    }
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
