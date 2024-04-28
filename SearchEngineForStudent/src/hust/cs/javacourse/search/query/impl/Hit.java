package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.query.*;

import java.util.Map;

public class Hit extends AbstractHit {

    public Hit(){

    }

    public Hit(int docId, String docPath){
        super(docId, docPath);
    }

    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping){
        super(docId, docPath, termPostingMapping);
    }

    public int getDocId(){
        return this.docId;
    }

    public String getDocPath(){
        return this.docPath;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public double getScore(){
        return this.score;
    }

    public void setScore(double score){
        this.score = score;
    }

    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping(){
        return this.termPostingMapping;
    }

    @Override
    public String toString(){
        return "DocId: " + this.docId + "\nDocPath: " + this.docPath + "\nContent: " + this.content + "\nScore: " + this.score + "\nTermPostingMapping: " + this.termPostingMapping + "\n";
    }

    @Override
    public int compareTo(AbstractHit o){
        return (int)(this.score - o.getScore());
    }
}
