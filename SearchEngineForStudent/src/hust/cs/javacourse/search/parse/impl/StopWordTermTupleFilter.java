package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.util.StopWords;

import java.util.List;
import java.util.Arrays;

public class StopWordTermTupleFilter extends AbstractTermTupleFilter{

    private List<String> stopWords = Arrays.asList(StopWords.STOP_WORDS);


    public StopWordTermTupleFilter(AbstractTermTupleStream input){
        super(input);
    }

    @Override
    public AbstractTermTuple next(){
        AbstractTermTuple termTuple = input.next();
        if(termTuple == null)
            return null;
        while(stopWords.contains(termTuple.term.getContent())){
            termTuple = input.next();
            if(termTuple == null)
                return null;
        }
        return termTuple;
    }
}

