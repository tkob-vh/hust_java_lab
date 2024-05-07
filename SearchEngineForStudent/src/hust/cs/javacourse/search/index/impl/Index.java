package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;

import java.io.*;
import java.util.*;


public class Index extends AbstractIndex{
    

    @Override
    public String toString(){
        if(docIdToDocPathMapping.size() == 0 && termToPostingListMapping.size() == 0){
            return "Empty Index";
        }
        else{
            return "docIdToDocPathMapping:\n" + docIdToDocPathMapping.toString() + "\n" + "termToPostingListMapping:\n" + termToPostingListMapping.toString() + "\n";
        }
    }

    public void addDocument(AbstractDocument document){
        HashMap<AbstractTerm,List<Integer>> map = new HashMap<>();
        for(AbstractTermTuple termTuple : document.getTuples()){
            if(!map.containsKey(termTuple.term)){
                map.put(termTuple.term, new ArrayList<>());
                map.get(termTuple.term).add(termTuple.curPos);
            }
            else
                map.get(termTuple.term).add(termTuple.curPos);
        }

        for(Map.Entry<AbstractTerm,List<Integer>> entry : map.entrySet()){
            if(!this.termToPostingListMapping.containsKey(entry.getKey()))
                termToPostingListMapping.put(entry.getKey(), new PostingList());
            termToPostingListMapping.get(entry.getKey()).add(new Posting(document.getDocId(), entry.getValue().size(), entry.getValue()));
        docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        }
    }

    @Override
    public void load(File file){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            readObject(objectInputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file){
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            writeObject(objectOutputStream);
        }catch (IOException e){
            e.printStackTrace();
        } 
    }

    @Override
    public AbstractPostingList search(AbstractTerm term){
        return termToPostingListMapping.getOrDefault(term, null);
    }

    @Override
    public Set<AbstractTerm> getDictionary(){
        return termToPostingListMapping.keySet();
    }

    @Override
    public void optimize(){
        for(Map.Entry<AbstractTerm, AbstractPostingList> entry : termToPostingListMapping.entrySet()){
            entry.getValue().sort();
            for(int i = 0; i < entry.getValue().size(); i++){
                Collections.sort(entry.getValue().get(i).getPositions());
            }
        }
    }

    @Override
    public String getDocName(int docId){
        return docIdToDocPathMapping.getOrDefault(docId, null);
    }

    @Override
    public void writeObject(ObjectOutputStream out){
        try{
            out.writeObject(docIdToDocPathMapping);
            out.writeObject(termToPostingListMapping);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream in){
        try{
            docIdToDocPathMapping = (Map<Integer, String>) in.readObject();
            termToPostingListMapping = (Map<AbstractTerm, AbstractPostingList>) in.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
