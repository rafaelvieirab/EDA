package br.ufc.crateus.eda.st;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.st.Map;

public class SeparateChainingHashMap<K extends Comparable<K>, V> implements Map<K, V> {

	private static class Node {
		Object key;
		Object value;
		Node next;

		public Node(Object key, Object value, Node next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	private Node[] table;
	private int size = 0;

	public SeparateChainingHashMap() {
		this(97);
	}

	public SeparateChainingHashMap(int lenght) {
		this.table = new Node[lenght];
	}

	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % table.length;
	}
	
	@SuppressWarnings("unchecked")
	private void resize(int newLenght) {
		SeparateChainingHashMap<K, V> tmp = new SeparateChainingHashMap<K, V>(newLenght);

		int i;
		for (i = 0; i < table.length; i++)
			for (Node n = table[i]; n != null; n = n.next)
				tmp.put((K) n.key, (V) n.value);
		table = tmp.table;
	}

	public Node getNode(K key) {
		int i = hash(key);
		for (Node n = table[i]; n != null; n = n.next)
			if (key.equals(n.key))
				return n;

		return null;
	}

	@Override
	public void put(K key, V value) {
		Node n = getNode(key);
		if (n == null) {
			int i = hash(key);
			table[i] = new Node(key, value, table[i]);

			this.size++;
			if (this.size / table.length == 8)
				resize(table.length * 2);
		} else
			n.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		Node n = getNode(key);
		return (n != null) ? (V) n.value : null;
	}

	@Override
	public void remove(K key) {
		int i = hash(key);
		Node tmp = new Node(null, null, table[i]);

		for (Node n = tmp; n.next != null; n = n.next)
			if (key.equals(n.next.key)) {
				n.next = n.next.next;

				this.size--;
				if (this.size / table.length == 2)
					resize(table.length / 2);
				break;
			}

		table[i] = tmp.next;
	}
	
	@Override
	public Iterable<K> keys() {
		Queue<K> q = new LinkedList<K>();
		collect(table,q);
		return q;
	}
	
	private void collect(Node[] table,Queue<K> q) {
		for(int i=0 ; i < table.length ; i++) 
			for(Node r = table[i]; r != null; r = r.next) 
				q.add((K) r.key);
	}
	
	@Override
	public boolean contains(K key) {
		return (getNode(key) != null);
	}
	
}
