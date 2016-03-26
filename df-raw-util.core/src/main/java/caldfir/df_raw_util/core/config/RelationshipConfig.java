package caldfir.df_raw_util.core.config;

import caldfir.df_raw_util.core.relationship.FileRelationshipMap;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class RelationshipConfig extends Config {

  // property file/classpath names
  public static final String GLOBAL_RESOURCE_NAME = 
      "/df_raw_util.properties";
  public static final String CORE_RESOURCE_NAME = 
      "/df_raw_util.relationship.properties";
  
  // specific properties being pulled from the file
  public static final String RELATIONSHIP_DIR_NAME_PROPERTY =
      "relationship.dirname";
  public static final String RELATIONSHIP_FILE_EXT_PROPERTY =
      "relationship.fileext";
  public static final String REDIRECT_FILE_NAME_PROPRERTY =
      "relationship.redirect";

  public RelationshipConfig() {
    super(CORE_RESOURCE_NAME);
  }
  
  public RelationshipMap buildRelationshipMap() {
    FileRelationshipMap relFileMap =
        new FileRelationshipMap(
            getProperty(RELATIONSHIP_DIR_NAME_PROPERTY),
            getProperty(RELATIONSHIP_FILE_EXT_PROPERTY),
            getProperty(REDIRECT_FILE_NAME_PROPRERTY));
    return relFileMap;
  }
}
