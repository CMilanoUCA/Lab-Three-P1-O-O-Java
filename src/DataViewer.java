import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.*;

public class DataViewer {
    // GUI Definitions
    private JTable table;
    private JFrame frame;
    private JPanel mainPanel;
    // Sorting Definitions
    private TableRowSorter<TableModel> sorter;
    // Filtering Definitions
    private JCheckBox onlyWinnersFilterCheckbox;
    private JCheckBox noWinnersFilterCheckbox;
    private JCheckBox pre2000FilterCheckbox;
    private JCheckBox post2000FilterCheckbox;
    // Details Definitions
    private JPanel detailsPanel;
    private JLabel yearLabel, categoryLabel, winnersLabel, numWinnersLabel;
    // Stats Definitions
    private JPanel statsPanel;
    private JLabel avgWinnersLabel, totalWinnersLabel, yearsTrackedLabel;

    public DataViewer(Object[][] data, String[] columnNames) {
        initializeFrame();
        initializeMainPanel();
        initializeTable(data, columnNames);
        initializeFilters();
        initializeDetailsPanel();
        initializeStatsPanel();
        setupListeners();
        frame.setVisible(true);
    }

    // Initialize JFrame
    private void initializeFrame() {
        frame = new JFrame("Nobel Prize Winner Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
    }

    // Initialize mainPanel
    private void initializeMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);
    }

    // Initialize Table + Components
    private void initializeTable(Object[][] data, String[] columnNames) {
        table = new JTable(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) {
                    return Object.class;
                }
                return super.getColumnClass(column);
            }
        };

        // Set up special renderer for Object[][] Winners Column
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if (value instanceof String[]) {
                    value = String.join(", ", (String[]) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

        // Set Column Width
        setColumnWidth();

        // Enable Sorting
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        sorter.setComparator(2, (o1, o2) -> {
            String[] winners1 = (String[]) o1;
            String[] winners2 = (String[]) o2;
            return String.join(", ", winners1).compareTo(String.join(", ", winners2));
        });

        // Add Table to JScrollPane + mainPanel
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(600, 200));
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(pane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }

    // Initialize Filter CBs
    private void initializeFilters() {
        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 5, 5));
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

        // Add filterPanel to tablePanel
        ((JPanel) mainPanel.getComponent(0)).add(filterPanel, BorderLayout.NORTH);
    }

    // Initialize detailsPanel
    private void initializeDetailsPanel() {
        detailsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Row Details"));
        detailsPanel.setPreferredSize(new Dimension(750, 100));

        yearLabel = new JLabel("Year: ");
        categoryLabel = new JLabel("Category: ");
        winnersLabel = new JLabel("Winner(s): ");
        numWinnersLabel = new JLabel("# of Winners: ");

        detailsPanel.add(yearLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(winnersLabel);
        detailsPanel.add(numWinnersLabel);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.add(detailsPanel);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Initialize statsPanel
    private void initializeStatsPanel() {
        statsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.setPreferredSize(new Dimension(750, 100));

        avgWinnersLabel = new JLabel("Average # of Winners: ");
        totalWinnersLabel = new JLabel("Total # of Winners: ");
        yearsTrackedLabel = new JLabel("# of Years Tracked: ");

        statsPanel.add(avgWinnersLabel);
        statsPanel.add(totalWinnersLabel);
        statsPanel.add(yearsTrackedLabel);

        ((JPanel) mainPanel.getComponent(1)).add(statsPanel);
    }

    // Let Mouse react to Table Row being selected
    private void setupListeners() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateDetailsPanel();
            }
        });

        // Update statsPanel
        updateStatsPanel();
    }

    // Set Column Widths for Data Table
    private void setColumnWidth() {
        TableColumn column;
        column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(75);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(150);
        column = table.getColumnModel().getColumn(2);
        column.setPreferredWidth(300);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(75);
    }

    // ActionListener for Filter CBs
    private class FilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();

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

            RowFilter<TableModel, Integer> combinedFilter = RowFilter.andFilter(filters);
            sorter.setRowFilter(combinedFilter);

            updateStatsPanel();
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
                int year = Integer.parseInt(entry.getStringValue(0));
                return year < 1901 || year > 1999;
            }
        };
    }

    // FILTER: Remove Table Rows between Years 2000-2024 (present day in data)
    private RowFilter<TableModel, Integer> removePost2000Filter() {
        return new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
                int year = Integer.parseInt(entry.getStringValue(0));
                return year < 2000 || year > 2024;
            }
        };
    }

    // Update detailsPanel w/selected Table Row Data
    private void updateDetailsPanel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int year = (int) table.getValueAt(selectedRow, 0);
            String category = (String) table.getValueAt(selectedRow, 1);
            String[] winners = (String[]) table.getValueAt(selectedRow, 2);
            int numWinners = (int) table.getValueAt(selectedRow, 3);

            yearLabel.setText("Year: " + year);
            categoryLabel.setText("Category: " + category);
            winnersLabel.setText("Winner(s): " + String.join(", ", winners));
            numWinnersLabel.setText("# of Winners: " + numWinners);
        }
    }

    // Update statsPanel w/aggregate Table Data
    private void updateStatsPanel() {
        int totalWinners = 0;
        int totalAwards = 0;
        Set<Integer> uniqueYears = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); i++) {
            int year = (int) table.getValueAt(i, 0);
            int numWinners = (int) table.getValueAt(i, 3);

            uniqueYears.add(year);
            totalWinners += numWinners;
            totalAwards++;
        }

        double avgWinners = totalAwards == 0 ? 0 : (double) totalWinners / totalAwards;

        avgWinnersLabel.setText(String.format("Average # of Winners: %.2f", avgWinners));
        totalWinnersLabel.setText("Total # of Winners: " + totalWinners);
        yearsTrackedLabel.setText("# of Years Tracked: " + uniqueYears.size());
    }

    // GETTERS BELOW
    public JTable getTable() {
        return table;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}