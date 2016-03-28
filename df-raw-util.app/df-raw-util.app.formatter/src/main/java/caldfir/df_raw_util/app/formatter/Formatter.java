package caldfir.df_raw_util.app.formatter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.config.IOConfig;
import caldfir.df_raw_util.core.config.RelationshipConfig;
import caldfir.df_raw_util.core.parsers.TreeBuilder;
import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;
import caldfir.df_raw_util.ui.FileProgressFrame;

public class Formatter {

  private static final Logger LOG = LoggerFactory.getLogger(Formatter.class);

  public static void main(String[] args) {

    IOConfig ioConfig = new IOConfig();
    File[] fileList = null;
    try {
      fileList = ioConfig.listInputFiles();
    } catch (IOException e) {
      LOG.error(e.toString());
      return;
    }

    RelationshipConfig relConfig = new RelationshipConfig();
    RelationshipMap relFileMap = relConfig.buildRelationshipMap();

    FileProgressFrame display =
        new FileProgressFrame("Raw Checker", 2 * fileList.length);
    display.setVisible(true);

    for (int i = 0; i < fileList.length; i++) {
      String shortName = FilenameUtils.getName(fileList[i].getName());

      // read and parse
      display.set("reading " + shortName, 2 * i + 1);
      Tag root = null;
      try {
        TreeBuilder t = new TreeBuilder(fileList[i].getPath(), relFileMap);
        root = t.getRoot();
      } catch (IOException e) {
        LOG.error(e.toString());
        continue;
      }

      // write
      display.set("writing " + shortName, 2 * i);
      Writer writer = null;
      try {
        writer = ioConfig.buildOutputWriter(shortName);
        root.writeRawFile(shortName, writer);
      } catch (IOException e) {
        LOG.error(e.toString());
      } finally {
        try {
          writer.close();
        } catch (Exception e) {
          LOG.error(e.toString());
        }
      }
    }
    
    display.setVisible(false);
  }
}
