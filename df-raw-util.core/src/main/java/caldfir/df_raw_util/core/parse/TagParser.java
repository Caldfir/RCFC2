package caldfir.df_raw_util.core.parse;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import caldfir.df_raw_util.core.primitives.Tag;

public abstract class TagParser implements Closeable {

  private Scanner in;
  private int lineNum;

  private Tag nextTag;
  private Tag prevTag;

  public TagParser(Reader reader) {
    this.in = new Scanner(reader);
    this.lineNum = 0;
    this.prevTag = null;
    this.nextTag = null;
  }

  protected abstract Tag buildTag(String tagString);

  protected abstract Pattern getPattern();

  /**
   * Determines if the input stream contains any more Tags.
   */
  public boolean hasNext() {
    return peekNext() != null;
  }

  /**
   * Gives the value of the subsequent call to next().
   */
  public Tag peekNext() {
    if (nextTag == null) {
      nextTag = readTag();
    }

    return nextTag;
  }

  /**
   * Gives the value of the previous call to next().
   */
  public Tag peekPrev() {
    return prevTag;
  }

  /**
   * Obtains the next tag, and updates the position.
   */
  public Tag next() {
    prevTag = peekNext();
    nextTag = null;

    return prevTag;
  }

  /**
   * Obtains the next tag from the input stream.
   */
  protected Tag readTag() {
    String tagString = null;
    // the loop is a bit strange because we don't want to stop parsing just
    // because we're on the last line of the file
    try {
      for (; tagString == null; in.nextLine(), lineNum++) {
        tagString = in.findInLine(getPattern());
      }
    } catch (NoSuchElementException e) {
      return null;
    }

    return buildTag(tagString);
  }

  /**
   * Gives the current line number in the input stream.
   */
  public int getLineNum() {
    return lineNum;
  }

  /**
   * Closes the
   */
  public void close() throws IOException {
    in.close();
  }
}