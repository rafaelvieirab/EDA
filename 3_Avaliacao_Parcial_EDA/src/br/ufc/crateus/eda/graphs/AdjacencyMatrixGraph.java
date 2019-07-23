package br.ufc.crateus.eda.graphs;

import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.st.Map;
import br.ufc.crateus.eda.st.SeparateChainingHashMap;

public class AdjacencyMatrixGraph<T> implements Graph<T> {
	
	private T vertices[];
	private int edges;
	private boolean adjacency[][]; //matriz de adjacência
	private int qtdVertices;
	private Map<T, Integer> search;
	
	public AdjacencyMatrixGraph(int length) {
		vertices = (T[]) new Object[length];
		adjacency  = new boolean[length][length];
		this.search = new SeparateChainingHashMap(length);
		edges = 0;
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
		return (index < vertices.length) ? vertices[index] : null;
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
		
		adjacency[i][j] = true;
		adjacency[j][i] = true;
		edges++;
	}

	@Override
	public Iterable<T> adjacents(T v) {
		int i = index(v);
		if(i == -1) return null;
		
		Queue q = new LinkedList<T>();
		for(int j = 0; j < vertices.length; j++)
			if(adjacency[i][j])
				q.add(vertices[j]);
		
		return q;
	}
	
	@Override
	public Iterable<Integer> adjacentsInt(T v) {
		int i = index(v);
		if(i == -1) return null;
		
		Queue q = new LinkedList<Integer>();
		for(int j = 0; j < vertices.length; j++)
			if(adjacency[i][j]) 
				q.add(j);
		
		return q;
	}
	
	@Override
	public int degree(T v) {
		int i = index(v);
		if (i == -1) return 0;
		
		int count = 0;
		for(int j = 0; j < adjacency[i].length; j++)
			if(adjacency[i][j])
				count++;
		return count;
	}

	private void insert(T label) {
		vertices[qtdVertices] = label;
		search.put(label, qtdVertices);
		qtdVertices++;
	}

	
}
