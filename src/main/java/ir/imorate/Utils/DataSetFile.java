package ir.imorate.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;

public class DataSetFile {
    public static int countLines(File file) throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");
        int lines = 0;
        try {
            while (lineIterator.hasNext()) {
                lines++;
                lineIterator.nextLine();
            }
        } finally {
            lineIterator.close();
        }
        return lines;
    }
}
