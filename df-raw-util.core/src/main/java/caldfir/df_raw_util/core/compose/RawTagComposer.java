package caldfir.df_raw_util.core.compose;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;

import caldfir.df_raw_util.core.primitives.Tag;

public class RawTagComposer extends TagComposer {


  public static void writeRawFile(Tag tag, String fileName, Writer writer) {
    PrintWriter outWriter = new PrintWriter(writer);

    // write the file name as the header
    outWriter.println(FilenameUtils.getBaseName(fileName));
    outWriter.println();

    // add a list of elements in the file
    for (int j = 0; j < tag.getChildCount(); j++) {
      outWriter.print('\t');
      outWriter.println(tag.getChildAt(j).getArgument(1));
    }
    outWriter.println();

    writeRaw(tag, outWriter);

    // don't close() since we don't want to release
    outWriter.flush();
    outWriter = null;
  }

  public static String toRawString(Tag tag) {
    
    StringWriter stringWriter = new StringWriter();
    writeRaw(tag, stringWriter);
    String raw = stringWriter.toString();
    try {
      stringWriter.close();
    } catch (IOException e) {
      // unrecoverable (probably impossible) state
      throw new RuntimeException(e);
    }

    return raw;
  }
  
  public static void writeRaw(Tag tag, Writer writer) {
    PrintWriter outWriter = new PrintWriter(writer);

    for (int i = 0; i < tag.getDepth(); i++) {
      outWriter.print('\t');
    }
    
    outWriter.print('[');
    for( int i=0; i<tag. ) {
      outWriter.print(it.next());
      if(it.hasNext()) {
        outWriter.print(':');
      }
    }
    outWriter.println(']');
    
    // don't close() since we don't want to release
    outWriter.flush();
    outWriter = null;
    
    for( Iterator<Tag> ci = children.iterator(); ci.hasNext(); ) {
      ci.next().writeRaw(writer);
    }
  }
}
