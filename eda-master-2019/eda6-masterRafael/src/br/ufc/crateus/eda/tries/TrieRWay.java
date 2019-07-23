package br.ufc.crateus.eda.tries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TrieRWay<V> {
	public static final int R = 256;

	public static class Node {
		Object value;
		Node[] next = new Node[R];
	}

	private Node root = new Node();

	private Node put(Node r, String str, V value, int i) {
		if (r == null)
			r = new Node();

		if (i == str.length())
			r.value = value;
		else {
			char ch = str.charAt(i);
			r.next[ch] = put(r.next[ch], str, value, i + 1);
		}
		return r;
	}

	public void put(String str, V value) {
		root = put(root, str, value, 0);
	}

	private Node getNode(Node r, String str, int i) {
		if (r == null)
			return null;
		if (i == str.length()) {
			return r;
		} else {
			char ch = str.charAt(i);
			return getNode(r.next[ch], str, i + 1);
		}
	}

	public V get(String key) {
		Node r = getNode(root, key, 0);
		return r == null ? null : ((V) r.value);
	}

	public void delete(String str) {
		root = delete(root, str, 0);
	}

	private Node delete(Node r, String str, int i) {
		if (r == null) return null;
		
		if (i == str.length()) r.value = null;
		else {
			char c = str.charAt(i);
			r.next[c] = delete(r.next[c], str, i + 1);
		}
		
		if(r.value != null) return r;
		
		for (int j = 0; j < R; j++) 
			if (r.next[j] != null)
				return r;
		
		return null;
	}

	public Iterable<String> keys() {
		Queue<String> q = new LinkedList<String>();
		collect(root, "", q);
		return q;
	}

	private void collect(Node r, String acc, Queue<String> q) {
		if (r == null)
			return;

		if (r.value != null)
			q.add(acc);

		for (char ch = 0; ch < R; ch++)
			if (r.next[ch] != null)
				collect(r.next[ch], acc + ch, q);
	}

	public void consulta(String str) {
		String s = str.toLowerCase();
		TrieRWay<Integer> trw = lerArquivo();
		Iterable<String> filmes;

		System.out.println("(1)-Títulos dos filmes que tenham" + s + " como prefixo:");
		filmes = trw.keysWithPrefix(s);
		for (String nome : filmes)
			System.out.println(nome);

		System.out.println("(2)-Mais longo prefixo de " + s + "  que seja um título de filme:");
		String preffix = trw.longestPrefixOf(s);
		System.out.println(preffix);

		System.out.println("(3)-Títulos dos filmes que casam com " + s + ": ");
		filmes = trw.keysThatMatch(s);
		for (String nome : filmes)
			System.out.println(nome);

	}

	private TrieRWay<Integer> lerArquivo() {
		try {
			File arq = new File("movies.txt");
			FileReader fileReader = new FileReader(arq);
			BufferedReader br = new BufferedReader(fileReader);

//			InputStream in = new FileInputStream("movies.txt");
//			Scanner scan = new Scanner(in);
//			
			TrieRWay<Integer> trw = new TrieRWay<Integer>();

			String linha = br.readLine();
			// String linha = scan.nextLine();

			while (linha != null) {
				// while (scan.hasNextLine()){
				String aux[] = linha.split("/");
				String a = aux[0].substring(0, aux[0].length() - 6);
				String filme = conversao(a.trim());
				trw.put(filme, 0);
				linha = br.readLine();
				// linha = scan.nextLine();
			}

//			scan.close();
//			in.close();
			fileReader.close();
			br.close();

			return trw;
		} catch (IOException e) {
			System.out.println("Falha ao ler o arquivo movies.txt");
		}
		return null;
	}

	private String conversao(String str) {
		char[] filme = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if ((filme[i] >= 'a' && filme[i] <= 'z') || filme[i] == ' ' || (filme[i] >= '0' && filme[i] <= '9'))
				continue;
			else if (filme[i] >= 'A' && filme[i] <= 'Z')
				filme[i] += 32;
			else if (filme[i] == 'Ç' || filme[i] == 'ç')
				filme[i] = 'c';
			else if (filme[i] == 'ñ' || filme[i] == 'Ñ')
				filme[i] = 'n';
			else if (filme[i] == 'ü' || filme[i] == 'û' || filme[i] == 'ù' || filme[i] == 'Ü' || filme[i] == 'ú'
					|| filme[i] == 230 || (filme[i] >= 'Ú' && filme[i] <= 'Ù'))
				filme[i] = 'u';
			else if (filme[i] == 'É' || filme[i] == 'é' || (filme[i] >= 'ê' && filme[i] <= 'è')
					|| (filme[i] >= 'Ê' && filme[i] <= 'È'))
				filme[i] = 'e';
			else if ((filme[i] == 'â' && filme[i] <= 134) || filme[i] == 142 || filme[i] == 143 || filme[i] == 'á'
					|| filme[i] == 166 || (filme[i] >= 'Á' && filme[i] <= 'À') || filme[i] == 'ã' || filme[i] == 'Ã')
				filme[i] = 'a';
			else if ((filme[i] == 'ï' && filme[i] <= 141) || filme[i] == 'í')
				filme[i] = 'i';
			else if ((filme[i] == 'ô' && filme[i] <= 'ò') || filme[i] == 'Ö' || filme[i] == 'ó' || filme[i] == 167
					|| filme[i] == 208 || filme[i] == 'Ó' || (filme[i] >= 'Ô' && filme[i] <= 'Õ'))
				filme[i] = 'o';
			else if (filme[i] == 'ÿ' || filme[i] == 'ý' || filme[i] == 'Ý')
				filme[i] = 'y';
			else if (filme[i] == 158)
				filme[i] = 'x';
			else if (filme[i] == 159)
				filme[i] = 'f';
			else if (filme[i] == 169)
				filme[i] = 'r';
			else if (filme[i] == 184 || filme[i] == 189)
				filme[i] = 'c';
			else if (filme[i] == 251)
				filme[i] = '1';
			else if (filme[i] == 252)
				filme[i] = '2';
			else if (filme[i] == 253)
				filme[i] = '3';
			else
				filme[i] = '?';
		}
		String nome = "";
		for (int i = 0; i < str.length(); i++) {
			nome += filme[i];
		}
		return nome;
	}

	// Questão 13
	public int numSubStrings(String texto, int l) {
		TrieRWay<Integer> rWay = new TrieRWay<Integer>();
		int qtd = 0;
		for (int i = 0; i < texto.length() - l; i++) {
			String aux = texto.substring(i, i + l);
			if (rWay.get(aux) == null) {
				qtd++;
				rWay.put(aux, qtd);
			}
		}
		return qtd;
	}

	// Questão 14
	public void numCadaSubStrings(String texto, int l) {
		TrieRWay<Integer> rWay = new TrieRWay<Integer>();

		for (int i = 0; i <= texto.length() - l; i++) {
			String aux = texto.substring(i, i + l);
			Integer ocorre = rWay.get(aux);

			if (ocorre == null)
				rWay.put(aux, 1);
			else
				rWay.put(aux, ocorre + 1);
		}
		percorre(rWay.root, "");
	}

	// Questão 14
	private void percorre(Node r, String acc) {
		if (r == null)
			return;
		if (r.value != null)
			System.out.println("A chave " + acc + " aparece " + r.value + " vezes");

		for (char ch = 0; ch < R; ch++)
			if (r.next[ch] != null)
				percorre(r.next[ch], acc + ch);
	}

	// Retorna todas as chaves que começam com um determinado prefixo
	public Iterable<String> keysWithPrefix(String prefix) {
		Node r = getNode(root, prefix, 0);
		Queue<String> q = new LinkedList<String>();
		collect(r, prefix, q);
		return q;
	}

	// A maior chave que é prefixo de str
	public String longestPrefixOf(String str) {
		int length = search(root, str, 0, 0);
		String longPrefix = str.substring(0, length);
		return longPrefix;
	}

	private int search(Node r, String query, int d, int length) {
		if (r == null)
			return length;

		if (r.value != null) length = d;
		if (d == query.length()) return length;

		char ch = query.charAt(d);
		return search(r.next[ch], query, d + 1, length);
	}

	@SuppressWarnings("rawtypes")
	public Iterable<String> keysThatMatch(String str) {
		Queue<String> q = new LinkedList<String>();
		keysThatMatch(root, str, "", 0, q);
		return q;
	}

	@SuppressWarnings("unchecked")
	private void keysThatMatch(Node r, String str, String acc, int d, Queue<String> q) {
		if (r == null)
			return;

		if (d == str.length()) {
			if (r.value != null)
				q.add(acc);
			return;
		}

		char ch = str.charAt(d);
		if (ch != '.')
			keysThatMatch(r.next[ch], str, acc + ch, d + 1, q);
		else {
			for (ch = 0; ch < R; ch++)
				if (r.next[ch] != null)
					keysThatMatch(r.next[ch], str, acc + ch, d + 1, q);
		}
	}

	// Questão 12 da lista de EDA
	public String floor(String str) {// retorna a menor String que é maior ou igual a str
		Iterable<String> words = keysWithPrefix(str);

		String menor = "";

		for (String s : words) {
			if (str.compareTo(s) == 0)
				return str;// se a string é igual, então é a menor possivel
			if (menor == "")
				menor = s;
			if (s.length() <= menor.length())
				menor = s;
		}
		return menor;
	}

	// Questão 12 da lista de EDA
	public String ceiling(String str) {// retorna a maior String que é menor ou igual a str
		return longestPrefixOf(str);
	}

	// Questão 12 da lista de EDA
	public Iterable<String> rank(String str1, String str2) {
		Queue<String> q = new LinkedList<>();
		if(str1.charAt(0) < str2.charAt(0))
			rankAux(root, q, "", str1, str2, 0);
		else
			rankAux(root, q, "", str2, str1, 0);
		return q;
	}

	// Questão 12 da lista de EDA
	private void rankAux(Node r, Queue<String> q, String acc, String str1, String str2, int d) {
		if (r == null)
			return;

		if (d >= str1.length() && d >= str2.length()) {
			collect(r, acc, q);
			return;
		}
		if (r.value != null) q.add(acc);
		
		char i = 0;
		char j = R;
		if (d < str1.length()) i = str1.charAt(d);
		if (d < str2.length()) j = str2.charAt(d);

		for (char ch = i; ch < j; ch++)
			if (r.next[ch] != null)
				rankAux(r.next[ch], q, acc + ch, str1, str2, d + 1);
	}

	// Questão 12 da lista de EDA
	public void select() {

	}

}