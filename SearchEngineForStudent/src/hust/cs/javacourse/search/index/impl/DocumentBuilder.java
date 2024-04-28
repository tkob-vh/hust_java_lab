package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.*;
import hust.cs.javacourse.search.parse.impl.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DocumentBuilder extends AbstractDocumentBuilder {

    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream){
        List<AbstractTermTuple> list = new ArrayList<>();
        AbstractTermTuple tmp;
        while((tmp = termTupleStream.next()) != null){
            list.add(tmp);
        }
        termTupleStream.close();
        return new Document(docId, docPath, list);
    }

    public AbstractDocument build(int docId, String docPath, File file){
        BufferedReader reader;
        AbstractTermTupleStream scanner = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            scanner = new TermTupleScanner(reader);
            scanner = new LengthTermTupleFilter(new PatternTermTupleFilter(new StopWordTermTupleFilter(scanner)));
        }catch (Exception e){
            e.printStackTrace();
        }

        assert scanner != null;
        return build(docId, docPath, scanner);
    }
}
