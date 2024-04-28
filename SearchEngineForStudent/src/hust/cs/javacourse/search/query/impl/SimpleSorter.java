package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.query.*;

import java.util.List;
import java.util.Map;

public class SimpleSorter implements Sort{

    @Override
    public void sort(List<AbstractHit> hits){
        hits.sort((AbstractHit o1, AbstractHit o2) -> ((int)(score(o2) - score(o1))));
    }

    @Override
    public double score(AbstractHit hit){
        double res = 0.0;
        for(Map.Entry<AbstractTerm, AbstractPosting> entry : hit.getTermPostingMapping().entrySet()){
            res += entry.getValue().getFreq();
        }
        return res;
    }
}
