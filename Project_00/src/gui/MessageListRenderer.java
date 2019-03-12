package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import model.Message;

/*
 * This demonstrates using an arbitrary component as a renderer
 */

public class MessageListRenderer implements ListCellRenderer<Message> {
	private JPanel panel;
	private JLabel label;

	private Color selectedColor;
	private Color normalColor;

	public MessageListRenderer() {
		panel = new JPanel();
		label = new JLabel();

		selectedColor = new Color(210, 210, 255);
		normalColor = Color.WHITE;

		label.setIcon(Utils.createIcon("/images/Information24.gif"));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(label);
	}

	public Component getListCellRendererComponent(JList<? extends Message> list, Message message, int index,
			boolean isSelected, boolean cellHasFocus) {
		label.setText(message.getTitle());
		panel.setBackground(cellHasFocus ? selectedColor : normalColor);
		return panel;
	}

}
