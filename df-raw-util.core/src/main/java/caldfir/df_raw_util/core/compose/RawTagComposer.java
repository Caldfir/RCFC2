package caldfir.df_raw_util.core.compose;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import caldfir.df_raw_util.core.primitives.TagNode;

public class RawTagComposer extends TagComposer {
  
  private String headerLine;

  public RawTagComposer(Writer writer, String headerLine) {
    super(writer);
    this.headerLine = headerLine;
  }

  @Override
  public void writeHeader(TagNode root) throws IOException {
    writeString(headerLine);
    writeNewline();
    writeNewline();
    
    for(int i = 0; i < root.getNumChildren(); i++) {
      writeIndent(1);
      writeString(root.getChild(i).getArgument(1));
      writeNewline();
    }
    writeNewline();
  }
  
  @Override
  public void writeTag(TagNode tag) throws IOException {
    // write this tag's content
    writeIndent(tag.getDepth());
    writeString(buildString(tag));
    writeNewline();
    
    // recurse on children
    for(int i = 0; i < tag.getNumChildren(); i++) {
      writeTag(tag.getChild(i));
    }
  }

  protected String buildString(TagNode tag) {
    StringWriter strWrite = new StringWriter();
    
    strWrite.write('[');
    strWrite.write(tag.tagName());
    for(int i = 1; i < tag.getNumArguments(); i++) {
      strWrite.write(':');
      strWrite.write(tag.getArgument(i));
    }
    strWrite.write(']');
    
    return strWrite.toString();
  }
}
