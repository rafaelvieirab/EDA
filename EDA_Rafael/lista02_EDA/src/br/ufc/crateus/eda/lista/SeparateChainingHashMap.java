package br.ufc.crateus.eda.lista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import br.ufc.crateus.eda.st.Map;

//Questão 10 da 2° Lista de Exercicios
public class SeparateChainingHashMap<K extends Comparable<K>, V> implements Map<K, V> {

	public static void main(String[] args) {

		int n = (int) Math.pow(10, 6);
		SeparateChainingHashMap<Integer, Integer> teste = new SeparateChainingHashMap<Integer, Integer>(n / 100);
		Random gerador = new Random();

		for (int i = 0; i < n; i++)
			teste.put(gerador.nextInt(n), n);
		

		teste.lengthLists(n);

		// RELATORIO
		/*
		 * Obs.: 
		 *  >'n' é a quantidade de "tentativas" de inserção na tabela hash. 
		 *  >Como as inserções eram de valores aleatorios, então algumas vezes eram 
		 * inseridas a mesma key mais de uma vez, logo não há  'n' keys inseridas.
		 *  >Separei o relatorio em duas partes: com e sem o reajuste do tamanho do 
		 * array table (metodo "resize()"). 
		 * 
		 */
		
		/*   =>Não havera alteração do tamanho do array table.
		 * 
		 * >Quando n = 1000;
		 *  Tamanho do array = 10;
		 *  Numero de keys inseridas: 627;
		 *  Número de listas encadeadas vazias = 0;
		 *  A menor lista encadeada teve 57 elementos;
		 *  A maior lista encadeada teve 68 elementos;
		 *
		 * >Quando n = 10000;
		 *  Tamanho do array = 100;
		 *  Numero de keys inseridas: 6343;
		 *  Número de listas encadeadas vazias = 0;
		 *  A menor lista encadeada teve 75 elementos;
		 *  A maior lista encadeada teve 53 elementos;
		 *
		 *>Quando n = 100000;
		 *  Tamanho do array = 1000;
		 *  Numero de keys inseridas: 63271 ;
		 *  Número de listas encadeadas vazias = 0;
		 *  A menor lista encadeada teve 78 elementos;
		 *  A maior lista encadeada teve 48 elementos;
		 *
		 * >Quando n = 1000000;
		 *  Tamanho do array = 10000;
		 *  Numero de keys inseridas: 632563;
		 *  Número de listas encadeadas vazias =  0 ;
		 *  A menor lista encadeada teve 79  elementos;
		 *  A maior lista encadeada teve 45 elementos;
		 *
		 */

		 /*  =>Havera alteração do tamanho do array table.
		 * 
		 * >Quando n = 1000;
		 *  Tamanho do array = 80;
		 *  Numero de keys inseridas: 635 ;
		 *  Quantas vezes houve um reajuste no tamanho de table: 3;
		 *  Número de listas encadeadas vazias = 0;
		 *  A menor lista encadeada teve 3 elementos;
		 *  A maior lista encadeada teve 12 elementos;
		 *
		 * >Quando n = 10000;
		 *  Tamanho do array = 800;
		 *  Numero de keys inseridas: 6308; 
		 *  Quantas vezes houve um reajuste no tamanho de table: 3
		 *  Número de listas encadeadas vazias = 0;
		 *  A menor lista encadeada teve 3 elementos;
		 *  A maior lista encadeada teve 13 elementos;
		 *
		 *>Quando n = 100000;
		 *  Tamanho do array = 8000;
		 *  Numero de keys inseridas: 63114;
		 *  Quantas vezes houve um reajuste no tamanho de table: 3;
		 *  Número de listas encadeadas vazias = 1;
		 *  A menor lista encadeada teve 1 elemento;
		 *  A maior lista encadeada teve 13 elementos;
		 *
		 * >Quando n = 1000000;
		 *  Tamanho do array = 80000;
		 *  Numero de keys inseridas: 631728;
		 *  Quantas vezes houve um reajuste no tamanho de table: 3;
		 *  Número de listas encadeadas vazias = 1;
		 *  A menor lista encadeada teve 1 elementos;
		 *  A maior lista encadeada teve 13 elementos;
		 *
		 */

	}

	public void lengthLists(int n) {
		int maior = 0;
		int menor = n;
		int listNUll = 0;
		for (int i = 0; i < table.length; i++) {
			int total = 0;
			if (table[i] == null)
				listNUll++;
			else {
				for (Node r = table[i]; r != null; r = r.next)
					total++;
				if (total > maior) maior = total;
				if (total < menor) menor = total;
			}
		}

		System.out.println("M: "+table.length);
		System.out.println("Num keys: " + size);
		System.out.println("maior: " + maior);
		System.out.println("menor: " + menor);
		System.out.println("nulas: " + listNUll);
		
//		System.out.println("M: "+table.length);
//		System.out.println("Tamanho da maior lista: " + maior);
//		System.out.println("Tamanho da menor lista: " + menor);
//		System.out.println("Numero de listas nulas: " + listNUll);
	}

	private static class Node {
		Object key;
		Object value;
		Node next;

		public Node(Object key, Object value, Node next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	private Node[] table;
	private int size = 0;
	public SeparateChainingHashMap() {
		this(97);
	}

	public SeparateChainingHashMap(int lenght) {
		this.table = new Node[lenght];
	}

	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % table.length;
	}

	@SuppressWarnings("unchecked")
	private void resize(int newLenght) {
		SeparateChainingHashMap<K, V> tmp = new SeparateChainingHashMap<K, V>(newLenght);

		int i;
		for (i = 0; i < table.length; i++)
			for (Node n = table[i]; n != null; n = n.next)
				tmp.put((K) n.key, (V) n.value);
		table = tmp.table;
	}

	public Node getNode(K key) {
		int i = hash(key);
		for (Node n = table[i]; n != null; n = n.next)
			if (key.equals(n.key))
				return n;
		return null;
	}

	@Override
	public void put(K key, V value) {
		Node n = getNode(key);
		if (n == null) {
			int i = hash(key);
			table[i] = new Node(key, value, table[i]);

			this.size++;
//			if (this.size / table.length == 8)
//				resize(table.length * 2);
		} else
			n.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		Node n = getNode(key);
		return (n != null) ? (V) n.value : null;
	}

	@Override
	public void remove(K key) {
		int i = hash(key);
		Node tmp = new Node(null, null, table[i]);

		for (Node n = tmp; n.next != null; n = n.next)
			if (key.equals(n.next.key)) {
				n.next = n.next.next;

				this.size--;
				if (this.size / table.length == 2)
					resize(table.length / 2);
				break;
			}

		table[i] = tmp.next;
	}

	public int getSize() {
		return size;
	}

	@Override
	public Iterable<K> keys() {
		Queue<K> q = new LinkedList<K>();
		collect(table, q);
		return q;
	}

	private void collect(Node[] table, Queue<K> q) {
		for (int i = 0; i < table.length; i++)
			for (Node r = table[i]; r != null; r = r.next)
				q.add((K) r.key);
	}

	@Override
	public boolean contains(K key) {
		return (getNode(key) != null);
	}

}
