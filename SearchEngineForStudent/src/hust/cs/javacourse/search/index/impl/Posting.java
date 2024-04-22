package hust.cs.javacourse.search.index.impl;

import java.util.Objects;
import java.io.*;
import hust.cs.javacourse.search.index.AbstractTermTuple;

public class Posting extends AbstractPosting,implements Comparable<AbstractPosting>, FileSerializable
{
    public Posting(){
        super();
    }

    public Posting(int docId, int freq, List<Integer> positions){
        super(docId, freq, positions);
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof AbstractPosting)
        {
            AbstractPosting posting = (AbstractPosting) obj;
            return this.docId == posting.docId && this.freq == posting.freq && Objects.equals(this.positions, posting.positions);
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
