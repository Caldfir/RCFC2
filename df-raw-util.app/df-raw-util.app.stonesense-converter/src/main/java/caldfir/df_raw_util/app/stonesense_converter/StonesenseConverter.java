package caldfir.df_raw_util.app.stonesense_converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.lang.NumberFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.config.RelationshipConfig;
import caldfir.df_raw_util.core.parse.TreeBuilder;
import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;
import caldfir.df_raw_util.ui.FileProgressFrame;

public class StonesenseConverter {

  private static final Logger LOG =
      LoggerFactory.getLogger(StonesenseConverter.class);

  static final int SHEET_WIDE = 20;

  public static void main(String[] args) {

    final String SRC_FOLDER = "in";
    final String DST_FOLDER = "out";

    File folder = new File(SRC_FOLDER);
    File[] fileList = folder.listFiles();
    FileProgressFrame display =
        new FileProgressFrame("df to ssense", 2 * fileList.length);
    FileWriter writer = null;
    PrintWriter out;
    TreeBuilder t;
    RelationshipConfig c = new RelationshipConfig();
    RelationshipMap relFileMap = c.buildRelationshipMap();

    try {
      display.setVisible(true);

      String inName = null;
      String shortName;
      String extension;

      String output = "";
      String index = "";

      try {
        for (int i = 0; i < fileList.length; i++) {
          inName = fileList[i].getName();
          shortName = inName.substring(0, inName.length() - 4);
          extension = inName.substring(inName.length() - 3, inName.length());
          // read and parse
          t = new TreeBuilder(SRC_FOLDER + "/" + inName, relFileMap);
          display.set("reading " + inName, 2 * i + 1);
          // write
          if (extension.equals("txt")) {
            if (t.getRoot() != null) {
              output = toSSenseXString(t.getRoot());
              if (output != null) {
                display.set("writing " + shortName + ".xml", 2 * i + 2);

                // write
                writer = new FileWriter(DST_FOLDER + "/" + shortName + ".xml");
                out = new PrintWriter(writer);
                out.print(output);

                out.close();
                writer.close();

                // add the newly written file to the index
                index += shortName + ".xml\n";
              } else
                LOG.error(
                    "non-graphics file \"" + shortName + ".txt\" was skipped");
            } else
              LOG.error("invalid or empty file: " + shortName + ".txt");
          }
        }

        // write the index
        writer = new FileWriter(DST_FOLDER + "/" + "index.txt");
        out = new PrintWriter(writer);
        out.print(index);

        out.close();
        writer.close();

      } catch (IOException e0) {
        e0.printStackTrace();
      }
      display.setVisible(false);
    } finally {
      System.exit(0);
    }
  }

  /**
   * Converts a loaded graphics file into a string representation appropriate
   * for loading the graphics in Stonesense. Returns null if this is not a
   * graphics raw file.
   */
  private static String toSSenseXString(Tag root) {

    if (root.getArgument(1).compareTo("GRAPHICS") != 0)
      return null;
    HashMap<String, GraphicsSheet> filenames = getFileMap(root);
    Tag current = null, currentchild = null;

    String xml = "<?xml version=\"1.0\"?>\n";
    xml += "<creatures>\n";

    for (int i = 0; i < root.getChildCount(); i++) {
      current = root.getChildAt(i);
      if (current.tagName().compareTo("CREATURE_GRAPHICS") == 0) {
        xml += "\t<creature gameID=\"" + current.getArgument(1) + "\">\n";
        for (int j = 0; j < current.getChildCount(); j++) {
          currentchild = current.getChildAt(j);

          xml += "\t\t<variant ";

          try {

            int index = Integer.parseInt(currentchild.getArgument(2));

            if (filenames.containsKey(currentchild.getArgument(1))) {
              GraphicsSheet g = filenames.get(currentchild.getArgument(1));
              xml += "file=\"" + g.filename + "\" ";
              if (g.zoom != 0)
                xml += "zoom=\"" + g.zoom + "\" ";
              if (g.height > 0 && index >= SHEET_WIDE) {
                int jump = index / SHEET_WIDE;
                jump = jump * g.height * SHEET_WIDE;

                index = index % SHEET_WIDE;
                index = index + jump;
              }
            } else {
              LOG.error(
                  "argument 1 was expected to be a tile page specifier: "
                      + currentchild.toRawString());
              xml += "/>\n";
              continue;
            }

            index += SHEET_WIDE * Integer.parseInt(currentchild.getArgument(3));
            xml += "sheetIndex=\"" + index + "\" ";

            if (currentchild.tagName().compareTo("DEFAULT") != 0
                && currentchild.tagName().compareTo("STANDARD") != 0) {
              xml += "profession=\"" + currentchild.tagName() + "\" ";
            }

            if (currentchild.tagLength() < 6)
              xml += "special=\"Normal\" ";
            else if (currentchild.getArgument(5).compareTo("DEFAULT") == 0)
              xml += "special=\"Normal\" ";
            else if (currentchild.getArgument(5).compareTo("ANIMATED") == 0)
              xml += "special=\"Zombie\" ";
            else if (currentchild.getArgument(5).compareTo("GHOST") == 0)
              xml += "special=\"Skeleton\" ";

            if (currentchild.tagLength() >= 5
                && currentchild.getArgument(4).compareTo("ADD_COLOR") == 0)
              xml += "color=\"profession\" ";

          } catch (NumberFormatException nfe) {
            LOG.error(
                "arguments 3 and 4 were expected to be integers: "
                    + currentchild.toRawString());
          }

          xml += "/>\n";
        }

        xml += "\t</creature>\n";
      }

    }

    xml += "</creatures>\n";
    return xml;
  }

  static class GraphicsSheet {
    public String filename;
    public int zoom;
    public int height;

    public GraphicsSheet(String filenamee, int zoomm, int heightt) {
      filename = filenamee;
      zoom = zoomm;
      height = heightt;
    }
  }

  /**
   * Parses the file, returns a map between the tile page descriptors and the
   * relevant information.
   */
  private static HashMap<String, GraphicsSheet> getFileMap(Tag root) {

    Tag current = null, currentchild = null;
    HashMap<String, GraphicsSheet> filenames =
        new HashMap<String, GraphicsSheet>();

    // load up the mapping from page descriptors to file names
    for (int i = 0; i < root.getChildCount(); i++) {
      current = root.getChildAt(i);

      if (current.tagName().compareTo("TILE_PAGE") == 0) {

        String filename = null;
        int zoom = 0;
        int height = 0;

        for (int j = 0; j < current.getChildCount(); j++) {
          currentchild = current.getChildAt(j);

          if (currentchild.tagName().compareTo("FILE") == 0) {
            filename = currentchild.getArgument(1);
          } else if (currentchild.tagName().compareTo("TILE_DIM") == 0) {
            int xdim = Integer.parseInt(currentchild.getArgument(1));
            int ydim = Integer.parseInt(currentchild.getArgument(2));
            // check to see if these are equal powers of two - otherwise we just
            // default to 0
            if (xdim == ydim && (xdim & (-xdim)) == xdim) {
              int pow = 0;
              int pow2 = 1;
              while (pow2 < xdim && pow2 > 0) {
                pow2 = 2 * pow2;
                pow++;
              }
              zoom = pow - 5;
            }
          } else if (currentchild.tagName().compareTo("PAGE_DIM") == 0) {
            height = Integer.parseInt(currentchild.getArgument(2));
          }
        }
        if (filename != null) {
          filenames.put(
              current.getArgument(1),
              new GraphicsSheet(filename, zoom, height));
        }
      }
    }
    return filenames;
  }
}
