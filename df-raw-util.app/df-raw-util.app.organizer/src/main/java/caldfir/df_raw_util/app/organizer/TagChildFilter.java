package caldfir.df_raw_util.app.organizer;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import caldfir.df_raw_util.core.primitives.Tag;

public class TagChildFilter {

  
  protected Tag selectChildren(Tag root, Predicate<Tag> p) {
    Stream<Tag> childStream = 
        Stream.generate(new TagChildSupplier(root));
    
    Tag result = new Tag();
    result.copyArgs(root);
    
    result.addChildren(
        childStream.filter(p).collect(Collectors.toList()));
    
    return result;
  }
}
