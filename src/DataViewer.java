
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;


public class DataViewer {
    // GUI Definitions
    private JTable table;
    private JFrame frame;
    private JPanel panel;
    // Sorter Definition
    private TableRowSorter<TableModel> sorter;
    // Filter Definitions
    private JCheckBox onlyWinnersFilterCheckbox;
    private JCheckBox noWinnersFilterCheckbox;
    private JCheckBox pre2000FilterCheckbox;
    private JCheckBox post2000FilterCheckbox;

    public DataViewer(Object[][] data, String[] columnNames) {
        // Set up JFrame
        frame = new JFrame("Nobel Prize Winner Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Show 30 Rows if not FS
        frame.setSize(950, 600);

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

        // Enable Sorting
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        // Override Comparator to Sort Winners
        sorter.setComparator(2, (o1, o2) -> {
            String[] winners1 = (String[]) o1;
            String[] winners2 = (String[]) o2;
            String joined1 = String.join(", ", winners1);
            String joined2 = String.join(", ", winners2);
            return joined1.compareTo(joined2);
        });

        // Add table to JScrollPane, JScrollPane to panel
        JScrollPane pane = new JScrollPane(table);
        panel.add(pane, BorderLayout.CENTER);

        // Add Filter CBs
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        onlyWinnersFilterCheckbox = new JCheckBox("Show Only Winners");
        noWinnersFilterCheckbox = new JCheckBox("Show No Winners");
        pre2000FilterCheckbox = new JCheckBox("Show Post-2000 Winners");
        post2000FilterCheckbox = new JCheckBox("Show Pre-2000 Winners");

        // Add ActionListeners to CBs
        onlyWinnersFilterCheckbox.addActionListener(new FilterActionListener());
        noWinnersFilterCheckbox.addActionListener(new FilterActionListener());
        pre2000FilterCheckbox.addActionListener(new FilterActionListener());
        post2000FilterCheckbox.addActionListener(new FilterActionListener());

        // Add CBs to filterPanel
        filterPanel.add(onlyWinnersFilterCheckbox);
        filterPanel.add(noWinnersFilterCheckbox);
        filterPanel.add(pre2000FilterCheckbox);
        filterPanel.add(post2000FilterCheckbox);

        // Add filterPanel to JPanel
        panel.add(filterPanel, BorderLayout.NORTH);

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

    // ActionListeners for Filter CBs
    private class FilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a list to hold all active Filters
            List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();

            // Add Filters based on CB status
            if (onlyWinnersFilterCheckbox.isSelected()) {
                filters.add(removeOnlyWinnersFilter());
            }
            if (noWinnersFilterCheckbox.isSelected()) {
                filters.add(removeNoWinnersFilter());
            }
            if (pre2000FilterCheckbox.isSelected()) {
                filters.add(removePre2000Filter());
            }
            if (post2000FilterCheckbox.isSelected()) {
                filters.add(removePost2000Filter());
            }

            // Combine all Filters into a single AND Filter
            RowFilter<TableModel, Integer> combinedFilter = RowFilter.andFilter(filters);
            sorter.setRowFilter(combinedFilter);
        }
    }

    // FILTER: Remove Table Rows with 0 Winners
    private RowFilter<TableModel, Integer> removeOnlyWinnersFilter() {
        return RowFilter.notFilter(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, 0, 3));
    }

    // FILTER: Remove Table Rows with 1+ Winner(s)
    private RowFilter<TableModel, Integer> removeNoWinnersFilter() {
        return RowFilter.notFilter(RowFilter.numberFilter(RowFilter.ComparisonType.NOT_EQUAL, 0, 3));
    }

    // FILTER: Remove Table Rows between Years 1901-1999
    private RowFilter<TableModel, Integer> removePre2000Filter() {
        return new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
                // Get the year value from the first column (index 0)
                int year = Integer.parseInt(entry.getStringValue(0));
                // Include rows where the year is less than 1901 or greater than 1999
                return year < 1901 || year > 1999;
            }
        };
    }

    // FILTER: Remove Table Rows between Years 2000-2024 (present day in data)
    private RowFilter<TableModel, Integer> removePost2000Filter() {
        return new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
                // Get the year value from the first column (index 0)
                int year = Integer.parseInt(entry.getStringValue(0));
                // Include rows where the year is less than 2000 or greater than 2024
                return year < 2000 || year > 2024;
            }
        };
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