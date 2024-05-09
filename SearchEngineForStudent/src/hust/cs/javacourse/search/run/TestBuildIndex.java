package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.util.Config;

import java.io.File;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        AbstractIndexBuilder indexBuilder = new IndexBuilder(new DocumentBuilder());
        System.out.println("Building index...");
        AbstractIndex index = indexBuilder.buildIndex(Config.DOC_DIR);
        index.optimize();
        System.out.println(index.toString());
        File file = new File(Config.INDEX_DIR + "index.dat");
        index.save(file);

        AbstractIndex index2 = new Index();
        index2.load(new File(Config.INDEX_DIR + "index.dat"));
        System.out.println("\n-------------------------\n");
        System.out.println(index2.toString());
    }
}
