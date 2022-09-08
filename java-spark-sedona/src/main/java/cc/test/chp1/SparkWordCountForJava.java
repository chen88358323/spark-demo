package cc.test.chp1;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.Thread.sleep;

public class SparkWordCountForJava {
    public static void main(String[] args) throws InterruptedException {

        // 初始化spark , local[*]：以*核心数在本地运行
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("SparkWordCountForJava");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaRDD<String> textFileRdd = jsc.textFile("/home/greatwall/CODE/bigdata/spark/README.md");

        // 将数据按照切分规则分成一个个单词
        JavaRDD<String> flatMapRdd = textFileRdd.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(String s) throws Exception {
                String[] splits = s.split("\t");
                List<String> list = Arrays.asList(splits);
                return list.iterator();
            }
        });

        // 每个单词作为key,value为1
        JavaRDD<Tuple2<String, Integer>> mapRdd = flatMapRdd.map(new Function<String, Tuple2<String, Integer>>() {
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        //  分组：相同 key 分为一组
        JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> groupByRdd = mapRdd.groupBy(new Function<Tuple2<String, Integer>, String>() {
            public String call(Tuple2<String, Integer> s) throws Exception {
                return s._1;
            }
        });

        // Lmbda 表达式写法 和 mapRdd 、 groupByRdd 值一样
        JavaRDD<Tuple2<String, Integer>> mapRdd1 = flatMapRdd.map(s -> new Tuple2<String, Integer>(s, 1));
        JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> groupByRdd1 = mapRdd1.groupBy(s -> s._1);


        // 相同key，value值累加
        JavaPairRDD<String, Integer> mapValuesRdd = groupByRdd.mapValues(new Function<Iterable<Tuple2<String, Integer>>, Integer>() {
            public Integer call(Iterable<Tuple2<String, Integer>> v1) throws Exception {
                int sum = 0;
                for(Tuple2<String, Integer> t:v1) {
                    sum += t._2;
                }
                return sum;
            }
        });
        // 行动算子：collect,将数据拉取到driver端
        List<Tuple2<String, Integer>> list = mapValuesRdd.collect();
        System.out.println(list);

        sleep(1000*120);
    }
}
