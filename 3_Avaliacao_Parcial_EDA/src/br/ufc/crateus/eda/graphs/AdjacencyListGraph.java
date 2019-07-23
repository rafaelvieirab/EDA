package br.ufc.crateus.eda.graphs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.ufc.crateus.eda.st.Map;
import br.ufc.crateus.eda.st.SeparateChainingHashMap;

public class AdjacencyListGraph<T> implements Graph<T> {

	private T vertices[];
	List<Integer>[] adjacency; // lista de adjacência
	private Map<T, Integer> search;
	private int qtdVertices;
	private int edges;

	public AdjacencyListGraph(int length) {
		this.vertices = (T[]) new Object[length];
		this.search = new SeparateChainingHashMap(length);
		this.adjacency = new LinkedList[length];
		this.edges = 0;
		qtdVertices = 0;
	}

	@Override
	public int countVertices() {
		return qtdVertices;
	}

	@Override
	public int countEdges() {
		return edges;
	}

	@Override
	public int index(T v) {
		Integer x = search.get(v);
		if (x == null)
			return -1;
		return x;
	}

	@Override
	public T label(int index) {
		return (index < qtdVertices) ? vertices[index] : null;
	}

	@Override
	public boolean contains(T v) {
		return (index(v) != -1);
	}

	@Override
	public void addEdge(T v1, T v2) {
		int i = index(v1);
		int j = index(v2);

		if (i == -1 && qtdVertices < vertices.length - 1) {
			i = qtdVertices;
			insert(v1);
		}
		if (j == -1 && qtdVertices < vertices.length - 1) {
			j = qtdVertices;
			insert(v2);
		}
		if (i == -1 || j == -1)
			return;
			
		adjacency[i].add(j);
		adjacency[j].add(i);
		edges++;
	}

	@Override
	public Iterable<T> adjacents(T v) {
		int i = index(v);
		if (i == -1)
			return null;

		Queue<T> q = new LinkedList<T>();
		for (Integer j : adjacency[i])
			q.add(vertices[j]);
		return q;
	}

	@Override
	public Iterable<Integer> adjacentsInt(T v) {
		int i = index(v);
		if (i == -1)
			return null;

		Queue<Integer> q = new LinkedList<Integer>();
		for (Integer j : adjacency[i])
			q.add(j);
		return q;
	}

	@Override
	public int degree(T v) {
		int i = index(v);
		return (i == -1) ? adjacency[i].size() : 0;
	}

	private void insert(T label) {
		vertices[qtdVertices] = label;
		adjacency[qtdVertices] = new LinkedList();
		search.put(label, qtdVertices);
		qtdVertices++;
	}

}
