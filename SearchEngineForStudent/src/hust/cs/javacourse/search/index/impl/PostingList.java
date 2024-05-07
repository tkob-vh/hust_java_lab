package hust.cs.javacourse.search.index.impl;

import java.util.Objects;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;


public class PostingList extends AbstractPostingList{

    public void add(AbstractPosting posting){
        if(list.indexOf(posting) == -1){
            list.add(posting);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(AbstractPosting posting : list){
            sb.append(posting.toString() + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");

        return sb.toString();
    }

    public void add(List<AbstractPosting> postings){
        for(AbstractPosting posting : postings){
            if(list.indexOf(posting) == -1){
                list.add(posting);
            }
        }
    }

    public AbstractPosting get(int index){
        return list.get(index);
    }

    public int indexOf(AbstractPosting posting){
        return list.indexOf(posting);
    }

    public int indexOf(int docId){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getDocId() == docId){
                return i;
            }
        }
        return -1;
    }

    public boolean contains(AbstractPosting posting){
        return list.contains(posting);
    }

    public void remove(int index){
        list.remove(index);
    }


    public void remove(AbstractPosting posting){
        list.remove(posting);

    }

    public int size(){
        return list.size();
    }

    public void clear(){
        list.clear();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void sort(){
        list.sort(new Comparator<AbstractPosting>(){
            @Override
            public int compare(AbstractPosting p1, AbstractPosting p2){
                return p1.compareTo(p2);
            }
        });
    }

    @Override
    public void writeObject(ObjectOutputStream out){
        try{
            out.writeObject(list);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream in){
        try{
            list = (List<AbstractPosting>) in.readObject();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

}


