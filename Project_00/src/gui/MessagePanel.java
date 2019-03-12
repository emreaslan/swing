package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import controller.MessageServer;
import model.Message;

class ServerInfo {
	private String name;
	private int id;
	private boolean checked;

	public ServerInfo(String name, int id, boolean checked) {
		this.id = id;
		this.name = name;
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public String toString() {
		return name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}

public class MessagePanel extends JPanel implements ProgressDialogListener {

	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;
	private Set<Integer> selectedServers;
	private MessageServer messageServer;
	private ProgressDialog progressDialog;
	private SwingWorker<List<Message>, Integer> worker;

	private TextPanel textPanel;
	private JList<String> messageList;
	private JSplitPane upperPane;
	private JSplitPane lowerPane;

	private DefaultListModel<String> messageListModel;

	public MessagePanel(JFrame parent) {

		messageListModel = new DefaultListModel<String>();

		messageServer = new MessageServer();
		selectedServers = new TreeSet<Integer>();
		selectedServers.add(1);
		selectedServers.add(2);
		selectedServers.add(4);

		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();
		progressDialog = new ProgressDialog(parent, "Messages downloading...");
		progressDialog.setListener(this);

		serverTree = new JTree(createTree());
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.setEditable(true);

		serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		messageServer.setSelectedServers(selectedServers);

		treeCellEditor.addCellEditorListener(new CellEditorListener() {
			public void editingStopped(ChangeEvent e) {
				ServerInfo info = (ServerInfo) treeCellEditor.getCellEditorValue();

				int serverId = info.getId();
				if (info.isChecked()) {
					selectedServers.add(serverId);
				} else {
					selectedServers.remove(serverId);
				}
				messageServer.setSelectedServers(selectedServers);
				retrieveMessages();
			}

			public void editingCanceled(ChangeEvent e) {

			}
		});
		setLayout(new BorderLayout());

		textPanel = new TextPanel();
		messageList = new JList<String>(messageListModel);

		lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(messageList), textPanel);
		upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(serverTree), lowerPane);

		textPanel.setMinimumSize(new Dimension(10, 100));
		messageList.setMinimumSize(new Dimension(10, 100));

		upperPane.setResizeWeight(0.5);
		lowerPane.setResizeWeight(0.5);

		add(upperPane, BorderLayout.CENTER);
	}

	public void refresh() {
		retrieveMessages();
	}

	private void retrieveMessages() {
		progressDialog.setMaximum(messageServer.getMessagesCount());
		progressDialog.setVisible(true);

		worker = new SwingWorker<List<Message>, Integer>() {

			@Override
			protected List<Message> doInBackground() throws Exception {
				List<Message> retrievedMessages = new ArrayList<Message>();
				int count = 0;
				for (Message message : messageServer) {
					if (isCancelled())
						break;
					System.out.println(message.getTitle());
					retrievedMessages.add(message);
					++count;
					publish(count);
				}
				return retrievedMessages;
			}

			@Override
			protected void done() {
				progressDialog.setVisible(false);

				if (isCancelled())
					return;
				try {
					List<Message> retrivedMesages = get();
					messageListModel.removeAllElements();

					for (Message message : retrivedMesages) {
						messageListModel.addElement(message.getTitle());
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.setVisible(false);
			}

			@Override
			protected void process(List<Integer> counts) {
				int retrieved = counts.get(counts.size() - 1);
				progressDialog.setValue(retrieved);
			}
		};

		worker.execute();
	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");

		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(
				new ServerInfo("New York", 1, selectedServers.contains(1)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo("Boston", 2, selectedServers.contains(2)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(
				new ServerInfo("Los Angeles", 3, selectedServers.contains(3)));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");

		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(
				new ServerInfo("London", 4, selectedServers.contains(4)));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(
				new ServerInfo("Edinburgh", 5, selectedServers.contains(5)));

		branch2.add(server4);
		branch2.add(server5);

		top.add(branch1);
		top.add(branch2);

		return top;
	}

	public void progressDialogCancelled() {
		if (worker != null) {
			worker.cancel(true);
		}
	}

}
