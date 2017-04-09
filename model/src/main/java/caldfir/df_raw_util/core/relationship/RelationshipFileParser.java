package caldfir.df_raw_util.core.relationship;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelationshipFileParser {

  private static final String REDIRECT_LINE_PATTERN =
      "[\"]([0-9a-zA-Z_]*)[\"][ \t]*[\"]([0-9a-zA-Z_]*)[\"]";

  private static final String RELATIONSHIP_EXT = ".txt";

  private final Path dataDir;
  private final Path redirectFile;

  public RelationshipFileParser(Path dataDir, Path redirectFile) {
    this.dataDir = dataDir;
    this.redirectFile = redirectFile;
  }

  public RelationshipFileParser(String dataDir, String redirectFile) {
    this(Paths.get(dataDir), Paths.get(redirectFile));
  }

  public Map<String, String> readRedirect() throws IOException {
    return readRedirect(redirectFile);
  }

  public static Map<String, String> readRedirect(Path path) throws IOException {
    try (BufferedReader buf = Files.newBufferedReader(path)) {
      return readRedirect(buf);
    }
  }

  public static Map<String, String> readRedirect(BufferedReader reader)
      throws IOException {
    HashMap<String, String> direct = new HashMap<String, String>();
    Pattern pat = Pattern.compile(REDIRECT_LINE_PATTERN);
    String line;
    while ((line = reader.readLine()) != null) {
      Matcher match = pat.matcher(line);
      if (match.matches()) {
        direct.put(match.group(1), match.group(2));
      }
    }

    return direct;
  }

  public Set<String> readChildren(String parent) throws IOException {
    Path children = dataDir.resolve(Paths.get(parent, RELATIONSHIP_EXT));
    return readChildren(children);
  }

  public static Set<String> readChildren(Path path) throws IOException {
    try (BufferedReader buf = Files.newBufferedReader(path)) {
      return readChildren(buf);
    }
  }

  public static Set<String> readChildren(BufferedReader reader)
      throws IOException {
    HashSet<String> children = new HashSet<String>();
    String line;
    while ((line = reader.readLine()) != null) {
      children.add(line);
    }

    return children;
  }
}
