package caldfir.df_raw_util.core.relationship;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileRelationshipMap extends RelationshipMap {

  private static final Logger LOG =
      LoggerFactory.getLogger(FileRelationshipMap.class);

  private String relationshipDirName;
  private String relationshipFileExt;

  private MultiValueMap map;
  private HashMap<String, String> redirect;

  public FileRelationshipMap(
      String relationshipDirName,
      String relationshipFileExt,
      String redirectFileName) {
    this.relationshipDirName = relationshipDirName;
    this.relationshipFileExt = relationshipFileExt;
    this.map = new MultiValueMap();
    this.redirect = buildRedirect(redirectFileName);
  }

  private HashMap<String, String> buildRedirect(String redirectFileName) {
    HashMap<String, String> direct = new HashMap<String, String>();
    try {
      FileReader f = new FileReader(redirectFileName);
      Scanner scn = new Scanner(f);
      Pattern pSimp =
          Pattern
              .compile("[\"]([0-9a-zA-Z_]*)[\"][ \t]*[\"]([0-9a-zA-Z_]*)[\"]");
      while (scn.hasNextLine()) {
        String s = scn.nextLine();
        Matcher mSimp = pSimp.matcher(s);
        if (mSimp.matches()) {
          direct.put(mSimp.group(1), mSimp.group(2));
        }
      }
      scn.close();
    } catch (FileNotFoundException e) {
      LOG.error("could not locate " + redirectFileName);
    }

    return direct;
  }

  public boolean isParentOfChild(String parent, String child) {
    if (map.getCollection(parent) == null) {
      String filename =
          relationshipDirName + redirect.get(parent) + relationshipFileExt;
      try {
        FileReader f = new FileReader(filename);
        Scanner scn = new Scanner(f);
        while (scn.hasNextLine()) {
          map.put(parent, scn.nextLine());
        }
        scn.close();
      } catch (FileNotFoundException e) {
        LOG.warn("could not locate " + filename);
        map.put(parent, "");
      }
    }
    return map.containsValue(parent, child);
  }
}
