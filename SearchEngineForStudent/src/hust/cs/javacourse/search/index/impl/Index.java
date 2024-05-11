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
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<Integer, String> entry : docIdToDocPathMapping.entrySet()){
                sb.append("docId: " + entry.getKey() + ", docPath: " + entry.getValue() + "\n");
            }
            sb.append("\n");
            for(Map.Entry<AbstractTerm, AbstractPostingList> entry : termToPostingListMapping.entrySet()){
                sb.append("term: " + entry.getKey() + " ----> posting: " + entry.getValue() + "\n");
            }
            sb.append("\n");

            return sb.toString();
        }
    }

    /**
     * Add a document to the index.
     * @author yyx
     * @param document: the document to be added.
     */
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

    /**
     * Load the index from the index file which have been saved before.
     * @author yyx
     * @param file: the index file to be loaded.
     */
    @Override
    public void load(File file){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            readObject(objectInputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Write the index to the index file.
     * @author yyx
     * @param file: the index file to be written.
     */
    @Override
    public void save(File file){
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            writeObject(objectOutputStream);
        }catch (IOException e){
            e.printStackTrace();
        } 
    }

    /**
     * Return the posting list of a specific term.
     * @author yyx
     * @param term: the term to be searched.
     * @return the posting list of the term.
     */
    @Override
    public AbstractPostingList search(AbstractTerm term){
        return termToPostingListMapping.getOrDefault(term, null);
    }

    /**
     * Return the dictionary of the index, which is a set of terms in the index.
     * @author yyx
     * @return the dictionary of the index.
     */
    @Override
    public Set<AbstractTerm> getDictionary(){
        return termToPostingListMapping.keySet();
    }

    /**
     * Optimize the index, including sorting the postingList according to their docId, and sorting the position list of each posting.
     * @author yyx
     */
    @Override
    public void optimize(){
        for(Map.Entry<AbstractTerm, AbstractPostingList> entry : termToPostingListMapping.entrySet()){
            entry.getValue().sort(); // Sort the postingList by the docId of each posting.
            for(int i = 0; i < entry.getValue().size(); i++){
                Collections.sort(entry.getValue().get(i).getPositions()); // Sort the positions of each posting.
            }
        }
    }

    @Override
    public String getDocName(int docId){
        return docIdToDocPathMapping.getOrDefault(docId, null);
    }

    /**
     * Write the object to the output stream.
     * @auther yyx
     */
    @Override
    public void writeObject(ObjectOutputStream out){
        try{
            out.writeObject(docIdToDocPathMapping);
            out.writeObject(termToPostingListMapping);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Read the object from the input stream.
     * @author yyx
     */
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
