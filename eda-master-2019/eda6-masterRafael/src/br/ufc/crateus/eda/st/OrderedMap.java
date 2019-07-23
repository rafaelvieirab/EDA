package br.ufc.crateus.eda.st;

public interface OrderedMap<K extends Comparable<K>, V> extends Map<K, V> {
	K min();
	K max();
	K ceiling(K val);
	K floor(K val);
	void removeMax();
	void removeMin();
}
