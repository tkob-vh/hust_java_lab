package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.util.*;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class TermTupleScanner extends AbstractTermTupleScanner {

    private int position = 0;
    private List<String> buffer;
    private StringSplitter stringSplitter = new StringSplitter();

    public TermTupleScanner(){
        buffer = new ArrayList<>();
        stringSplitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
    }

    public TermTupleScanner(BufferedReader input){
        super(input);
        buffer = new ArrayList<>();
        stringSplitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
    }

    @Override
    public AbstractTermTuple next(){
        try{
            if(buffer.isEmpty()){
                String s;
                StringBuilder sb = new StringBuilder();
                while((s = input.readLine()) != null){
                    sb.append(s).append("\n");
                }
                s = sb.toString().trim();
                s = s.toLowerCase();
                buffer = stringSplitter.splitByRegex(s);
            }
            if(buffer.size() == 0)
                return null;
            AbstractTerm term = new Term(buffer.get(0));
            buffer.remove(0);
            return new TermTuple(term, position++);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close(){
        super.close();
    }


}


