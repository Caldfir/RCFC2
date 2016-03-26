package caldfir.df_raw_util.core.relationship;

public abstract class RelationshipMap {

  public abstract boolean isParentOfChild(String parent, String child);
  
  public boolean isRelated(String a, String b)
  {
    return isParentOfChild(a,b) || isParentOfChild(b,a);
  }

}
