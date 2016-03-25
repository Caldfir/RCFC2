package caldfir.df_raw_util.core.parsers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.Relationship;

public class TreeBuilder {

  private static final Logger LOG = 
      LoggerFactory.getLogger(TreeBuilder.class);

  private TagIterator iter;
  private Tag root;

  private String filename;

  public TreeBuilder(String filename) throws IOException {
    this.filename = filename;
    String extension =
        filename.substring(filename.length() - 3, filename.length());
    if (extension.equals("txt")) {
      iter = new RawIterator(filename);
      rawBuild();
      iter.close();
    } else if (extension.equals("xml")) {
      iter = new XIterator(filename);
      xBuild();
      iter.close();
    } else {
      throw new IOException("unrecognized file extension: " + extension);
    }
  }

  private void rawBuild() {
    root = iter.next();

    Tag childTag;
    Tag parentTag = root;
    Tag prevTag = null;

    while ((childTag = iter.next()) != null) {
      while (!addBranch(parentTag, childTag)) {
        parentTag = parentTag.getParent();
        if (parentTag == null) {
          LOG.warn("unrecognized tag:\t"
              + childTag.tagName()
              + "\t"
              + filename
              + "\t"
              + iter.getLine());
          childTag = prevTag;
          break;
        }
      }
      prevTag = childTag;
      parentTag = prevTag;
      if (parentTag == null) {
        parentTag = root;
      }
    }
  }

  private void xBuild() {
    // skip the first tag as it is the xml definition
    iter.next();
    root = iter.next();

    Tag tempChild;
    Tag tempParent = root;

    while ((tempChild = iter.next()) != null) {
      if (tempChild.xData() == Tag.ONELINE) {
        tempParent.addChild(tempChild);
      } else if (tempChild.xData() == Tag.NONE) {
        tempParent.addChild(tempChild);
        tempParent = tempChild;
      } else if (tempChild.xData() == Tag.ENDTAG) {
        tempParent = tempParent.getParent();
        if (tempParent == null) {
          // TODO
        }
      } else {
        // TODO throw exception
        return;
      }
    }
  }

  private boolean addBranch(Tag parent, Tag child) {
    Relationship r = Relationship.getInstance();
    if (r.lookup(parent.tagName(), child.tagName())) {
      parent.addChild(child);
      return true;
    }
    return false;
  }

  public Tag getRoot() {
    return root;
  }

}
