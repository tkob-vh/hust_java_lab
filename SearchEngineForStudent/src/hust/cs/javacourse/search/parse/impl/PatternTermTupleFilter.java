package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.util.Config;

import java.util.regex.Pattern;

public class PatternTermTupleFilter extends AbstractTermTupleFilter{

    private Pattern pattern = Pattern.compile(Config.TERM_FILTER_PATTERN);

    public PatternTermTupleFilter(AbstractTermTupleStream input){
        super(input);
    }

    public PatternTermTupleFilter(AbstractTermTupleStream input, String regex){
        super(input);
        this.pattern = Pattern.compile(regex);
    }

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
