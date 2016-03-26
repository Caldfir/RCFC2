package caldfir.df_raw_util.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.relationship.FileRelationshipMap;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class CoreConfig {

  private static final Logger LOG = LoggerFactory.getLogger(CoreConfig.class);

  // property file/classpath names
  public static final String GLOBAL_RESOURCE_NAME = 
      "daf_raw_util.properties";
  public static final String CORE_RESOURCE_NAME = 
      "daf_raw_util_core.properties";
  
  // specific properties being pulled from the file
  public static final String RELATIONSHIP_DIR_NAME_PROPERTY =
      "relationship.dirname";
  public static final String RELATIONSHIP_FILE_EXT_PROPERTY =
      "relationship.fileext";
  public static final String REDIRECT_FILE_NAME_PROPRERTY =
      "relationship.redirect";

  private Properties coreProperties;

  public CoreConfig() {
    try {
      Properties baseProps = new Properties();
      setPropertyResource(baseProps, CORE_RESOURCE_NAME);
      Properties props = new Properties(baseProps);
      setPropertyResource(props, GLOBAL_RESOURCE_NAME);
      this.coreProperties = props;
    } catch (IOException e) {
      LOG.error("problem loading properties");
      LOG.error(e.toString());
      this.coreProperties = new Properties();
    }
  }

  private static void setPropertyResource(Properties props, String resourceName)
      throws IOException {
    InputStream stream = CoreConfig.class.getResourceAsStream(resourceName);
    try {
      props.load(stream);
    } finally {
      stream.close();
    }
  }

  public Properties getCoreProperties() {
    return coreProperties;
  }

  public String getRelationshipDirName() {
    return coreProperties.getProperty(RELATIONSHIP_DIR_NAME_PROPERTY);
  }

  public String getRelationshipFileExt() {
    return coreProperties.getProperty(RELATIONSHIP_FILE_EXT_PROPERTY);
  }

  public String getRedirectFileName() {
    return coreProperties.getProperty(REDIRECT_FILE_NAME_PROPRERTY);
  }
  
  public RelationshipMap buildRelationshipMap() {
    FileRelationshipMap relFileMap =
        new FileRelationshipMap(
            getRelationshipDirName(),
            getRelationshipFileExt(),
            getRedirectFileName());
    return relFileMap;
  }
}
