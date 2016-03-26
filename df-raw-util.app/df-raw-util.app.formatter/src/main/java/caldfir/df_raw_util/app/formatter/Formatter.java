package caldfir.df_raw_util.app.formatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.config.RelationshipConfig;
import caldfir.df_raw_util.core.parsers.TreeBuilder;
import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;
import caldfir.df_raw_util.ui.FileProgressFrame;

public class Formatter {

  private static final Logger LOG = LoggerFactory.getLogger(Formatter.class);

  public static void main(String[] args) {

    final String SRC_FOLDER = "in";
    final String DST_FOLDER = "out";

    File folder = new File(SRC_FOLDER);
    if( !folder.exists() ){
      LOG.error("directory '" + folder.getAbsolutePath() + "' does not exist");
      return;
    }
    
    File[] fileList = folder.listFiles();
    FileProgressFrame display =
        new FileProgressFrame("Raw Checker", 2 * fileList.length);
    FileWriter writer = null;
    PrintWriter out;
    RelationshipConfig c = new RelationshipConfig();
    RelationshipMap relFileMap = c.buildRelationshipMap();

    try {
      display.setVisible(true);

      String inName = null;
      String shortName;
      String extension;

      String output = "";

      for (int i = 0; i < fileList.length; i++) {
        try {
          inName = fileList[i].getName();
          shortName = inName.substring(0, inName.length() - 4);
          extension = inName.substring(inName.length() - 3, inName.length());
          // read and parse
          TreeBuilder t = new TreeBuilder(SRC_FOLDER + "/" + inName, relFileMap);
          display.set("reading " + inName, 2 * i + 1);
          // write
          if (extension.equals("txt")) {
            Tag root = t.getRoot();
            if (root != null) {
              output = shortName + "\n\n";
              for (int j = 0; j < root.getChildCount(); j++)
                output =
                    output + "\t" + root.getChildAt(j).getArgument(1) + "\n";
              output = output + "\n" + root.toRawString();

              display.set("writing " + shortName + ".txt", 2 * i + 2);

              // write
              writer = new FileWriter(DST_FOLDER + "/" + shortName + ".txt");
              out = new PrintWriter(writer);
              out.print(output);

              out.close();
              writer.close();
            } else
              LOG.error("invalid or empty file: " + shortName + ".txt");
          }
        } catch (IOException e0) {
          e0.printStackTrace();
        }
      }
      display.setVisible(false);
    } finally {
      System.exit(0);
    }
  }
}
