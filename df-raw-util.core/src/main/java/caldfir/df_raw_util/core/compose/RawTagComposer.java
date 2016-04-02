package caldfir.df_raw_util.core.compose;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import caldfir.df_raw_util.core.primitives.Tag;

public class RawTagComposer extends TagComposer {

  public RawTagComposer(Writer writer) {
    super(writer);
  }

  @Override
  public void writeHeader(Tag root) throws IOException {
    writeString(root.tagName());
    writeNewline();
    writeNewline();
    
    for(int i = 0; i < root.getNumChildren(); i++) {
      writeIndent(1);
      writeString(root.getChild(i).tagName());
      writeNewline();
    }
    writeNewline();
  }
  
  @Override
  public void writeTag(Tag tag) throws IOException {
    // write this tag's content
    writeIndent(tag.getDepth());
    writeString(buildString(tag));
    writeNewline();
    
    // recurse on children
    for(int i = 0; i < tag.getNumChildren(); i++) {
      writeTag(tag.getChild(i));
    }
  }

  protected String buildString(Tag tag) {
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
