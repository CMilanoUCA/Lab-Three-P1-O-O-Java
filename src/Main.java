// Carson Milano
// Lab 3 PT. 1 (Data Display (Console Ver.))


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
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

        /*
        String contents = Files.readString(Path.of("nobel_prizes.csv"), StandardCharsets.UTF_8);
        List<String> lines = List.of(contents.split("\n"));
        ArrayList<Object[]> dataStr = new ArrayList<>();
        dataStr = lines.stream()
                .skip(1)
                .map(line -> line.split(","))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        Object[][] allData = dataStr.toArray(new Object[dataStr.size()][]);

        TableModel model = new DefaultTableModel(allData, 2);

        JTable table = new JTable(model);

        JPanel panel = new JPanel();
        panel.add(table);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

         */

    }
}