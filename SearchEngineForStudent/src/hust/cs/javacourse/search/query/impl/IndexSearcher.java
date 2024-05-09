package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.query.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class IndexSearcher extends AbstractIndexSearcher {

    @Override
    public void open(String indexFile) {
        index.load(new File(indexFile));
        index.optimize();
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter){
        AbstractPostingList postingList = index.search(queryTerm);
        if(postingList == null)
        {
            System.out.println("Can not find the term in the index, postingList is null");
            return null;
        }

        List<AbstractHit> list = new ArrayList<>();
        for(int i = 0; i < postingList.size(); i++){
            AbstractPosting posting = postingList.get(i); // iterate the posting list
            String path = index.getDocName(posting.getDocId()); // Get the path for each doc in the posting list
            
            HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
            map.put(queryTerm, posting);
            AbstractHit hit = new Hit(posting.getDocId(), path, map);
            hit.setScore(sorter.score(hit));
            list.add(hit);
        }

        return list.toArray(new AbstractHit[0]);
    }


    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine){
        AbstractPostingList postingList1 = index.search(queryTerm1);
        AbstractPostingList postingList2 = index.search(queryTerm2);

        // if both terms can not be found in the index
        if(postingList1 == null && postingList2 == null)
        {
            System.out.println("Both terms can not be found in the index, postingList is null");
            return null;
        }

        // if one of the terms can not be found in the index
        else if(postingList1 == null || postingList2 == null)
        {
            if(combine == LogicalCombination.AND)
            {
                System.out.println("One of the terms can not be found in the index, postingList is null");
                return null;
            }
            else if(combine == LogicalCombination.OR){
                if(postingList1 == null) return search(queryTerm2, sorter);
                else return search(queryTerm1, sorter);
            }
        }

        
        // if both terms can be found in the index
        List<AbstractHit> list = new ArrayList<>();

        if(combine == LogicalCombination.AND){
            int i = 0, j = 0;
            while(i < postingList1.size() && j < postingList2.size()){
                AbstractPosting posting1 = postingList1.get(i);
                AbstractPosting posting2 = postingList2.get(j);

                if(posting1.getDocId() == posting2.getDocId()){
                    String path = index.getDocName(posting1.getDocId());

                    HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                    map.put(queryTerm1, posting1);
                    map.put(queryTerm2, posting2);

                    AbstractHit hit = new Hit(posting1.getDocId(), path, map);
                    hit.setScore(sorter.score(hit));
                    list.add(hit);
                    i++; j++;
                }
                else if(posting1.getDocId() < posting2.getDocId()) i++;
                else j++;
            }
        }
        else if(combine == LogicalCombination.OR){
            int i = 0, j = 0;

            while(i < postingList1.size() && j < postingList2.size()){
                AbstractPosting posting1 = postingList1.get(i);
                AbstractPosting posting2 = postingList2.get(j);

                if(posting1.getDocId() == posting2.getDocId()){
                    String path = index.getDocName(posting1.getDocId());

                    HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                    map.put(queryTerm1, posting1);
                    map.put(queryTerm2, posting2);

                    AbstractHit hit = new Hit(posting1.getDocId(), path, map);
                    hit.setScore(sorter.score(hit));
                    list.add(hit);
                    i++; j++;
                }
                else if(posting1.getDocId() < posting2.getDocId()){
                    String path = index.getDocName(posting1.getDocId());

                    HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                    map.put(queryTerm1, posting1);

                    AbstractHit hit = new Hit(posting1.getDocId(), path, map);
                    hit.setScore(sorter.score(hit));
                    list.add(hit);
                    i++;
                }
                else if(posting1.getDocId() > posting2.getDocId()){
                    String path = index.getDocName(posting2.getDocId());

                    HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                    map.put(queryTerm2, posting2);

                    AbstractHit hit = new Hit(posting2.getDocId(), path, map);
                    hit.setScore(sorter.score(hit));
                    list.add(hit);
                    j++;
                }
            }
            while(i < postingList1.size()){
                AbstractPosting posting1 = postingList1.get(i);
                String path = index.getDocName(posting1.getDocId());
                
                HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                map.put(queryTerm1, posting1);

                AbstractHit hit = new Hit(posting1.getDocId(), path, map);
                hit.setScore(sorter.score(hit));

                list.add(hit);
                i++;
            }
            while(j < postingList2.size()){
                AbstractPosting posting2 = postingList2.get(j);
                String path = index.getDocName(posting2.getDocId());

                HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();
                map.put(queryTerm2, posting2);

                AbstractHit hit = new Hit(posting2.getDocId(), path, map);
                hit.setScore(sorter.score(hit));

                list.add(hit);
                j++;
            }

        }



        return list.toArray(new AbstractHit[0]);
    }
}
