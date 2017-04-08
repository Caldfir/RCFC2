package caldfir.df_raw_util.app.organizer;

import java.util.Comparator;

import caldfir.df_raw_util.core.primitives.TagNode;

public class TagArgComparator implements Comparator<TagNode> {

    public int compare(TagNode t1, TagNode t2) {
      // check pointer equality
      if( t1 == t2 ) {
        return 0;
      }
      
      // check argument count
      int argDiff = t1.getNumArguments() - t2.getNumArguments();
      if( argDiff != 0 ){
        return argDiff;
      }
      
      // look for the first argument that doesn't match
      int argNum = t1.getNumArguments();
      for(int i = 0; i < argNum; i++) {
        int argComp = t1.getArgument(i).compareTo(t2.getArgument(i));
        if( argComp != 0 ) {
          return argComp;
        }
      }

      // these tags match
      return 0;
    }
}
