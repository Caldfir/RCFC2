package caldfir.df_raw_util.app.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import caldfir.df_raw_util.core.primitives.TagNode;

public class TagArg1ChildFilterTest {
  
  private static final String OBJECT_S = "OBJECT";
  private static final String CREATURE_S = "CREATURE";
  private static final String ALPACA_S = "ALPACA";
  private static final String LLAMA_S = "LLAMA";
  private static final String CAVY_S = "CAVY";
  
  private TagNode testNode;
  private TreeSet<String> template;
  
  @Before
  public void setup(){
    testNode = createTag(OBJECT_S,CREATURE_S);
    
    testNode.addChild(createTag(CREATURE_S,ALPACA_S));
    testNode.addChild(createTag(CREATURE_S,LLAMA_S));
    testNode.addChild(createTag(CREATURE_S,CAVY_S));
    
    template = new TreeSet<String>();
    template.add(ALPACA_S);
    template.add(LLAMA_S);
    template.add(CAVY_S);
  }
  
  private TagNode createTag(String first, String second) {
    ArrayList<String> temp = new ArrayList<String>();
    temp.add(first);
    temp.add(second);
    return new TagNode(temp);
  }

  @Test
  public void testExtract() {
    TagArg1ChildFilter filter = new TagArg1ChildFilter(template);
    TagNode out = filter.extractMatchingChildren(testNode);

    assertTrue(out.getNumChildren() == 3);
    assertTrue(testNode.getNumChildren() == 0);
  }

  @Test
  public void testCopy() {
    TagArg1ChildFilter filter = new TagArg1ChildFilter(template);
    TagNode out = filter.copyMatchingChildren(testNode);

    assertTrue(out.getNumChildren() == 3);
    assertTrue(testNode.getNumChildren() == 3);
  }

}
