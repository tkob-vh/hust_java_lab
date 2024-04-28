package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;

public class IndexBuilder extends AbstractIndexBuilder {
    private int docNum = 0;

    public IndexBuilder(AbstractDocumentBuilder docBuilder){
        super(docBulider);
    }

    @Override
    public AbstractIndex buildIndex(String rootDirectory){
        AbstractIndex index = new Index();
        for(String path : FileUtil.list(rootDirectory)){
            AbstractDocument document = docBuilder.build(docNum++, path, new File(path));
            index.addDocument(document);
        }

        return index;
    }
}
