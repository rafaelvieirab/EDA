package br.ufc.crateus.eda.st;

import br.ufc.crateus.eda.graphs.Graph;

public class ComponenteConexo<T> {
	private boolean marked[];
	private int id[];
	private int count; //quantidade de componentes complexos

	public ComponenteConexo (Graph<T> G) {
		marked = new boolean[G.countVertices()];
		id = new int[G.countVertices()];
		count = 0;
		
		for (int s = 0; s < G.countVertices(); s++)
			if (!marked[s]) {
				dfs(G, s);
				count++;
			}
	}

	private void dfs(Graph<T> G, int v) {
		marked[v] = true;
		id[v] = count;
		for (Integer i : G.adjacentsInt(G.label(v))) 
			if (!marked[i]) 
				dfs(G, i);
	}
    
	public int count() {
		return count;
	}
}