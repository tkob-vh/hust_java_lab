package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.util.Config;


public class LengthTermTupleFilter extends AbstractTermTupleFilter {


    /**
     * Constructor with parameters.
     * @author yyx
     * @param input: the input stream.
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input){
        super(input);
    }



    /**
     * Get the next AbstractTermTuple in the stream.
     * @author yyx
     * @return the next AbstractTermTuple in the stream, return null if reach the end of the stream
     */
    @Override
    public AbstractTermTuple next(){
        AbstractTermTuple termTuple = input.next();
        if(termTuple == null)
            return null;
        while(termTuple.term.getContent().length() < Config.TERM_FILTER_MINLENGTH || termTuple.term.getContent().length() > Config.TERM_FILTER_MAXLENGTH){
            termTuple = input.next();
            if(termTuple == null)
                return null;
        }
        return termTuple;
    }
}
