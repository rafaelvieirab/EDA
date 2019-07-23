package br.ufc.crateus.eda.graphs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import br.ufc.crateus.eda.st.BreadthFirstPaths;
import br.ufc.crateus.eda.st.ComponenteConexo;

public class GraphUtils {
	public static void main(String[] args) throws FileNotFoundException {
		
		InputStream is= new FileInputStream("movies.txt");
		Graph<String> g = readFromFile(is,",");
		System.out.println(g.countVertices());
		System.out.println(componentesConexos(g));
	}
	
	static <T> Iterable<T> shortestPath(Graph<T> graph, T v1, T v2) {
		int i = graph.index(v1);
		int j = graph.index(v2);
		
		if(i == -1 || j == -1) 	
			return null;
		
		BreadthFirstPaths<T> bfs = new BreadthFirstPaths<T>(graph,i);	
		
		Queue<T> q = new LinkedList<T>();
		for(Integer pos : bfs.pathTo(j))
			q.add(graph.label(pos));
		return q;
	}
	
	static Graph<String> readFromFile(InputStream is, String sep){
		Scanner scan = new Scanner(is);
		Graph<String> g = new AdjacencyListGraph<String>(200000);
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String vetor[] = line.split(sep);
			
			for(int i = 1; i < vetor.length; i++) 
				g.addEdge(vetor[0],vetor[i]);
		}
		
		scan.close();
		return g;
	}
	
	static int componentesConexos(Graph g) {
		if(g == null) return 0;
		ComponenteConexo cc = new ComponenteConexo(g);
		return cc.count();
	}
	
}
