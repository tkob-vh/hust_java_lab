package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.util.*;

public class TermTupleScanner extends AbstractTermTupleScanner{

    private int position = 0;
    private List<String> buffer;
    private StringSplitter stringSplitter = new StringSplitter();

    public TermTupleScanner(){
        buffer = new ArrayList<>();
        stringSplitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
    }

    public TermTupleScanner(BufferdReader input){
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
                buf = stringSplitter.splitByRegex(s);
            }
            AbstractTerm term = new Term(buffer.get(position));
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


