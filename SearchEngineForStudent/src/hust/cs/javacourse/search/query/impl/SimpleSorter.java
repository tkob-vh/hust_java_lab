package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.query.*;

import java.util.List;
import java.util.Map;

public class SimpleSorter implements Sort{


    /**
     * Sort the search result by the score of AbstractHit in descending order.
     * @author yyx
     * @param hits: the search result to be sorted
     */
    @Override
    public void sort(List<AbstractHit> hits){
        hits.sort((AbstractHit o1, AbstractHit o2) -> ((int)(score(o2) - score(o1))));
    }


    /**
     * Calculate the score of the AbstractHit. The score is the sum of the frequency of the term in the document.
     * @author yyx
     * @param hit: the hit to be scored
     * @return the score of the hit
     */
    @Override
    public double score(AbstractHit hit){
        double res = 0.0;
        for(Map.Entry<AbstractTerm, AbstractPosting> entry : hit.getTermPostingMapping().entrySet()){
            res += entry.getValue().getFreq();
        }
        return res;
    }
}
