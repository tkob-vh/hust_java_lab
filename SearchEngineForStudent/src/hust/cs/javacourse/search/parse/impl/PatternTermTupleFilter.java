package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.util.Config;

import java.util.regex.Pattern;

public class PatternTermTupleFilter extends AbstractTermTupleFilter{

    private Pattern pattern = Pattern.compile(Config.TERM_FILTER_PATTERN);



    /**
     * Constructor with parameters.
     * @author yyx
     * @param input: the input stream.
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input){
        super(input);
    }


    /**
     * Constructor with parameters.
     * @author yyx
     * @param input: the input stream.
     * @param regex: the regex pattern.
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input, String regex){
        super(input);
        this.pattern = Pattern.compile(regex);
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
        while(!pattern.matcher(termTuple.term.getContent()).matches()){
            termTuple = input.next();
            if(termTuple == null)
                return null;
        }
        return termTuple;
    }
}
