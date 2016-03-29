package caldfir.df_raw_util.core.compose;

import java.util.Iterator;

import caldfir.df_raw_util.core.primitives.Tag;

public class XmlTagComposer extends TagComposer {


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
}
