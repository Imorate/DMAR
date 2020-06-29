package ir.imorate;

import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class DataSetParser {
    private File dataSetFile;

    public DataSetParser(File dataSetFile) {
        this.dataSetFile = dataSetFile;
    }

    public List<CSVRecord> readDataSet() {
        try (Reader reader = new FileReader(dataSetFile);
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.EXCEL.withNullString("").withIgnoreSurroundingSpaces().withIgnoreEmptyLines())) {
            return csvParser.getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public List<List<String>> toList() {
        List<List<String>> dataSetList = new ArrayList<>();
        for (CSVRecord csvRecord : readDataSet()) {
            List<String> recordList = new ArrayList<>();
            for (int i = 0; i < csvRecord.size(); i++) {
                if (csvRecord.get(i) != null) {
                    recordList.add(csvRecord.get(i));
                }
            }
            dataSetList.add(recordList);
        }
        return dataSetList;
    }

}
