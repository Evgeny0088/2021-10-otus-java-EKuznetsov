package ru.otus.dataprocessor;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper mapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        URL inputDataJson = ResourcesFileLoader.class.getClassLoader().getResource(fileName);
        assert inputDataJson != null;
        try(Reader jsonReader = new BufferedReader(new FileReader(inputDataJson.getFile()))){
            var gson = new Gson();
            return Arrays.asList(gson.fromJson(jsonReader,Measurement[].class));
        }catch (FileProcessException exception){
            throw new FileProcessException("file cannot be processed!");
        }
    }

    private List<Measurement> loadJacksonImplementationBackUp() throws IOException {
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Measurement> measurements = new ArrayList<>();
        URL inputDataJson = ResourcesFileLoader.class.getClassLoader().getResource(fileName);
        if (inputDataJson == null){
            throw new IllegalArgumentException("input file is not found!");
        }
        mapper.readTree(new BufferedReader(new FileReader(inputDataJson.getFile()))).getElements().forEachRemaining(node-> {
            if (node != null && node.get("name") != null){
                String nameField = node.get("name").getValueAsText();
                if (node.get("value").isNumber()){
                    measurements.add(new Measurement(nameField,node.get("value").getIntValue()));
                }else {
                    System.out.printf("wrong value for node: %s => %s\n", nameField,node.get("value").getValueAsText());
                }
            }
        });
        return measurements;
    }

}
