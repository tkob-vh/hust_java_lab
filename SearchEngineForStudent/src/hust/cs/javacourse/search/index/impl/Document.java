package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.util.List;
import java.util.ArrayList;

public class Document extends AbstractDocument{

    public Document(){

    }

    public Document(int docId, String docPath){
        this.docId = docId;
        this.docPath = docPath;
    }

    public Document(int docId, String docPath, List<AbstractTermTuple> tuples){
        this.docId = docId;
        this.docPath = docPath;
        this.tuples = tuples;
    }

    public int getDocId(){
        return this.docId;
    }

    public void setDocId(int docId){
        this.docId = docId;
    }

    public String getDocPath(){
        return this.docPath;
    }

    public void setDocPath(String docPath){
        this.docPath = docPath;
    }

    public List<AbstractTermTuple> getTuples(){
        return this.tuples;
    }

    public void addTuple(AbstractTermTuple tuple){
        if(!this.tuples.contains(tuple)){
            this.tuples.add(tuple);
        }
    }

    public boolean contains(AbstractTermTuple tuple){
        return this.tuples.contains(tuple);
    }

    public AbstractTermTuple getTuple(int index){
        return this.tuples.get(index);
    }

    public int getTupleSize(){
        return this.tuples.size();
    }

    @Override
    public String toString(){
        return "Document Info: " +
                "docId = " + docId +
                ", docPath = " + docPath +
                ", tuples = " + tuples + "\n";
    }

}
