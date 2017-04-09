package caldfir.df_raw_util.core.relationship;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazyFileRelationshipMap extends RelationshipMap {

  private static final Logger LOG =
      LoggerFactory.getLogger(LazyFileRelationshipMap.class);
  
  private final RelationshipFileParser relParser;
  private Map<String, String> redirect;
  private MemoryRelationshipMap dictionary;
  
  public LazyFileRelationshipMap(RelationshipFileParser relParser) {
    this.relParser = relParser;
    this.dictionary = new MemoryRelationshipMap();
    this.redirect = null;
  }

  @Override
  public boolean isParentOfChild(String parent, String child) {
    // if we haven't loaded the redirect file yet, then do so now
    if(redirect == null) {
      try {
        redirect = relParser.readRedirect();
      } catch (IOException e) {
        LOG.error("problem loading redirect", e);
        throw new RuntimeException(e);
      };
    }
    
    // no redirect means no children ever
    if(!redirect.containsKey(parent)) {
      return false;
    }
    
    // if we have a redirect but no values, then we need to load the children
    if(!dictionary.isParent(parent)) {
      try {
        dictionary.addRelationships(parent, relParser.readChildren(parent));
      } catch (IOException e) {
        LOG.error("problem loading children", e);
        throw new RuntimeException(e);
      }
    }
    
    return dictionary.isParentOfChild(parent, child);
  }

}
