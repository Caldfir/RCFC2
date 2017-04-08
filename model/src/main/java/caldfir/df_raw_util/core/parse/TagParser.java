package caldfir.df_raw_util.core.parse;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import caldfir.df_raw_util.core.primitives.TagNode;

public abstract class TagParser implements Closeable {

  // used for logging purposes
  private String sourceName;
  private int lineNum;

  // data source
  private final Scanner in;

  // tag position tracking
  private TagNode nextTag;
  private TagNode prevTag;

  public TagParser(File file) throws FileNotFoundException {
    this(new BufferedReader(new FileReader(file)), file.getName());
  }

  public TagParser(Reader reader, String sourceName) {
    this.in = new Scanner(reader);
    this.sourceName = sourceName;
    this.lineNum = 0;
    this.prevTag = null;
    this.nextTag = null;
  }

  protected abstract TagNode buildTag(String tagString);

  protected abstract Pattern getPattern();

  /**
   * Read the entire input stream, and build a hierarchy of tags from it.
   */
  public TagNode parse() {
    TagNode root = next();
    while (hasNext()) {
      next();
    }
    return root;
  }

  /**
   * Determines if the input stream contains any more Tags.
   */
  public boolean hasNext() {
    return peekNext() != null;
  }

  /**
   * Gives the value of the subsequent call to next().
   */
  public TagNode peekNext() {
    if (nextTag == null) {
      nextTag = readTag();
    }

    return nextTag;
  }

  /**
   * Gives the value of the previous call to next().
   */
  public TagNode peekPrev() {
    return prevTag;
  }

  /**
   * Obtains the next tag, and updates the position.
   */
  public TagNode next() {
    prevTag = peekNext();
    nextTag = null;

    return prevTag;
  }

  /**
   * Obtains the next tag from the input stream.
   */
  protected TagNode readTag() {
    TagNode tag = null;
    // the loop is a bit strange because we don't want to stop parsing just
    // because we're on the last line of the file
    try {
      while (tag == null) {
        String found = in.findInLine(getPattern());
        if (found != null) {
          tag = buildTag(found);
          break;
        }
        in.nextLine();
        lineNum++;
      }
    } catch (NoSuchElementException e) {
      return null;
    }

    return tag;
  }

  protected void setNextTag(TagNode nextTag) {
    this.nextTag = nextTag;
  }

  protected void setPrevTag(TagNode prevTag) {
    this.prevTag = prevTag;
  }

  /**
   * Gives the current line number in the input stream.
   */
  public int getLineNum() {
    return lineNum;
  }
  
  /**
   * Gives a string representing the data source.
   */
  public String toString() {
    return sourceName;
  }

  /**
   * Closes the underlying input.
   */
  @Override
  public void close() throws IOException {
    in.close();
  }
}
