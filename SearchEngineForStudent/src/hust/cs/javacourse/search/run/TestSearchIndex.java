package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.query.*;
import hust.cs.javacourse.search.query.impl.*;
import hust.cs.javacourse.search.util.Config;

import java.io.*;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        IndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open(Config.INDEX_DIR + "index.dat");
        SimpleSorter sorter = new SimpleSorter();

        System.out.println("Please input the query or input Q/q to query:");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    
        String target;

        try{
            while( (target = bufferedReader.readLine()) != null && !target.equals("q") && !target.equals("Q") ){
                String[] split = target.split(" ");
                if(split.length == 1){
                    AbstractHit[] hits = indexSearcher.search(new Term(split[0]), sorter);
                    if(hits == null)
                        System.out.println("Target not found.");
                    else{
                        for(AbstractHit hit : hits){
                            System.out.println(hit.toString());
                        }
                    }
                    System.out.println("Please input the query or input Q/q to exit:");
                }
                else if(split.length == 2){
                    AbstractHit[] hits = indexSearcher.search(new Term(split[0]), new Term(split[1]), sorter);
                    if(hits == null)
                        System.out.println("Target not found.");
                    else{
                        for(AbstractHit hit : hits){
                            System.out.println(hit.toString());
                        }
                    }
                    System.out.println("Please input the query or input Q/q to exit:");
                }
                else if(split.length == 3){
                    AbstractHit[] hits = indexSearcher.search(new Term(split[0]), new Term(split[2]), sorter, AbstractIndexSearcher.LogicalCombination.valueOf(split[1]));
                    if(hits == null)
                        System.out.println("Target not found.");
                    else{
                        for(AbstractHit hit : hits){
                            System.out.println(hit.toString());
                        }
                    }
                    System.out.println("Please input the query or input Q/q to exit:");
                }
                else{
                    System.out.println("Invalid input.");
                    System.out.println("Please input the query or input Q/q to exit:");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
