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
    writeString(root.getArgument(0));
    writeNewline();
    writeNewline();
    
    for(int i = 0; i < root.getChildCount(); i++) {
      writeIndent(1);
      writeString(root.getChildAt(i).getArgument(0));
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
    for(int i = 0; i < tag.getChildCount(); i++) {
      writeTag(tag.getChildAt(i));
    }
  }

  protected String buildString(Tag tag) {
    StringWriter strWrite = new StringWriter();
    
    strWrite.write('[');
    strWrite.write(tag.getArgument(0));
    for(int i = 1; i < tag.getNumArguments(); i++) {
      strWrite.write(':');
      strWrite.write(tag.getArgument(i));
    }
    strWrite.write(']');
    
    return strWrite.toString();
  }
}
