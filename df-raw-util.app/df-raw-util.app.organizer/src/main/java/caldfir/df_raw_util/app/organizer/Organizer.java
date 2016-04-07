package caldfir.df_raw_util.app.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.app.filter.TagArg1ChildFilter;
import caldfir.df_raw_util.core.compose.RawTagComposer;
import caldfir.df_raw_util.core.compose.TagComposer;
import caldfir.df_raw_util.core.config.IOConfig;
import caldfir.df_raw_util.core.config.RelationshipConfig;
import caldfir.df_raw_util.core.parse.RawTagParser;
import caldfir.df_raw_util.core.parse.TagParser;
import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;
import caldfir.df_raw_util.ui.FileProgressFrame;

public class Organizer {

  private static final Logger LOG = LoggerFactory.getLogger(Organizer.class);

  private IOConfig ioConfig;
  private RelationshipConfig relConfig;
  private FileProgressFrame display;
  private int inputFileCount, outputFileCount;

  public Organizer() {
    this.ioConfig = new IOConfig();
    this.relConfig = new RelationshipConfig();
    this.inputFileCount = (int) ioConfig.countInputFiles();
    this.outputFileCount = (int) ioConfig.countOutputFiles();
    this.display =
        new FileProgressFrame(
            "Raw Organizer",
            inputFileCount + outputFileCount);
    display.setVisible(true);
  }

  public static void main(String[] args) {

    try {
      Organizer org = new Organizer();
      Set<Tag> tagLibrary = org.readTagLibrary();
      org.populateTemplates(tagLibrary);
      org.writeFailures(tagLibrary);
    } catch (Throwable t) {
      LOG.error(t.toString());
      t.printStackTrace();
      System.exit(1);
    }

    System.exit(0);
  }

  private void writeFailures(Set<Tag> tagLibrary) {
    Iterator<Tag> it = tagLibrary.iterator();
    while (it.hasNext()) {
      Tag root = it.next();

      String shortName = root.getArgument(1) + ".txt";
      TagComposer composer = null;
      try {
        composer =
            new RawTagComposer(
                ioConfig.buildBooleanWriter(shortName, false),
                shortName);
        composer.writeHeader(root);
        composer.writeTag(root);
      } catch (IOException e) {
        LOG.error(e.toString());
      } finally {
        try {
          composer.close();
        } catch (Exception e) {
          // no-op
        }
      }
    }
  }

  private Set<String> readTemplate(File file) {
    try {
      return Files
          .lines(file.toPath())
          .map(a -> a.trim())
          .collect(Collectors.toSet());
    } catch (IOException e) {
      LOG.error(e.toString());
    }

    return new TreeSet<String>();
  }

  public void populateTemplates(Set<Tag> tagLibrary) {

    File[] fileList = null;
    try {
      fileList = ioConfig.listOutputFiles();
    } catch (IOException e) {
      LOG.error(e.toString());
      return;
    }

    for (int i = 0; i < fileList.length; i++) {

      String shortName = FilenameUtils.getName(fileList[i].getName());
      display.set("reading " + shortName, inputFileCount + i);

      // read the template file
      TagArg1ChildFilter tagFilter =
          new TagArg1ChildFilter(readTemplate(fileList[i]));

      // try to find a tag library with matches for the template file
      Iterator<Tag> it = tagLibrary.iterator();
      Tag root = null;
      while (it.hasNext()) {
        root = tagFilter.extractMatchingChildren(it.next());
        if (root.getNumChildren() != 0) {
          break;
        }
      }

      if (root.getNumChildren() == 0) {
        LOG.warn("output file has no matches: " + shortName);
        continue;
      }

      display.set("writing " + shortName, inputFileCount + i + 1);

      TagComposer composer = null;
      try {
        composer =
            new RawTagComposer(
                ioConfig.buildBooleanWriter(shortName, false),
                shortName);
        composer.writeHeader(root);
        composer.writeTag(root);
      } catch (IOException e) {
        LOG.error(e.toString());
      } finally {
        try {
          composer.close();
        } catch (Exception e) {
          // no-op
        }
      }

    }

  }

  public Set<Tag> readTagLibrary() {
    // temporarily use a map so we can extract elements
    TreeMap<Tag, Tag> library = new TreeMap<Tag, Tag>(new TagArgComparator());

    File[] fileList = null;
    try {
      fileList = ioConfig.listInputFiles();
    } catch (IOException e) {
      LOG.error(e.toString());
      return library.keySet();
    }

    RelationshipMap relFileMap = relConfig.buildRelationshipMap();

    for (int i = 0; i < fileList.length; i++) {

      // set display
      String shortName = FilenameUtils.getName(fileList[i].getName());
      display.set("reading " + shortName, i + 1);

      // read and parse
      Tag root = null;
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
        } catch (IOException e) {
          // no-op
        }
      }

      if (library.containsKey(root)) {
        Tag winner = library.get(root);
        winner.copyChildren(root);
      } else {
        library.put(root, root);
      }
    }

    return library.keySet();
  }
}
