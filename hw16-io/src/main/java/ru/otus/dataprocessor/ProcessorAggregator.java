package ru.otus.dataprocessor;

import ru.otus.model.Measurement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        LinkedHashMap<String, Double> groupsOfMeasurements = new LinkedHashMap<>();
        int i = 0;
        int summaryOfValues;
        String currentName;
        while (i<data.size()){
            currentName = data.get(i).getName();
            summaryOfValues = 0;
            while (i<data.size() && currentName.equals(data.get(i).getName())){
                summaryOfValues += data.get(i++).getValue();
            }
            groupsOfMeasurements.put(currentName, (double) summaryOfValues);
        }
        return groupsOfMeasurements;
    }
}
