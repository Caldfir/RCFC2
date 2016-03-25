package caldfir.df_raw_util.core.parsers;

import caldfir.df_raw_util.core.primitives.Tag;

public abstract class TagIterator {

	protected int lineNum = 0;
	
	public abstract Tag next();
	
	public int getLine(){
		return lineNum;
	}
	
	public abstract void close();
	
}
