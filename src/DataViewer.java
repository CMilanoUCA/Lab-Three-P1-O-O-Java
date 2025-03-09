import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class DataViewer {
    private JTable table;
    private JFrame frame;
    private JPanel panel;

    public DataViewer(Object[][] data, String[] columnNames) {
        // Set up JFrame
        frame = new JFrame("Nobel Prize Winner Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Show 30 Rows if not FS
        frame.setSize(950, 540);

        // Set up JPanel
        panel = new JPanel(new BorderLayout());

        // Set up JTable with columns and data
        table = new JTable(data, columnNames) {
            // Override 3rd Column (i == 2) (String[]) to read its data.
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) {
                    return Object.class;
                }
                return super.getColumnClass(column);
            }
        };

        // Set up a specialized 3rd Column for String[] + 3rd Column render
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                // Convert String[] to Strings separated by commas
                if (value instanceof String[]) {
                    String[] array = (String[]) value;
                    value = String.join(", ", array);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

        // Set Column Width
        setColumnWidth();

        // Add table to JScrollPane, JScrollPane to panel
        JScrollPane pane = new JScrollPane(table);
        panel.add(pane, BorderLayout.CENTER);

        // Add JPanel and Display JFrame
        frame.add(panel);
        frame.setVisible(true);
    }

    // Function setting Column Width
    private void setColumnWidth() {
        // Set Column Width
        TableColumn column;
        // 1st Column (Year)
        column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(75);
        // 2nd Column (Category)
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(150);
        // 3rd Column (Winner(s))
        column = table.getColumnModel().getColumn(2);
        column.setPreferredWidth(650);
        // 4th Column (# of Winners)
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(75);
    }

    // GETTERS BELOW
    public JTable getTable() {
        return table;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanel() {
        return panel;
    }
}