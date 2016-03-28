package caldfir.df_raw_util.core.parse;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.primitives.Tag;


public class XIterator extends TagIterator{

  private static final Logger LOG = 
      LoggerFactory.getLogger(TagIterator.class);

	private FileReader reader;
	private Scanner in;
	private static Pattern p = Pattern.compile("(<)(([^><])*)(>)");
	
	public XIterator(String filename) throws FileNotFoundException{
		reader = new FileReader(filename);
		in = new Scanner(reader);
		in.useDelimiter("<");
	}

	@Override
	public Tag next() {
		String t = null;
		if(in.hasNext()){
			t = in.findInLine(p);
			if(in.hasNextLine()){
				in.nextLine();
				lineNum++;
			}
			return Tag.xTag(t);
		}
		return null;
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
