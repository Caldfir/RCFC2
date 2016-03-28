package caldfir.df_raw_util.core.primitives;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import javax.swing.tree.TreeNode;

import org.apache.commons.io.FilenameUtils;

public class Tag implements TreeNode {

  public static final byte RAW = 0;
  public static final byte ONELINE = 1;
  public static final byte ENDTAG = 2;
  public static final byte NONE = 3;

  private Tag parent;
  private ArrayList<Tag> children;
  private ArrayList<String> args;
  private byte xData;

  public Tag() {
    parent = null;
    children = new ArrayList<Tag>();
    args = new ArrayList<String>();
  }

  private Tag(Collection<String> args) {
    this();
    this.args.addAll(args);
  }

  public Tag clone() {
    Tag t = new Tag();
    t.copyTags(this);
    t.copyChildren(this);

    return t;
  }

  public static Tag xTag(String s) {
    Tag t = new Tag();
    t.xParse(s);
    return t;
  }

  public static Tag rawTag(String s) {
    Tag t = new Tag();
    t.rawParse(s);
    return t;
  }

  private void xParse(String s) {
    if (s.charAt(1) == '/') {
      s = s.substring(2, s.length() - 1);
      xData = ENDTAG;
    } else if (s.charAt(s.length() - 2) == '/') {
      s = s.substring(1, s.length() - 2);
      xData = ONELINE;
    } else {
      s = s.substring(1, s.length() - 1);
      xData = NONE;
    }

    Scanner parser = new Scanner(s);
    String temp;
    temp = parser.next();
    args.add(temp);

    temp = parser.findInLine("(\")([^\"])+(\")");

    while (temp != null) {
      args.add(temp.substring(1, temp.length() - 1));
      temp = parser.findInLine("(\")([^\"])+(\")");
    }

    parser.close();
  }

  private void rawParse(String s) {
    s = s.substring(1, s.length() - 1);

    Scanner parser = new Scanner(s);
    parser.useDelimiter("\\:");

    while (parser.hasNext()) {
      args.add(parser.next());
    }

    xData = RAW;
    parser.close();
  }

  private void setParent(Tag p) {
    parent = p;
  }

  public void addChild(Tag c) {
    children.add(c);
    c.setParent(this);
  }

  public Tag getParent() {
    return parent;
  }

  public String tagName() {
    return args.get(0);
  }

  public int tagLength() {
    return args.size();
  }

  public String getArgument(int i) {
    return args.get(i);
  }

  public short getDepth() {
    if (parent == null) {
      return 0;
    }
    return (short) (parent.getDepth() + 1);
  }

  public String toRawString() {
    
    StringWriter stringWriter = new StringWriter();
    writeRaw(stringWriter);
    String raw = stringWriter.toString();
    try {
      stringWriter.close();
    } catch (IOException e) {
      // unrecoverable (probably impossible) state
      throw new RuntimeException(e);
    }

    return raw;
  }
  
  public void writeRaw(Writer writer) {
    PrintWriter outWriter = new PrintWriter(writer);

    for (int i = 0; i < getDepth(); i++) {
      outWriter.print('\t');
    }
    
    outWriter.print('[');
    for( Iterator<String> it = args.iterator(); it.hasNext(); ) {
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

  public String toXString() {
    String xml = "";

    for (int i = 0; i < getDepth(); i++) {
      xml += "\t";
    }

    xml += "<";

    Iterator<String> tagIter = args.iterator();
    xml += tagIter.next();

    for (int j = 0; tagIter.hasNext(); j++) {
      xml += " var" + j + "=\"" + tagIter.next() + "\"";
    }

    if (children.size() == 0) {
      xml += "/>";
    } else {
      xml += ">";

      Iterator<Tag> childIter = children.iterator();
      while (childIter.hasNext()) {
        xml += "\n" + childIter.next().toXString();
      }

      xml += "\n";

      for (int i = 0; i < getDepth(); i++) {
        xml += "\t";
      }

      xml += "</" + tagName() + ">";
    }
    return xml;
  }

  public byte xData() {
    return xData;
  }

  public Enumeration<Tag> children() {
    class TagEnumerator implements Enumeration<Tag> {

      private ArrayList<Tag> children;
      private int index;

      public TagEnumerator(ArrayList<Tag> children) {
        this.children = children;
        index = 0;
      }

      public boolean hasMoreElements() {
        return (index < children.size());
      }

      public Tag nextElement() {
        return children.get(index++);
      }

    }
    return new TagEnumerator(children);
  }

  public boolean getAllowsChildren() {
    return true;
  }

  public Tag getChildAt(int i) {
    return children.get(i);
  }

  public int getChildCount() {
    return children.size();
  }

  public boolean hasChild(Tag child) {
    boolean match = true;
    for (int j = 0; j < children.size(); j++) {
      for (int i = 0; i < child.args.size(); i++) {
        // look at children of this tag recursively
        if (children.get(j).children.size() > 0
            && children.get(j).hasChild(child))
          return true;

        // search this tag directly
        if (child.args.get(i).compareTo(children.get(j).args.get(i)) != 0) {
          match = false;
          break;
        } else
          match = true;
      }
      if (match)
        return true;
    }

    return false;
  }

  public int getIndex(TreeNode tNod) {
    Iterator<Tag> tIter = children.iterator();
    for (int i = 0; tIter.hasNext(); i++) {
      if (tNod == tIter.next()) {
        return i;
      }
    }
    return -1;
  }

  public boolean isLeaf() {
    if (children.size() == 0) {
      return true;
    }
    return false;
  }

  public void sortChildren(Comparator<Tag> tComp) {
    Collections.sort(children, tComp);
  }

  public void copyChildren(Tag t) {
    for (int i = 0; i < t.getChildCount(); i++) {
      Tag iTag = t.getChildAt(i).clone();
      addChild(iTag);
    }
  }
  
  public void copyTags(Tag t){
    args.clear();
    args.addAll(t.args);
  }

  public Tag binarySearchChildren(
      Collection<String> key, 
      Comparator<Tag> tComp) {
    Tag keyTag = new Tag(key);
    return binarySearchChildren(keyTag, tComp);
  }

  public Tag binarySearchChildren(Tag key, Comparator<Tag> tComp) 
      throws NoSuchElementException {
    int index = Collections.binarySearch(children, key, tComp);
    if (index >= 0 && tComp.compare(key, children.get(index)) == 0) {
      return children.get(index);
    }
    throw new NoSuchElementException(key.toRawString());
  }

  public void writeRawFile(String fileName, Writer writer) {
    PrintWriter outWriter = new PrintWriter(writer);

    // write the file name as the header
    outWriter.println(FilenameUtils.getBaseName(fileName));
    outWriter.println();

    // add a list of elements in the file
    for (int j = 0; j < getChildCount(); j++) {
      outWriter.print('\t');
      outWriter.println(getChildAt(j).getArgument(1));
    }
    outWriter.println();

    writeRaw(outWriter);

    // don't close() since we don't want to release
    outWriter.flush();
    outWriter = null;
  }
}
