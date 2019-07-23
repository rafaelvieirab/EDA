package br.ufc.crateus.eda.hash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.st.Map;

public class SeparateChainingHashMap<K extends Comparable<K>, V> implements Map<K, V> {

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

	public void verificadorOrtografico() {
		SeparateChainingHashMap<String, String> tmp = dicionario();
		if (tmp == null)
			return;
		try {
			File arq = new File("pessoal.txt");
			FileReader fileReader = new FileReader(arq);
			BufferedReader br = new BufferedReader(fileReader);

			int numLinha = 1;
			String lines = br.readLine();

			while (lines != null) {
				String words[] = lines.split(" ");
				for (int i = 0; i < words.length; i++) {
					if (tmp.get(words[i]) == null) {
						System.out.println("\nErro na " + (i + 1) + "° palavra da linha " + numLinha);
						System.out.println("Palavras obtidas atraves da ");
						adicaoChar(words[i], tmp);
						removeChar(words[i], tmp.dicionario());
						inverteChar(words[i], tmp.dicionario());
					}
				}
				numLinha++;
				lines = br.readLine();
			}

			fileReader.close();
			br.close();
		} catch (IOException e) {
			System.out.println("Erro ao abrir arquivo pessoal.txt");
		}
	}

	public SeparateChainingHashMap<String, String> dicionario() {
		try {
			File arq = new File("dicionario.txt");
			FileReader fileReader = new FileReader(arq);
			BufferedReader br = new BufferedReader(fileReader);

			SeparateChainingHashMap<String, String> tmp = new SeparateChainingHashMap<String, String>();

			String linha = br.readLine();
			;
			while (linha != null) {
				String words[] = linha.split(" ");
				for (int i = 0; i < words.length; i++)
					tmp.put(words[i], words[i]);
				linha = br.readLine();
			}
			fileReader.close();
			br.close();
			return tmp;
		} catch (IOException e) {
			System.out.println("Falha ao ler o arquivo dicionario.txt");
		}
		return null;
	}

	private void adicaoChar(String word, SeparateChainingHashMap<String, String> tmp) {
		System.out.print("	Adicao de um caractere: ");
		for (int j = 0; j <= word.length(); j++) {
			String word1 = word.substring(0, j);
			String word2 = word.substring(j, word.length());

			for (char i = 0; i < 256; i++) {
				String newWord = word1 + i + word2;
				if (tmp.contains(newWord))
					System.out.print(newWord + " ");
			}
		}
	}

	private void removeChar(String word, SeparateChainingHashMap<String, String> tmp) {
		System.out.print("\n	Remoção de um caractere: ");
		for (int i = 0; i < word.length(); i++) {
			String word1 = word.substring(0, i);
			String word2 = word.substring(i + 1, word.length());
			String newWord = word1 + word2;

			if (tmp.contains(newWord))
				System.out.print(newWord + " ");
		}
	}

	private void inverteChar(String word, SeparateChainingHashMap<String, String> tmp) {
		System.out.print("\n	Inversão de dois caracteres: ");
		for (int i = 0; i < word.length() - 1; i++) {
			String word1 = word.substring(0, i);
			String word2 = word.substring(i + 2, word.length());
			String newWord = word1 + word.charAt(i + 1) + word.charAt(i) + word2;

			if (tmp.contains(newWord))
				System.out.print(newWord + " ");
		}
	}

	// Q3
	public int encontraString(String p, String a) {
		int tamP = p.length();
		if (tamP == 0)
			return 0;

		int hashP = p.hashCode();
		for (int i = 0; i <= a.length() - tamP; i++) {
			String tmp = a.substring(i, i + tamP);
			if ((hashP == tmp.hashCode()) && (p.compareTo(tmp) == 0))
				return i;
		}
		return -1;
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
			if (this.size / table.length == 8)
				resize(table.length * 2);
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
	
	@Override
	public Iterable<K> keys() {
		Queue<K> q = new LinkedList<K>();
		collect(table,q);
		return q;
	}
	
	private void collect(Node[] table,Queue<K> q) {
		for(int i=0 ; i < table.length ; i++) 
			for(Node r = table[i]; r != null; r = r.next) 
				q.add((K) r.key);
	}
	
	@Override
	public boolean contains(K key) {
		return (getNode(key) != null);
	}
	
	//Somente para a questão 2 da lista de EDA 
	public Integer[] sumValue() {
		Integer[] values = new Integer[table.length];
		int soma;
		
		for(int i = 0; i < values.length ; i++) {
			soma = 0;
			for(Node n = table[i]; n!= null; n = n.next)
				soma += (Integer) n.value;
			values[i] = soma;
		}
		return values;
	}
	
}
