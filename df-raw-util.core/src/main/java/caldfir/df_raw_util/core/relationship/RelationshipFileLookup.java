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

public class RelationshipFileLookup extends RelationshipMap {

  private static final Logger LOG = LoggerFactory.getLogger(RelationshipFileLookup.class);
  
  private static final String TEXT_EXT = ".txt";
  private static final String RELATIONSHIPS_DIR = "etc/relationships/";
  private static final String REDIRECT_FILE_NAME =
      RELATIONSHIPS_DIR + "directory" + TEXT_EXT;

  private MultiValueMap map;
  private HashMap<String, String> redirect;

  public RelationshipFileLookup() {
    map = new MultiValueMap();
    redirect = buildRedirect();
  }

  private HashMap<String, String> buildRedirect() {
    HashMap<String, String> direct = new HashMap<String, String>();
    try {
      String s;
      FileReader f = new FileReader(REDIRECT_FILE_NAME);
      Scanner scn = new Scanner(f);
      Pattern pSimp =
          Pattern
              .compile("[\"]([0-9a-zA-Z_]*)[\"][ \t]*[\"]([0-9a-zA-Z_]*)[\"]");
      Matcher mSimp;
      while (scn.hasNextLine()) {
        s = scn.nextLine();
        mSimp = pSimp.matcher(s);
        if (mSimp.matches()) {
          direct.put(mSimp.group(1), mSimp.group(2));
        }
      }
      scn.close();
    } catch (FileNotFoundException e) {
      LOG.error("could not locate " + REDIRECT_FILE_NAME);
    }

    return direct;
  }

  public boolean isParentOfChild(String parent, String child) {
    if (map.getCollection(parent) == null) {
      String filename = RELATIONSHIPS_DIR + redirect.get(parent) + TEXT_EXT;
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
