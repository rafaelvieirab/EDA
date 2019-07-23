<<<<<<< HEAD
package br.ufc.crateus.eda.st;

import java.util.ArrayList;
import java.util.List;

public class MinHeap<T extends Comparable <T>>{
	private List<T> array;
	private int numElem;
	
	public MinHeap() {
		this.array = new ArrayList<T>();
		this.array.add(null);
		numElem = 0;
	}
	
	/*Funções:
	 * Suporta:
	 Make-Heap(): cria uma heap vazia
 	 Insert(H, x): insere um elemento x na heap H
	 Extract-Min(M): remove e retorna o elemento com a menor chave
 	 Decrease-Key(H, x, k): decrementa a chave do elemento x para k
	 * Úteis:
	 Is-Empty(H): A heap está vazia?
	 Find-Min(H): retorna o elemento com a menor chave.
	 Delete(H, x): remove o elemento x da heap H
	 Meld(H1, H2): une as heaps H1 e H2
	 */
	
	
	public void insert(T value) {
		array.add(value);
		swinUp(array.size()-1);
	}
	
	private void swinUp(int i) {
		while(i > 1) {
			if(greaterThan(i/2,i))
				swap(i/2,i);
			i /= 2;
		}		
	}
	
	private void swap(int i,int j) {
		T aux = array.get(i);
		array.set(i,array.get(j));
		array.set(j,aux);
	}
	
	private boolean greaterThan(int k, int j) {
		return array.get(k).compareTo(array.get(j)) > 0;
	}
	

	private void swinDown(int i) {//corrige depois
		while(i > 1) {
			if(greaterThan(i/2,i))
				swap(i/2,i);
			i /= 2;
		}		
	}
	
	
}
=======
package br.ufc.crateus.eda.st;

import java.util.ArrayList;
import java.util.List;

public class MinHeap<T extends Comparable <T>>{
	private List<T> array;
	private int numElem;
	
	public MinHeap() {
		this.array = new ArrayList<T>();
		this.array.add(null);
		numElem = 0;
	}
	
	public void insert(T value) {
		array.add(value);
		swinUp(array.size()-1);
	}
	
	private void swinUp(int i) {
		while(i > 1) {
			if(greaterThan(i/2,i))
				swap(i/2,i);
			i /= 2;
		}		
	}
	
	private void swap(int i,int j) {
		T aux = array.get(i);
		array.set(i,array.get(j));
		array.set(j,aux);
	}
	
	private boolean greaterThan(int k, int j) {
		return array.get(k).compareTo(array.get(j)) > 0;
	}
	

	private void swinDown(int i) {//corrige depois
		while(i > 1) {
			if(greaterThan(i/2,i))
				swap(i/2,i);
			i /= 2;
		}		
	}
	
	
}
>>>>>>> 1b2f72bf913c4084e1416cd5dec7e9dbe5fae73e
