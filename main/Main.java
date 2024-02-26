import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        final String ANSI_UNDERLINE = "\u001B[4m";
        final String ANSI_RESET = "\u001B[0m";

        if (args.length < 2) {
            System.err.println("Two CSV files are required as arguments!!!");
            return;
        }

        String csvFile1 = args[0];
        String csvFile2 = args[1];

        CSVLogic reader = new CSVLogic();

        List<String[]> data1 = reader.read_CSV(csvFile1);
        List<String[]> data2 = reader.read_CSV(csvFile2);

        //
        System.out.println(ANSI_UNDERLINE + "Input:"+ ANSI_RESET); 

        reader.print_data(csvFile1, data1);
        reader.print_data(csvFile2, data2); 

        //
        System.out.println(ANSI_UNDERLINE + "\nOutput:" + ANSI_RESET); 
        
        Map<String, Integer[]> commonHeaders = reader.find_common_headers(data1, data2);

        List<String[]> commonData = new ArrayList<>();
        List<String[]> diff1Data = new ArrayList<>();
        List<String[]> diff2Data = new ArrayList<>();

        reader.filter_rows(data1, data2, commonHeaders, commonData, diff1Data, diff2Data);
        
        reader.print_data("common", commonData);
        reader.print_data("diff1", diff1Data); 
        reader.print_data("diff2", diff2Data);

        System.out.println();        
        reader.write_CSV("common.csv", commonData);
        reader.write_CSV("diff1.csv", diff1Data);
        reader.write_CSV("diff2.csv", diff2Data);
    }
}

