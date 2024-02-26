import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CSVLogic {

    public List<String[]> read_CSV(String file) {

        List<String[]> data = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file + ". " + e.getMessage());
            return null;
        }

        return data;
    }

    public void print_data(String fileName, List<String[]> data) {

        if (data == null) {
            System.out.println("No data in file: " + fileName);
            return;
        }
        
        System.out.println("\nFile to read: " + fileName);

        for (String[] row : data) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }


    public Map<String, Integer[]> find_common_headers(List<String[]> data1, List<String[]> data2) {

        String[] headers1 = data1.get(0); 
        String[] headers2 = data2.get(0);

        Map<String, Integer[]> commonHeader = new HashMap<>();

        for (int i = 0; i < headers1.length; i++) {
            for (int j = 0; j < headers2.length; j++) {
                if (headers1[i].equals(headers2[j])) {
                    commonHeader.put(headers1[i], new Integer[] { i, j });
                }
            }
        }

        System.out.println("\nCommon headers:"); 
        System.out.println(commonHeader); 

        return commonHeader;
    }


    private boolean is_row_equal(String[] row1, String[] row2, Map<String, Integer[]> commonColumns) {
        for (Map.Entry<String, Integer[]> entry : commonColumns.entrySet()) {
            Integer[] indexes = entry.getValue();
            if (!row1[indexes[0]].equals(row2[indexes[1]])) {
                return false;
            }
        }
        return true;
    }

    private String[] merge_rows(String[] row1, String[] row2, Map<String, Integer[]> commonColumns, 
    String[] headers1, String[] headers2) {
        String[] mergedRow = new String[headers1.length + headers2.length - commonColumns.size()];
        int index = 0;

        for (String header : headers1) {
            mergedRow[index++] = row1[Arrays.asList(headers1).indexOf(header)];
        }
    
        for (String header : headers2) {
            if (!commonColumns.containsKey(header)) {
                mergedRow[index++] = row2[Arrays.asList(headers2).indexOf(header)];
            }
        }
    
        return mergedRow;
    }
    

    public void filter_rows(List<String[]> data1, List<String[]> data2, Map<String,
    Integer[]> commonColumns,List<String[]> commonData, List<String[]> diffData1, List<String[]> diffData2) { 
        String[] headers1 = data1.get(0);
        String[] headers2 = data2.get(0);

        //set headers
        diffData1.add(headers1);
        diffData2.add(headers2);
        List<String> combinedHeaders = new ArrayList<>(Arrays.asList(headers1));
        for (String header : headers2) {
            if (!commonColumns.containsKey(header)) {
                combinedHeaders.add(header);
            }
        }
        commonData.add(combinedHeaders.toArray(new String[0]));

        Set<String> matchedRowsInData2 = new HashSet<>();
        for (int i = 1; i < data1.size(); i++) {
            String[] row1 = data1.get(i);
            boolean isCommon = false;

            for (int j = 1; j < data2.size(); j++) {
                String[] row2 = data2.get(j);
                if (is_row_equal(row1, row2, commonColumns)) {
                    isCommon = true;
                    matchedRowsInData2.add(Arrays.toString(row2));
                    commonData.add(merge_rows(row1, row2, commonColumns, headers1, headers2));
                }
            }

            if (!isCommon) {
                diffData1.add(row1);
            }
        }

        for (int j = 1; j < data2.size(); j++) {
            String[] row2 = data2.get(j);
            if (!matchedRowsInData2.contains(Arrays.toString(row2))) {
                diffData2.add(row2);
            }
        }
    }


    public void write_CSV(String fileName, List<String[]> data) {
        File file = new File(fileName);
    
        if (file.exists()) {
            System.out.println("File " + fileName + " already exists. It will be overwritten.");
        }

        try (PrintWriter pw = new PrintWriter(file)) {
            for (String[] row : data) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + fileName + ". " + e.getMessage());
        }
    }
}
