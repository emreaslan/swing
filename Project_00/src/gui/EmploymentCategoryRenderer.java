package gui;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import model.EmploymentCategory;

public class EmploymentCategoryRenderer implements TableCellRenderer {

	private JComboBox comboBox;
	
	public EmploymentCategoryRenderer() {
		comboBox = new JComboBox(EmploymentCategory.values());
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		comboBox.setSelectedItem(value);

		return comboBox;
	}

}
