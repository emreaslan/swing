package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Message;

/*
 * This is sort of a simulated message server.
 */

public class MessageServer implements Iterable<Message> {
	private Map<Integer, List<Message>> messages;
	private List<Message> selected;

	public MessageServer() {
		selected = new ArrayList<Message>();
		messages = new TreeMap<Integer, List<Message>>();

		List<Message> list = new ArrayList<Message>();
		list.add(new Message("The cat is missing", "Have you seen Felix anywhere?"));
		list.add(new Message("See you later?", "Are we still meeting in the pub?"));
		messages.put(1, list);

		list = new ArrayList<Message>();
		list.add(new Message("How about dinner later?", "Are you doing anything later on?"));
		messages.put(2, list);
	}

	public void setSelectedServers(Set<Integer> servers) {
		selected.clear();
		for (Integer id : servers) {
			if (messages.containsKey(id)) {
				selected.addAll(messages.get(id));
			}
		}
	}

	public int getMessagesCount() {
		return selected.size();
	}

	public Iterator<Message> iterator() {
		return new MessageIterator(selected);
	}

}

class MessageIterator implements Iterator<Message> {
	private Iterator<Message> iterator;

	public MessageIterator(List<Message> messages) {
		iterator = messages.iterator();
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public Message next() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		return iterator.next();
	}

	public void remove() {
		iterator.remove();
	}
}