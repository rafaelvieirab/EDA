package br.ufc.crateus.eda.st;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import br.ufc.crateus.eda.graphs.Graph;

public class BreadthFirstPaths<T> {
	private boolean[] marked;
	private int[] edgeTo;
	private final int s;

	public BreadthFirstPaths(Graph<T> G, int s) {
		marked = new boolean[G.countVertices()];
		edgeTo = new int[G.countVertices()];
		this.s = s;
		bfs(G, s);
	}

	private void bfs(Graph<T> G, int s) {
		Queue<Integer> queue = new LinkedList<Integer>();
		marked[s] = true;
		queue.add(s);

		while (!queue.isEmpty()) {
			int v = queue.remove();
			for (Integer w : G.adjacentsInt(G.label(v)))
				if (!marked[w]) {
					edgeTo[w] = v;
					marked[w] = true;
					queue.add(w);
				}
		}
	}

	public boolean hasPathTo(int v) {
		return marked[v];
	}

	public Iterable<Integer> pathTo(int v) {
		if(v >= marked.length) return null;
		
		Stack<Integer> path = new Stack<Integer>();
		if (marked[v]) 
			path.push(v);
		 else {
			for (int x = v; x != s; x = edgeTo[x])
				path.push(x);
			path.push(s);
		}
		return path;
	}
}
