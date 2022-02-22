package lab13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    BufferedReader reader;
    String delimiter;
    boolean hasHeader;

    // nazwy kolumn w takiej kolejności, jak w pliku
    List<String> columnLabels = new ArrayList<>();
    // odwzorowanie: nazwa kolumny -> numer kolumny
    Map<String, Integer> columnLabelsToInt = new HashMap<>();

    String[] current;

    public CSVReader(String filename, String delimiter, boolean hasHeader) throws IOException {
        this.reader = new BufferedReader(new FileReader(filename));
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if (hasHeader) {
            this.parseHeader();
        }
    }

    void parseHeader() throws IOException {
        // wczytaj wiersz
        String line = reader.readLine();
        if (line == null) {
            return;
        }
        // podziel na pola
        String[] header = line.split(delimiter);
        // przetwarzaj dane w wierszu
        for (int i = 0; i < header.length; i++) {
            // dodaj nazwy kolumn do columnLabels i numery do columnLabelsToInt
            columnLabels.add(header[i]);
            columnLabelsToInt.put(header[i], i);
        }
    }

    boolean next() {
        // czyta następny wiersz, dzieli na elementy i przypisuje do current
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) { /* e.printStackTrace(); */ }
        if (line == null) {
            return false;
        }

        // Zamień przecinki w stringach
        line = line.replaceAll("\"(.+)" + this.delimiter + "(.+)\"", "\"$1#comma#$2\"");

        // podziel na pola
        this.current = line.split(delimiter);

        // przywróć przecinki i usuń cudzysłowy
        for (int i = 0; i < this.current.length; i++) {
            this.current[i] = this.current[i].replaceAll("#comma#", this.delimiter);
            this.current[i] = this.current[i].replaceAll("\"", "");
        }

        return true;
    }

    List<String> getColumnLabels() {
        return this.columnLabels;
    }

    int getRecordLength() {
        return this.current.length;
    }

    boolean isMissing(int columnIndex) {
        if (columnIndex < this.current.length) {
            return this.get(columnIndex).isEmpty();
        }

        return true;
    }

    boolean isMissing(String columnLabel) {
        if (this.columnLabelsToInt.get(columnLabel) < this.current.length) {
            return this.get(columnLabel).isEmpty();
        }

        return true;
    }

    String get(int columnIndex) {
        return this.current[columnIndex];
    }

    int getInt(int columnIndex) throws Exception {
        if (this.get(columnIndex).equals("")) {
            throw new Exception("Empty value!");
        }

        return Integer.parseInt(this.current[columnIndex]);
    }

    long getLong(int columnIndex) throws Exception {
        if (this.get(columnIndex).equals("")) {
            throw new Exception("Empty value!");
        }

        return Long.parseLong(this.current[columnIndex]);
    }

    double getDouble(int columnIndex) throws Exception {
        if (this.get(columnIndex).equals("")) {
            throw new Exception("Empty value!");
        }

        return Double.parseDouble(this.current[columnIndex]);
    }

    LocalTime getTime(int columnIndex, String format) throws Exception {
        if (this.get(columnIndex).equals("")) {
            throw new Exception("Empty value!");
        }

        return LocalTime.parse(this.current[columnIndex], DateTimeFormatter.ofPattern(format));
    }

    LocalDate getDate(int columnIndex, String format) throws Exception {
        if (this.get(columnIndex).equals("")) {
            throw new Exception("Empty value!");
        }

        return LocalDate.parse(this.current[columnIndex], DateTimeFormatter.ofPattern(format));
    }

    String get(String columnName) {
        return this.get(this.columnLabelsToInt.get(columnName));
    }

    int getInt(String columnName) throws Exception {
        return this.getInt(this.columnLabelsToInt.get(columnName));
    }

    long getLong(String columnName) throws Exception {
        return this.getLong(this.columnLabelsToInt.get(columnName));
    }

    double getDouble(String columnName) throws Exception {
        return this.getDouble(this.columnLabelsToInt.get(columnName));
    }

    LocalTime getTime(String columnName, String format) throws Exception {
        return this.getTime(this.columnLabelsToInt.get(columnName), format);
    }

    LocalDate getDate(String columnName, String format) throws Exception {
        return this.getDate(this.columnLabelsToInt.get(columnName), format);
    }
}
