package caldfir.df_raw_util.core.compose;

import java.io.IOException;
import java.io.Writer;

public class FormatWriter extends Writer {

  private final Writer inner;

  private char[] newline;

  private char[] indent;

  public FormatWriter(Writer inner) {
    this.inner = inner;
    this.newline = System.lineSeparator().toCharArray();
    this.indent = "\t".toCharArray();
  }

  @Override
  public void close() throws IOException {
    inner.close();
  }

  @Override
  public void flush() throws IOException {
    inner.flush();
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    inner.write(cbuf, off, len);
  }

  public void newline() throws IOException {
    inner.write(newline);
  }
  
  public void indent() throws IOException {
    indent(1);
  }

  public void indent(int depth) throws IOException {
    for (int i = 0; i < depth; i++) {
      inner.write(indent);
    }
  }

  public void setNewline(String newline) {
    this.newline = newline.toCharArray();
  }

  public void setIndent(String indent) {
    this.indent = indent.toCharArray();
  }
}
