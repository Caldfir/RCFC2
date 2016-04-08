package caldfir.df_raw_util.core.compose;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

import caldfir.df_raw_util.core.primitives.TagNode;

public abstract class TagComposer implements Closeable {

  private final Writer writer;
  
  public TagComposer( Writer writer ) {
    this.writer = writer;
  }

  public abstract void writeHeader(TagNode root) throws IOException;
  
  public abstract void writeTag(TagNode tag) throws IOException;
  
  public void compose(TagNode root) throws IOException {
    writeHeader(root);
    writeTag(root);
  }
  
  @Override
  public void close() throws IOException {
    writer.close();
  }
  
  protected void writeString(String s) throws IOException {
    writer.write(s);
  }
  
  protected void writeNewline() throws IOException {
    writer.write(System.lineSeparator());
  }

  protected void writeIndent(int depth) throws IOException {
    for( int i = 0; i < depth; i++ ) {
      writer.write('\t');
    }
  }
}
