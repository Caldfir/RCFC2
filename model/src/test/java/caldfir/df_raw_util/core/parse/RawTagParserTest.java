package caldfir.df_raw_util.core.parse;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import caldfir.df_raw_util.core.primitives.TagNode;
import caldfir.df_raw_util.core.relationship.BoolRelationshipMap;

public class RawTagParserTest {

  public static final String RAW_ONE = "[OBJECT:CREATURE]";
  public static final String RAW_TWO = "[CREATURE:OCELOT]";
  
  @Test
  public void testParseOneElement() {
    RawTagParser rtp =
        new RawTagParser(
            new StringReader(RAW_ONE),
            new BoolRelationshipMap(true),
            "source" );
    
    TagNode tag = rtp.parse();
    
    try {
      rtp.close();
    } catch (IOException e) {
      // no-op
    }
    
    assertTrue(tag.getArgument(0).equals("OBJECT"));
    assertTrue(tag.getArgument(1).equals("CREATURE"));
    assertTrue(tag.getNumChildren() == 0);
  }
}
