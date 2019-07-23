package br.ufc.crateus.eda.btree;

import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.btree.BinarySearchST;

public class BinarySearchST<K extends Comparable<K>, V> {
	private K[] keys;
	private V[] values;
	private Long[] filhos;
	private int length;
	
	@SuppressWarnings("unchecked")
	public BinarySearchST(int size) {
		this((K[]) new Comparable[size], (V[]) new Object[size],
				new Long[size+1], 0);
	}
	
	public BinarySearchST(K[] keys, V[] values, Long[] filhos, int length) {
		this.keys = keys;
		this.values = values;
		this.filhos = filhos;
		this.length = length;
	}

	@SuppressWarnings("unchecked")
	public BinarySearchST<K, V> split() {
		K[] sKeys = (K[]) new Comparable[keys.length];
		V[] sValues = (V[]) new Object[values.length];
		Long[] sFilhos = new Long[filhos.length];

		int half = length / 2;
		sFilhos[0] = null;// o primeiro filho é nulo, pois essa key futuramente
						  // será pai, por isso não precia de filhos agora
		for (int i = half; i < length; i++) {
			sKeys[i - half] = keys[i];
			sValues[i - half] = values[i];
			sFilhos[(i + 1) - half] = filhos[i + 1];//eledeixa a referencia 
		} 											//para o filho menor
		
		int newLength = this.length - half;
		BinarySearchST<K, V> newST = new BinarySearchST<K, V>(sKeys, sValues, sFilhos, newLength);
		
		this.length /= 2;
		return newST;
	}
	
	public int rank(K key) {// retorna a chave maior ou igual, em comparação a k;
		int lo = 0, hi = length - 1;
		while (lo <= hi) {
			int m = (lo + hi) / 2;
			int cmp = key.compareTo(keys[m]);
			if (cmp < 0) hi = m - 1;
			else if (cmp > 0) lo = m + 1;
			else return m;
		}
		return lo;
	}

	public void put(K key, V value) {
		
		int j = rank(key);
 		if (!key.equals(keys[j])) {
 			for (int i = length; i > j; i--) { 
 				keys[i] = keys[i - 1];
 				values[i] = values[i - 1];
 				filhos[i+1] = filhos[i];
 			}
 			length++;
 		}
 		keys[j] = key;
 		values[j] = value;
 		filhos[j] = 0L;
 		filhos[j+1] = 0L;
	}
	//Usado na subida de um nó(key,value e dois filhos)
	public void insert(BinarySearchST<K, V> bsST) {
		K key = bsST.min();
		int j = rank(key);
 		if (!key.equals(keys[j])) {
 			for (int i = length; i > j; i--) { 
 				keys[i] = keys[i - 1];
 				values[i] = values[i - 1];
 				filhos[i+1] = filhos[i];
 			}
 			length++;
 		}
 		keys[j] = key;
 		values[j] = bsST.values[0];
 		filhos[j] = bsST.filhos[0];
 		filhos[j+1] = bsST.filhos[1];
	}
	
	//usada na remoção de uma chave
	public void update(BinarySearchST<K, V> bs,int pos) {
		keys[pos] = bs.min();
		values[pos] = bs.get(bs.min());
	}
	
	public void uniao(BinarySearchST<K, V> bsST) {
		for (int i = length; i < length + bsST.length; i++) {
			keys[i] = bsST.keys[i - length];
			values[i] = bsST.values[i - length];
			filhos[i+1] = bsST.filhos[(i+1) - length];
		}
		length += bsST.length;
	}
	
	public V get(K key) {
		int j = rank(key);
 		return (key.equals(keys[j]))? values[j] : null;
	}
	
	public boolean qtdMin() {
		double m = (double) keys.length;
		return length <= Math.ceil(m/2)-1;  
	}
	
	public void delete(K key) {
		int j = rank(key);
		if (key.equals(keys[j])) {
 			for (int i = j; i < length - 1; i++) {
 				keys[i] = keys[i + 1];
 				values[i] = values[i + 1];
 				filhos[i+1] = filhos[i + 2];
 			}
 			length--;
		}
	}
	
	public boolean contains(K key) {
		return get(key) != null;
	}

	public boolean isEmpty() {
		return length == 0;
	}

	public int size() {
		return length;
	}

	public Iterable<K> keys() {
		Queue<K> queue = new LinkedList<>();
		for (int i = 0; i < length; i++) queue.add(keys[i]);
		return queue;
	}
	
	public Iterable<V> values() {
		Queue<V> queue = new LinkedList<>();
		for (int i = 0; i < length; i++) queue.add(values[i]);
		return queue;
	}

	public Iterable<Long> filhos() {
		Queue<Long> queue = new LinkedList<Long>();
		for (int i = 0; i <= length; i++) queue.add(filhos[i]);
		return queue;
	}

	public Long getFilhos(int pos) {
		return filhos[pos];
	}
	
	public K min() {
		return keys[0];
	}

	public K max() {
		return keys[length - 1];
	}

	public K floor(K key) {
		int j = rank(key);
		if (key.equals(keys[j])) return key;
		return (j > 0)? keys[j - 1] : null;
	}

	public K ceiling(K key) {
		int j = rank(key);
		return keys[j];
	}

	public K select(int i) {
		return keys[i];
	}

	public Iterable<K> keys(K lo, K hi) {
		return null;
	}
	
	public int size(K lo, K hi) {
		return rank(hi) - rank(lo);
	}

	public void deleteMax() {
		length--;
	}

	public void deleteMin() {
		for (int i = 0; i < length - 1; i++) {
			keys[i] = keys[i + 1];
			values[i] = values[i + 1];
			filhos[i] = filhos[i+1];
		}
		filhos[length - 1] = filhos[length];
		length--;
	}
	
	public void constroiRoot(K key, V value,Long filho1, Long filho2) {
		this.keys[0] = key;
		this.values[0] = value;
		this.filhos[0] = filho1;
		this.filhos[1] = filho2;
		length++;
	}
	
	/*
	public static void main(String[] args) {
		BinarySearchST<Integer, Integer> st = new BinarySearchST<>(10);
		
		for(int i = 0; i<5;i++)
			st.put(i, i);
		st.put(6, 6);
		st.put(10, 10);
		
		for (Integer key : st.keys()) System.out.println(key);
		System.out.println("Rank:"+st.select(st.rank(7)));
		System.out.println("Floor: " + st.floor(7));
		System.out.println("Ceiling: " + st.ceiling(7));
	}*/
}
