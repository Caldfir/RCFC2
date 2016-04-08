package caldfir.df_raw_util.app.formatter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.compose.RawTagComposer;
import caldfir.df_raw_util.core.compose.TagComposer;
import caldfir.df_raw_util.core.config.IOConfig;
import caldfir.df_raw_util.core.config.RelationshipConfig;
import caldfir.df_raw_util.core.parse.RawTagParser;
import caldfir.df_raw_util.core.parse.TagParser;
import caldfir.df_raw_util.core.primitives.TagNode;
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
      TagNode root = null;
      TagParser parser = null;
      try {
        parser = new RawTagParser(fileList[i], relFileMap);
        root = parser.parse();
      } catch (IOException e) {
        LOG.error(e.toString());
        continue;
      } finally {
        try {
          parser.close();
        } catch (IOException e){
          // no-op
        }
      }

      // write
      display.set("writing " + shortName, 2 * i);
      TagComposer composer = null;
      try {
        Writer w = ioConfig.buildOutputWriter(shortName);
        composer = new RawTagComposer(w,shortName);
        composer.compose(root);
      } catch (IOException e) {
        LOG.error(e.toString());
        continue;
      } finally {
        try {
          composer.close();
        } catch (IOException e) {
          // no-op
        }
      }
    }
    
    display.setVisible(false);
  }
}
