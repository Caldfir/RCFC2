package caldfir.df_raw_util.core.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import caldfir.df_raw_util.core.compose.FormatWriter;

public class IOConfig extends Config {

  // property file/classpath names
  public static final String CORE_RESOURCE_NAME = "/df_raw_util.io.properties";

  // specific properties being pulled from the file
  public static final String IO_INPUT_PROPERTY = "io.input";
  public static final String IO_TARGET_PROPERTY = "io.target";
  public static final String IO_OUTPUT_YES_PROPERTY = "io.output.yes";
  public static final String IO_OUTPUT_NO_PROPERTY = "io.output.no";

  public static final String FORMAT_INDENT = "format.indent";
  public static final String FORMAT_NEWLINE = "format.newline";

  public IOConfig() {
    super(CORE_RESOURCE_NAME);
  }

  public long countInputFiles() {
    return countFiles(IO_INPUT_PROPERTY);
  }

  public long countTargetFiles() {
    return countFiles(IO_TARGET_PROPERTY);
  }

  public long countFiles(String key) {
    try {
      return Files.list(Paths.get(getFileProperty(key))).count();
    } catch (IOException e) {
      return 0;
    }
  }

  public File[] listInputFiles() throws IOException {
    return listFiles(IO_INPUT_PROPERTY);
  }

  public File[] listTargetFiles() throws IOException {
    return listFiles(IO_TARGET_PROPERTY);
  }

  private File[] listFiles(String key) throws IOException {
    File folder = new File(getFileProperty(key));
    if (!folder.exists() || !folder.isDirectory()) {
      throw new IOException(
          "directory does not exist: " + folder.getAbsolutePath());
    }

    return folder.listFiles();
  }

  public FormatWriter buildOutputWriter(String basename) throws IOException {
    return buildWriter(getFileProperty(IO_OUTPUT_YES_PROPERTY), basename);
  }

  public FormatWriter buildBooleanWriter(String basename, boolean success)
      throws IOException {
    String successProperty =
        success ? IO_OUTPUT_YES_PROPERTY : IO_OUTPUT_NO_PROPERTY;
    return buildWriter(getFileProperty(successProperty), basename);
  }

  private FormatWriter buildWriter(String pathname, String basename)
      throws IOException {
    File toWrite = new File(FilenameUtils.concat(pathname, basename));
    FormatWriter writer =
        new FormatWriter(new BufferedWriter(new FileWriter(toWrite)));
    String indent = getFileProperty(FORMAT_INDENT);
    writer.setIndent(indent != null ? indent : "\t");
    String newline = getFileProperty(FORMAT_NEWLINE);
    writer.setNewline(newline != null ? newline : System.lineSeparator());
    return writer;
  }

  private String getFileProperty(String key) {
    return FilenameUtils.normalize(getProperty(key));
  }
}
