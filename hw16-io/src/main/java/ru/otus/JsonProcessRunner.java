package ru.otus;

import ru.otus.dataprocessor.FileSerializer;
import ru.otus.dataprocessor.ProcessorAggregator;
import ru.otus.dataprocessor.ResourcesFileLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JsonProcessRunner {
    public static void main(String[] args) throws IOException {

        var inputFileName = "inputData.json";
        var outputDataFileName = "outputData.json";
        var resources = new ResourcesFileLoader(inputFileName);
        var processor = new ProcessorAggregator();
        Map<String, Double> processed = processor.process(resources.load());

        // print out processed data
        System.out.println("data after process:");
        processed.forEach((k,v)-> System.out.println(k + " : " + v.toString()));

        // saving json into new file
        var serializer = new FileSerializer(outputDataFileName);
        serializer.serialize(processed);

        // reading and printing json from output file
        var serializedOutput = Files.readString(Paths.get(outputDataFileName));
        System.out.println("\ndata from output json file:\n" + serializedOutput);
    }
}
