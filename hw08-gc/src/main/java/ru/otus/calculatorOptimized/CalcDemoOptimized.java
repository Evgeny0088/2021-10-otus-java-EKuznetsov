package ru.otus.calculatorOptimized;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

import java.io.File;
import java.time.LocalDateTime;

import static ru.Utils.LogInformation.writeInfoToLog;

public class CalcDemoOptimized {

    /*
    creates new log file for speed results
     */
    private static final File file = new File("hw08-gc/src/main/resources/CalculatorOptimizedLog.log");

    public static void main(String[] args) {
        long counter = 100_000_000;
        var summator = new SummatorOptimized();
        long startTime = System.currentTimeMillis();
        DataOptimized data = new DataOptimized(0);
        for (int idx = 0; idx < counter; idx++) {
            data.setValue(idx);
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
        String info = "\nResults for 150m heap memory of CalcDemoOptimized class:\n" +
                "spend msec:" + delta + ", sec:" + (delta / 1000) + "\n";
        writeInfoToLog(file,info);

    }
}
