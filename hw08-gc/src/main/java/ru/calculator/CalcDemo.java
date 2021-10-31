package ru.calculator;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=hw08-gc/src/main/resources/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

import static ru.Utils.LogInformation.writeInfoToLog;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class CalcDemo {

    /*
    creates new log file for speed results
     */
    private static final File file = new File("hw08-gc/src/main/resources/CalculatorLog.log");

    public static void main(String[] args) throws IOException {
        long counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();
        for (var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);
            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }
        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));

        /*
        log writing below
         */
        String info = "\nResults for 500m heap memory of CalcDemo class:\n" +
                        "spend msec:" + delta + ", sec:" + (delta / 1000) + "\n";
        writeInfoToLog(file,info);
    }
}
