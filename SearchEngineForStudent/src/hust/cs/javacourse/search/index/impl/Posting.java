package hust.cs.javacourse.search.index.impl;

import java.util.Objects;
import java.io.*;
import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Posting extends AbstractPosting
{
    public Posting(){
        super();
    }

    public Posting(int docId, int freq, List<Integer> positions){
        super(docId, freq, positions);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this)
            return true;
        else if(obj instanceof Posting){
            Posting posting = (Posting)obj;
            if(((Posting) obj).positions != null && this.positions != null)
                return this.positions.size() == posting.positions.size() && this.positions.containsAll(((Posting) obj).positions)
                 && this.docId == posting.docId && this.freq == posting.freq;
            else if(posting.positions == null && this.positions == null)
                return this.freq == posting.freq && this.docId == posting.docId;
        }
        return false;
    } 

    @Override
    public String toString(){
        return "DocId: " + this.docId + ", word freq: " + this.freq + "\n" + "    Word positions: " + this.positions.toString() + "\n";
    }

    public int getDocId(){
        return this.docId;
    }

    public void setDocId(int docId){
        this.docId = docId;
    }

    public int getFreq(){
        return this.freq;
    }

    public void setFreq(int freq){
        this.freq = freq;
    }

    public List<Integer> getPositions(){
        return this.positions;
    }

    public void setPositions(List<Integer> positions){
        this.positions = positions;
    }

    @Override
    public int compareTo(AbstractPosting o){
        return this.docId - o.getDocId();
    }

    public void sort(){
        Collections.sort(this.positions);
    }

    @Override
    public void writeObject(ObjectOutputStream oss){
        try{
            oss.writeObject(this.docId);
            oss.writeObject(this.freq);
            oss.writeObject(this.positions);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream ois){
        try{
            this.docId = (int) ois.readObject();
            this.freq = (int) ois.readObject();
            this.positions = (List<Integer>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
