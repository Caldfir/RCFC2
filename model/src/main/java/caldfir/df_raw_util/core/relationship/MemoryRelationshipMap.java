package caldfir.df_raw_util.core.relationship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MemoryRelationshipMap extends RelationshipMap {

  private Map<String, Set<String>> dictionary;

  public MemoryRelationshipMap(Map<String, Set<String>> dictionary) {
    this.dictionary = dictionary;
  }

  public MemoryRelationshipMap() {
    dictionary = new HashMap<String, Set<String>>();
  }

  public void addRelationship(String parent, String child) {
    // if it isn't already present, then add an entry for the parent
    if (!dictionary.containsKey(parent)) {
      dictionary.put(parent, new HashSet<String>());
    }

    // add this child entry
    dictionary.get(parent).add(child);
  }

  public void addRelationships(String parent, Set<String> children) {
    // if it isn't already present, then add an entry for the parent
    if (!dictionary.containsKey(parent)) {
      dictionary.put(parent, children);
    }
    // if there are already children, then add these children to those
    else {
      dictionary.get(parent).addAll(children);
    }
  }

  @Override
  public boolean isParentOfChild(String parent, String child) {
    return dictionary.containsKey(parent)
        && dictionary.get(parent).contains(child);
  }

  public boolean isParent(String parent) {
    return dictionary.containsKey(parent);
  }

}
