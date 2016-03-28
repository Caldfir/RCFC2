package caldfir.df_raw_util.core.parse;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.primitives.Tag;


public class RawIterator extends TagIterator{

  private static final Logger LOG = 
      LoggerFactory.getLogger(TagIterator.class);

	private FileReader reader;
	private Scanner in;
	private static Pattern p = Pattern.compile("(\\[)(([^\\]\\[])*)(\\])");

	public RawIterator(String filename) throws FileNotFoundException{
		reader = new FileReader(filename);
		in = new Scanner(reader);
	}

	@Override
	public Tag next() {
		String t = null;
		t = in.findInLine(p);
		while(t == null){
			try{
				in.nextLine();
				lineNum++;
			} 
			catch (NoSuchElementException e){
				return null;
			}
			t = in.findInLine(p);
		}
		return Tag.rawTag(t);
	}

	@Override
	public void close() {
		try {
			in.close();
			reader.close();
			
		} catch (IOException e) {
			LOG.error("failure in attempting to close file: " + reader);
		}
	}
}
