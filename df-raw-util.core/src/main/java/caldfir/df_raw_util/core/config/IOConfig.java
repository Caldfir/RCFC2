package caldfir.df_raw_util.core.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class IOConfig extends Config {

  // property file/classpath names
  public static final String CORE_RESOURCE_NAME = "/df_raw_util.io.properties";

  // specific properties being pulled from the file
  public static final String IO_INPUT_PROPERTY = "io.input";
  public static final String IO_OUTPUT_PROPERTY = "io.output";
  public static final String IO_OUTPUT_YES_PROPERTY = "io.output.yes";
  public static final String IO_OUTPUT_NO_PROPERTY = "io.output.no";

  public IOConfig() {
    super(CORE_RESOURCE_NAME);
  }

  public File[] listInputFiles() throws IOException {
    Path path =
        FileSystems.getDefault().getPath(getProperty(IO_INPUT_PROPERTY));
    File folder = new File(path.getFileName().toString());
    if (!folder.exists()) {
      throw new IOException(
          "directory '" + folder.getAbsolutePath() + "' does not exist");
    }

    return folder.listFiles();
  }

  public Writer buildOutputWriter(String basename) throws IOException {
    return buildWriter(getProperty(IO_OUTPUT_PROPERTY), basename);
  }

  public Writer buildBooleanWriter(String basename, boolean success)
      throws IOException {
    String successProperty =
        success ? IO_OUTPUT_YES_PROPERTY : IO_OUTPUT_NO_PROPERTY;
    return buildWriter(getProperty(successProperty), basename);
  }

  private Writer buildWriter(String pathname, String basename)
      throws IOException {
    Path path = FileSystems.getDefault().getPath(pathname, basename);
    File toWrite = new File(path.getFileName().toString());
    BufferedWriter writer = new BufferedWriter(new FileWriter(toWrite));

    return writer;
  }
}
