package br.ufc.crateus.eda.hash;

import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.st.Map;

public class LinearProbingHashMap<K extends Comparable<K>,V> implements Map<K, V> {
	
	private K[] keys;
	private V[] values;
	int size;
	
	public LinearProbingHashMap() {
		this(397);
	}
	
	@SuppressWarnings("unchecked")
	public LinearProbingHashMap(int length) {
		this.keys =  (K[]) new Object[length];
		this.values =  (V[]) new Object[length];
		this.size = 0;
	}
	
	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % keys.length;
	}
	
	@Override
	public void put(K key, V value) {
		int p = hash(key);
		
		while(keys[p] != null && !key.equals(keys[p]))
			p = (p+1) % keys.length;
		
		if(values[p] == null) size++;
		
		keys[p] = key;
		values[p] = value;
		
		if( (double)(size / keys.length) == 0.5) resize(2 * keys.length);
	}

	@Override
	public V get(K key) {
		int p = hash(key);
		
		while(keys[p] != null && !key.equals(keys[p]))
			p = (p+1) % keys.length;
		return values[p];
	}

	@Override
	public void remove(K key) {
		int p = hash(key);
		while(keys[p] != null && !key.equals(keys[p]))
			p = (p+1) % keys.length;
		
		if(values[p] != null) size--;
		values[p] = null;
		
		if((double) size / keys.length == 0.125) resize(keys.length / 2);
	}
	
	private void resize(int newLength) {
		LinearProbingHashMap<K, V> tmp = new LinearProbingHashMap<K, V>(newLength);
		
		for(int i=0 ; i < keys.length ; i++) 
			if(values[i] != null)
				tmp.put(keys[i], values[i]);
			
		this.keys = tmp.keys;
		this.values = tmp.values;
	}

	@Override
	public Iterable<K> keys() {
		Queue<K> q = new LinkedList<K>();
		collect(values,q);
		return q;
	}
	
	private void collect(V[] values,Queue<K> q) {
		for(int i = 0;i < values.length ;i++)
			if(values[i] != null)
				q.add(keys[i]);
	}
	
	@Override
	public boolean contains(K key) {
		return get(key) != null;
	}

}
