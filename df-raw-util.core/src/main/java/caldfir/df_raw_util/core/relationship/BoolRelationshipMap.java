package caldfir.df_raw_util.core.relationship;

/**
 * A map where either everyone is related or nobody is related.
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
