package br.ufc.crateus.eda.st;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class Graph {

	private Integer[] vertices;
	List<Integer>[] adjacentes;
	private int edges;//arestas

	public Graph(int v){
		vertices = new  Integer[v];
		adjacentes = new LinkedList[v];
		
		for(int i=0 ; i< v; i++)
			vertices[i] = i;
	}
	public Graph(InputStream in) {
	
	}

	public void addEdge(int v, int w) {
		adjacentes[v].add(w);
		adjacentes[w].add(v);
		edges++;
	}
	
	public int V() {
		return vertices.length;
	}
	public int E() {
		return edges;
	}
	public Iterable<Integer> adj(int v){
		return adjacentes[v];
	}
	
	public static int degree(Graph g, int v) {
		int degree = 0;
		for(List<Integer> aux : g.adjacentes[v])
			degree++;
		adjacentes[v].size();
		return degree;
	}
}
