package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.query.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class IndexSearcher extends AbstractIndexSearcher{

    public void open(String indexFile){
        index.load(new File(indexFile));
        index.optimize();
    }


    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter){
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

    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sorter sorter, LogicalCombination combine){
        AbstractPostingList postingList1 = index.search(queryTerm1);
        AbstractPostingList postingList2 = index.search(queryTerm2);
        if(postingList1 == null || postingList2 == null){
            return null;
        }

        List<AbstractHit> list = new ArrayList<>();
        int len1 = postingList1.size();
        int len2 = postingList2.size();
        int i = 0, j = 0;
        Posting tmp = new Posting(-1, -1, null);

        while(i < len1 || j < len2){
            AbstractPosting posting1 = i < len1 ? postingList1.get(i) : tmp; 
            AbstractPosting posting2 = j < len2 ? postingList2.get(j) : tmp;

            int docId1 = posting1.getDocId();
            int docId2 = posting2.getDocId();

            if(docId1 == docId2 && docId1 != -1){
                String path = index.getDocName(docId1);
                HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                map.put(queryTerm1, posting1);
                map.put(queryTerm2, posting2);
                AbstractHit hit = new Hit(docId1, path, map);
                hit.setScore(sorter.score(hit));
                list.add(hit);
                i++;
                j++;
            }
            else if(docId1 < docId2 && docId1 != -1){
                if(combine == LogicalCombination.OR){
                    String path = index.getDocName(docId1);
                    HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                    map.put(queryTerm1, posting1);
                    AbstractHit hit = new Hit(docId1, path, map);
                    hit.setScore(sorter.score(hit));
                    list.add(hit);
                }
                i++;

            }
        }
    }


}
