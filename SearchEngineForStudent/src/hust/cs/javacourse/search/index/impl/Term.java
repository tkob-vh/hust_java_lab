package hust.cs.javacourse.search.index.impl;

import java.util.Objects;
import java.io.*;
import hust.cs.javacourse.search.index.*;


public class Term extends AbstractTerm implements Comparable<AbstractTerm>, FileSerializable
{
    public Term()
    {
        super();
    }

    public Term(String content)
    {
        super(content);
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof AbstractTerm)
        {
            AbstractTerm term = (AbstractTerm) obj;
            return Objects.equals(this.content, term.getContent());
        }
        return false;
    }

    @Override
    public String toString()
    {
        System.out.println(this.content);
        return this.content;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public int compareTo(AbstractTerm o)
    {
        return this.content.compareTo(o.getContent());
    }

    @Override
    public void writeObject(ObjectOutputStream oss){
        try{
            oss.writeObject(this.content);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream ois){
        try{
            this.content = (String) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
