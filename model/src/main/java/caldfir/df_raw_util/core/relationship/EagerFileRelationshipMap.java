package caldfir.df_raw_util.core.relationship;

import java.io.IOException;
import java.util.Map;

public class EagerFileRelationshipMap extends MemoryRelationshipMap {

  public EagerFileRelationshipMap(RelationshipFileParser relParser)
      throws IOException {
    // initialize the dictionary
    Map<String, String> redirect = relParser.readRedirect();
    for(String parent : redirect.keySet()) {
      addRelationships(parent, relParser.readChildren(redirect.get(parent)));
    }
  }

}
