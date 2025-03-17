// Carson Milano
// Lab 3 (Data Display)

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Read in data from a file
        Object[][] data = FileReader.readNobelWinnerData(("nobel_prizes.csv"));

        // 1st Data Item Attributes
        System.out.println("1st Data Item:");
        System.out.println(data[0][0]);
        System.out.println(data[0][1]);
        for (String winner : (String[]) data[0][2]) {
            System.out.println(winner);
        }
        System.out.println(data[0][3]);

        // 10th Data Item Attributes
        System.out.println("\n10th Data Item:");
        System.out.println(data[9][0]);
        System.out.println(data[9][1]);
        for (String winner : (String[]) data[9][2]) {
            System.out.println(winner);
        }
        System.out.println(data[9][3]);

        // # of Data Items in the File
        System.out.println("\nTotal # of Data Items:");
        System.out.println(data.length);

        // Set up Data Table/GUI with DataViewer
        String[] columnNames = {"Year", "Category", "Winner(s)", "# of Winners"};
        new DataViewer(data, columnNames);
    }
}





