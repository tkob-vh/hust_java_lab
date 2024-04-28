package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.query.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class IndexSearcher extends AbstractIndexSearch{

    public void open(String indexFile){
        index.load(new File(indexFile));
        index.optimize();
    }


    public abstract AbstractHit[] search(AbstractTerm, Sort sorter){
        AbstractPostingList postingList = index.search(queryTerm);
        if(postingList == null){
            return null;
        }
        
        List<AbstractHit> list = new ArrayList<>();
        for(int i = 0; i < postingList.size(); i++){
            AbstractPosting posting = postingList.get(i);
            String path = index.getDocName(posting.getDocId());
            HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
            map.put(queryTerm, posting);
            AbstractHit hit = new Hit(posting.getDocId(), path, map);
            hit.setScore(sorter.score(hit));
            list.add(hit);
        }
        sorter.sort(list);
    }

    public AbstractHit search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sorter sorter, LogicalCombination combine){

    }
}
