package caldfir.df_raw_util.core.relationship;

/**
 * A map where either all relationships are allowed or none are.  
 */
public class BoolRelationshipMap extends RelationshipMap {

  private boolean areRelated;
  
  public BoolRelationshipMap(boolean areRelated){
    this.areRelated = areRelated;
  }

  @Override
  public boolean isParentOfChild(String parent, String child) {
    return areRelated;
  }

}
