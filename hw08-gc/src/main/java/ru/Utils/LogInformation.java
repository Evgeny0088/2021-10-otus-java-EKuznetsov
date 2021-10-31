package ru.Utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogInformation {

    private static final DateTimeFormatter ftm = DateTimeFormatter.ofPattern("yyyy-MM-dd' Time: 'HH:mm:ss");
    private static final String logTime = LocalDateTime.now().format(ftm);

    public static void main(String[] args) {
        writeInfoToLog(new File("hw08-gc/src/main/resources/CalculatorLog.log"),laptopInfo());
    }

    public static String laptopInfo(){
        String delimiter = "\n";
        return delimiter + "Laptop properties:" + delimiter
                + "OS name : Linux Ubuntu 20.04.3 LTS" + delimiter +
                "Architecture : x86_64"  + delimiter +
                "CPU op-mode(s) : 32-bit, 64-bit"  + delimiter +
                "Available processors (cores) : 8" + delimiter +
                "RAM total : 7,4Gi" + delimiter +
                "CPU model : Intel(R) Core(TM) i5-10210U CPU @ 1.60GHz" + delimiter;
    }

    public static void writeInfoToLog(File file, String info){
        try{
            if (!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
            bufferedWriter.write(logTime);
            bufferedWriter.write(info);
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
