package caldfir.df_raw_util.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
  
  public static final String GLOBAL_RESOURCE_NAME = "/df_raw_util.properties";

  private static final Logger LOG = LoggerFactory.getLogger(Config.class);

  private final Properties properties;

  public Config(String coreResourceName) {
      Properties baseProps = new Properties();
      try {
        setPropertyResource(baseProps, coreResourceName);
      } catch (IOException e) {
        LOG.error("problem loading core-properties");
        LOG.error(e.toString());
      }
      
      Properties props = new Properties(baseProps);
      try {
        setPropertyResource(props, GLOBAL_RESOURCE_NAME);
      } catch (IOException e) {
        LOG.warn("problem loading override-properties");
        LOG.warn(e.toString());
      }
      
      this.properties = props;
  }

  private void setPropertyResource(Properties props, String resourceName)
      throws IOException {
    InputStream stream = getClass().getResourceAsStream(resourceName);
    if( stream == null ){
      throw new IOException("resource " + resourceName + " unavailable");
    }
    
    try {
      props.load(stream);
    } finally {
        stream.close();
    }
  }

  public Properties getProperties() {
    return properties;
  }
  
  public String getProperty(String key) {
    return getProperties().getProperty(key);
  }
}
