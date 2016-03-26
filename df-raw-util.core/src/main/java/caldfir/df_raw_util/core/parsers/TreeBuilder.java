package caldfir.df_raw_util.core.parsers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class TreeBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(TreeBuilder.class);

  private RelationshipMap relMap;
  private TagIterator iter;
  private Tag root;

  private String fileName;

  public TreeBuilder(String fileName, RelationshipMap relMap)
      throws IOException {
    this.fileName = fileName;
    this.relMap = relMap;
    String extension =
        fileName.substring(fileName.length() - 3, fileName.length());
    if (extension.equals("txt")) {
      iter = new RawIterator(fileName);
      rawBuild();
      iter.close();
    } else if (extension.equals("xml")) {
      iter = new XIterator(fileName);
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
          LOG.warn(
              "unrecognized tag:\t"
                  + childTag.tagName()
                  + "\t"
                  + fileName
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
    if (relMap.isParentOfChild(parent.tagName(), child.tagName())) {
      parent.addChild(child);
      return true;
    }
    return false;
  }

  public Tag getRoot() {
    return root;
  }

}
