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

    /**
     * Default constructor
     * @author yyx
     * @parem index: the index to be searched
     */
    @Override
    public void open(String indexFile) {
        index.load(new File(indexFile));
        index.optimize();
    }

    /**
     * Search for documents that contain the term and return the result in the form of AbstractHit
     * @author yyx
     * @param queryTerm: the term to be searched
     * @param sorter: the sorter to sort the result
     * @return: the result in the form of AbstractHit
     */
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

            if(sorter != null){
                double score = sorter.score(hit);
                hit.setScore(score);
            }
            // hit.setScore(sorter.score(hit));
            list.add(hit);
        }

        if(sorter != null){
            sorter.sort(list);
        }
        return list.toArray(new AbstractHit[0]);
    }


    /**
     * Search for documents that contain both/either of the two terms and return the result in the form of AbstractHit
     * @author yyx
     * @param queryTerm1: the first term to be searched
     * @param queryTerm2: the second term to be searched
     * @param sorter: the sorter to sort the result
     * @param combine: the logical combination of the two terms
     * @return the result in the form of AbstractHit
     */
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


    /**
     * Search for documents that contain the phrase and return the result in the form of AbstractHit
     * @author yyx
     * @param queryTerm1: the first term to be searched
     * @param queryTerm2: the second term to be searched
     * @param sorter: the sorter to sort the result
     * @return the result in the form of AbstractHit
     */
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter){
        AbstractPostingList postingList1 = index.search(queryTerm1);
        AbstractPostingList postingList2 = index.search(queryTerm2);
        
        if(postingList1 == null || postingList2 == null)
        {
            System.out.println("Can not find the phrase in the index, postingList is null");
            return null;
        }

        List<AbstractHit> list = new ArrayList<>();
        int i = 0, j = 0;
        while(i < postingList1.size() && j < postingList2.size()){
            AbstractPosting posting1 = postingList1.get(i);
            AbstractPosting posting2 = postingList2.get(j);

            int docId1 = posting1.getDocId();
            int docId2 = posting2.getDocId();
            
            if(docId1 == docId2 && docId1 != -1){
                List<Integer> position1 = posting1.getPositions();
                List<Integer> position2 = posting2.getPositions();

                List<Integer> curPosition = new ArrayList<>();
                
                int m = 0, n = 0;
                while(m < position1.size() && n < position2.size()){
                    int curPos1 = position1.get(m);
                    int curPos2 = position2.get(n);

                    if(curPos2 - curPos1 == 1){
                        curPosition.add(curPos1);
                        m++; n++;
                    }
                    else if(curPos1 < curPos2) m++;
                    else n++;
                }
                if(!curPosition.isEmpty()){
                    String path = index.getDocName(docId1);
                    HashMap<AbstractTerm, AbstractPosting> map = new HashMap<>();

                    map.put(new Term(queryTerm1.getContent() + " " + queryTerm2.getContent()), new Posting(docId1, curPosition.size(),curPosition));

                    AbstractHit hit = new Hit(docId1, path, map);
                    hit.setScore(sorter.score(hit));
                    list.add(hit);
                }
                i++; j++;
            }
            else if(docId1 < docId2) i++;
            else j++;
        }
        sorter.sort(list);
        return list.toArray(new AbstractHit[0]);
    }
}
