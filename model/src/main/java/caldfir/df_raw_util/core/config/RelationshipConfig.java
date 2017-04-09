package caldfir.df_raw_util.core.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.relationship.EagerFileRelationshipMap;
import caldfir.df_raw_util.core.relationship.LazyFileRelationshipMap;
import caldfir.df_raw_util.core.relationship.RelationshipFileParser;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class RelationshipConfig extends Config {

  private static final Logger LOG =
      LoggerFactory.getLogger(RelationshipConfig.class);

  // property file/classpath names
  public static final String GLOBAL_RESOURCE_NAME = "/df_raw_util.properties";
  public static final String CORE_RESOURCE_NAME =
      "/df_raw_util.relationship.properties";

  // specific properties being pulled from the file
  public static final String RELATIONSHIP_DIR_NAME_PROPERTY =
      "relationship.dirname";
  public static final String REDIRECT_FILE_NAME_PROPRERTY =
      "relationship.redirect";
  public static final String REDIRECT_LAZY_PROPERTY = "relationship.lazy";

  public RelationshipConfig() {
    super(CORE_RESOURCE_NAME);
  }

  public RelationshipMap buildRelationshipMap() {

    RelationshipFileParser rfp =
        new RelationshipFileParser(
            getString(RELATIONSHIP_DIR_NAME_PROPERTY),
            getString(REDIRECT_FILE_NAME_PROPRERTY));

    RelationshipMap relFileMap = null;
    if (getBoolean(REDIRECT_LAZY_PROPERTY)) {
      relFileMap = new LazyFileRelationshipMap(rfp);
    } else {
      try {
        relFileMap = new EagerFileRelationshipMap(rfp);
      } catch (IOException e) {
        LOG.error("problem loading relationships", e);
        throw new RuntimeException(e);
      }
    }

    return relFileMap;
  }
}
