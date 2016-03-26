package caldfir.df_raw_util.app.organizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.parsers.TreeBuilder;
import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipFileLookup;
import caldfir.df_raw_util.ui.FileProgressFrame;

public class Organizer {

  private static final Logger LOG = LoggerFactory.getLogger(Organizer.class);

  final static String SRC_DIR_NAME = "in";
  final static String DST_DIR_NAME = "out";

  final static String READABLE_EXT = "txt";

  private static class TagSecondArgStringComparator implements Comparator<Tag> {

    public int compare(Tag t1, Tag t2) {
      if (t1.tagLength() > 1 && t2.tagLength() > 1) {
        return (t1.getArgument(1)).compareTo(t2.getArgument(1));
      }
      return t1.tagLength() - t2.tagLength();
    }

  }

  public static void main(String[] args) {

    try {
      // read all the tags into a master tag library
      Tag libraryRoot = null;
      try {
        libraryRoot = readTagLibrary(SRC_DIR_NAME, READABLE_EXT);
      } catch (IOException e) {
        LOG.error("problem encountered attempting to read input files");
      }

      // write all tags
      try {
        populateTemplates(DST_DIR_NAME, READABLE_EXT, libraryRoot);
      } catch (IOException e) {
        LOG.error("problem encountered attempting to write output files");
      }
    } catch (Throwable t) {
      LOG.error(t.toString());
      System.exit(1);
    }

    System.exit(0);
  }

  private static void populateTemplates(
      String dstDirName,
      String readableExt,
      Tag libraryRoot) throws IOException {

    TagSecondArgStringComparator tagComp = new TagSecondArgStringComparator();
    libraryRoot.sortChildren(tagComp);

    File dstDir = new File(dstDirName);
    if (!dstDir.exists() || !dstDir.isDirectory()) {
      LOG.error("input directory is missing: " + dstDirName);
      throw new FileNotFoundException(dstDirName);
    }

    File[] fileList = dstDir.listFiles();

    FileProgressFrame display =
        new FileProgressFrame("Populating Output Files", 2 * fileList.length);
    display.setVisible(true);

    for (int i = 0; i < fileList.length; i++) {

      String iBaseName = FilenameUtils.getName(fileList[i].getName());
      String iExtension = FilenameUtils.getExtension(iBaseName);

      display.set("reading " + iBaseName, 2 * i);

      if (!fileList[i].canRead() || !iExtension.equals(readableExt)) {
        LOG.info("skipped unreadable file: " + iBaseName);
        continue;
      }

      List<String> iTagNames =
          Files.readAllLines(fileList[i].toPath(), Charset.defaultCharset());

      Tag iRoot = libraryRoot.clone();

      ListIterator<String> iTagNamesIter = iTagNames.listIterator();
      while (iTagNamesIter.hasNext()) {
        String jTagName = iTagNamesIter.next().trim();

        ArrayList<String> jKey = new ArrayList<String>(2);
        jKey.add(null);
        jKey.add(jTagName);
        Tag jTag = null;
        try {
          jTag = libraryRoot.binarySearchChildren(jKey, tagComp);
        } catch (NoSuchElementException e) {
          LOG.warn("unable to locate element in input files: " + jTagName);
          iTagNamesIter.remove();
          continue;
        }

        iRoot.addChild(jTag);
      }

      display.set("writing " + iBaseName, 2 * i + 1);

      FileWriter iWrite = null;
      PrintWriter iPrint = null;
      try {
        iWrite = new FileWriter(fileList[i], false);
        iPrint = new PrintWriter(iWrite);

        // print stuff to the file
        iPrint.print(iBaseName + "\n\n");
        ListIterator<String> iTagOutIter = iTagNames.listIterator();
        while (iTagOutIter.hasNext()) {
          iPrint.print("\t" + iTagOutIter.next() + "\n");
        }
        iPrint.print("\n");
        iPrint.print(iRoot.toRawString());
      } catch (IOException e) {
        LOG.error("unable to write: " + iBaseName);
        continue;
      } finally {
        iWrite.close();
        iPrint.close();
      }
    }

    display.setVisible(false);
  }

  private static Tag readTagLibrary(String srcDirName, String readableExt)
      throws IOException {

    File inDir = new File(srcDirName);
    if (!inDir.exists() || !inDir.isDirectory()) {
      LOG.error("input directory is missing: " + srcDirName);
      throw new FileNotFoundException(srcDirName);
    }

    File[] fileList = inDir.listFiles();

    FileProgressFrame display =
        new FileProgressFrame("Reading Tags", fileList.length);
    display.setVisible(true);

    Tag libraryRoot = null;
    RelationshipFileLookup relFileMap = new RelationshipFileLookup();
    
    for (int i = 0; i < fileList.length; i++) {

      String iBaseName = FilenameUtils.getName(fileList[i].getName());
      String iExtension = FilenameUtils.getExtension(fileList[i].getName());

      display.set("reading " + iBaseName, i);

      if (!fileList[i].canRead() || !iExtension.equals(readableExt)) {
        LOG.info("skipped unreadable file: " + iBaseName);
        continue;
      }

      TreeBuilder iBuildTrees = null;
      try {
        iBuildTrees = new TreeBuilder(fileList[i].getAbsolutePath(), relFileMap);
      } catch (IOException e) {
        LOG.error("unable to parse: " + iBaseName);
        continue;
      }

      Tag iRootTag = iBuildTrees.getRoot();
      if (iRootTag == null) {
        LOG.error("parsed file model is missing: " + iBaseName);
        continue;
      }

      if (libraryRoot == null) {
        libraryRoot = iRootTag.clone(true);
      } else {
        libraryRoot.copyChildren(iRootTag);
      }
    }

    display.setVisible(false);

    if (libraryRoot == null) {
      LOG.error("no input files contained any tags");
      throw new IOException(srcDirName);
    }

    return libraryRoot;
  }
}
