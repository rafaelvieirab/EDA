package br.ufc.crateus.eda.tries;

import java.util.LinkedList;
import java.util.Queue;

public class TrieTernariaMap <V>{
	class Node{
		private char ch;
		private Object value; 
		Node left, right, mid;
		
		public Node(char ch) {
			this.ch = ch;
			this.value = value;
		}
	}
	private Node root = null;
	
	public boolean contains(String str) {
		return get(str)!=null;
	}
	
	public V get(String str) {
		Node r = getNode(root, str, 0);
	    if(r != null) 
	    	return (V) r.value;
		return null;
	}
	
	private Node getNode(Node r, String str, int d) {
		if(r == null) return null;
		
		char ch = str.charAt(d);
		if(ch > r.ch) return  getNode(r.right, str, d);
		else if(ch < r.ch) return  getNode(r.left, str, d);
		else {
			if(d == str.length()-1) return r;
			return getNode(r.mid, str, d+1);
		}
	}
	
	public void put(String str, V value) {
		root = put(root, str, value ,0);
	}
	
	public Node put(Node r, String str, V value, int d) {
		char ch = str.charAt(d);
		if(r == null) {
			r = new Node(ch);
			r.ch = ch;
		}
		if(ch > r.ch) r.right =  put(r.right, str, value, d);
		else if(ch < r.ch) r.left =  put(r.left, str, value, d);
		else{
			if(d == str.length()-1) r.value = value;
			else r.mid =  put(r.mid, str, value, d+1);
		}
		return r;
	}
	public void delete(String str) {
		root = delete(root,str,0); 
	}
	
	private Node delete(Node r, String str, int d) {
		if(r == null) return null;
		char ch = str.charAt(d);
		
		if(ch > r.ch) r.right =  delete(r.right, str, d);
		else if(ch < r.ch) r.left =  delete(r.left, str, d);
		else{
			if(d == str.length()-1) { 
				r.value = null;
				if(r.mid != null) return r;//pois ainda existe palavras para baixo
				return null;
			}
			else {
				r.mid =  delete(r.mid, str, d+1);
				if(r.mid == null) {
					if(r.left != null) {
						r.mid = r.left;
						r.left = null;
					}else {
						r.mid = r.right;
						r.right = null;
					}
					return r;
				}
			}
		}
		return r;
	}
	//Questão 13
	public int numSubStrings(String texto, int l) {
		TrieRWay<Integer> rWay = new TrieRWay<Integer>();
		int qtd = 0;
		for(int i = 0; i < texto.length() - l ; i++) {
			String aux = texto.substring(i,i+l);
			if(rWay.get(aux) == null) {
				qtd++;
				rWay.put(aux,qtd);
			}
		}	
		return qtd;
	}
	//Questão 14
		public void numCadaSubStrings(String texto, int l) {
			TrieTernariaMap<Integer> ternaria = new TrieTernariaMap<Integer>();
			
			for(int i = 0; i <= texto.length() - l ; i++) {
				String aux = texto.substring(i,i+l);
				System.out.println(aux);
				Integer ocorre = ternaria.get(aux);  
				
				if(ocorre == null)  ternaria.put(aux,1);
				else ternaria.put(aux,ocorre+1);
			}	
			
			percorre(ternaria.root,"");
		}
		//ajeitar isso
		private void percorre(TrieTernariaMap<Integer>.Node root2,String acc) {
			if(root2 == null) return;
		
			percorre(root2.left,acc);
			percorre(root2.right,acc);
			
			if(root2.value != null) System.out.println("A chave "+acc+root2.ch+" aparece "+
										root2.value+" vezes");
			percorre(root2.mid,acc+root2.ch);			
		}
		

	public Iterable<String> keys(){
		Queue<String> q = new LinkedList<String>();
		collect(root,"",q);
		return q;
	}
	
	private void collect(Node r, String acc, Queue<String> q){
		if(r == null) return;

		if(r.value != null) q.add(acc+r.ch);
		
		collect(r.left, acc,q);
		collect(r.right, acc,q);
		collect(r.mid, acc+r.ch,q);
		
	}
	
	public Iterable<String> keysWithPrefix(String prefix){
		Node r = getNode(root, prefix,0);
		Queue<String> q = new LinkedList<String>();
		if(r != null ) collect(r.mid,prefix,q);
		return q;
	}

	public String longestPrefixOf(String str) {
		int length = search(root,str, 0,0);
		String longPrefix = str.substring(0, length+1); 
		return longPrefix;
	}
	
	private int search(Node r,String query, int d, int length) {
		if(r == null) return length;
		if (query.length() == d) return length;
		
		char ch = query.charAt(d);
		if(ch < r.ch) return search(r.left,query, d, length);
		else if(ch > r.ch) return search(r.right,query, d, length);
		else {
			if(r.value != null) length = d;
			return search(r.mid,query, d+1, length);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Iterable<String> keysThatMatch(String str) {
		//caractere coringa sera representado por '.';
		Queue<String> q = new LinkedList<String>();
		keysThatMatch(root, str, "", 0, q);
		return q;
	}
	
	@SuppressWarnings("unchecked")
	private void keysThatMatch(Node r, String str, String acc, int d, Queue<String> q) {
		if(r == null || d >= str.length()) return;
		
		char ch = str.charAt(d);
		if(ch < r.ch || ch == '.') keysThatMatch(r.left, str, acc, d, q);
		if(ch > r.ch || ch == '.') keysThatMatch(r.right, str, acc, d, q);
		if(ch == r.ch || ch == '.')  keysThatMatch(r.mid, str, acc +r.ch , d+1 ,q);
			
		if(ch != r.ch && ch != '.') return;	
		if(r.value != null && d == str.length()-1) q.add(acc + r.ch);
	}
	
	//Questão 12 da lista de EDA
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
	
	//Questão 12 da lista de EDA
	public String ceiling(String str) {//retorna a maior String que é menor ou igual a str
		return longestPrefixOf(str);
	}
	/*
	//Questão 12 da lista de EDA
	public Iterable<String> rank(String str1, String str2) {
		Queue<String> q = new LinkedList<>();
		if(str1.charAt(0) < str2.charAt(0))
			rankAux(root, q, "", str1, str2, 0);
		else
			rankAux(root, q, "", str2, str1, 0);
		return q;
	}
	
	//Questão 12 da lista de EDA
	private void rankAux(Node r, Queue<String> q, String acc, String str1, String str2, int d) {
		if (r == null) return;

		if (d >= str1.length() && d >= str2.length()) {
			collect(r, acc, q);
			return;
		}
		char ch1 = str1.charAt(d);
		char ch2 = str1.charAt(d);
		
		if(ch1 < r.ch < ch2) r.right =  put(r.right, str, value, d);
		if(r.ch < ch1) r.left =  put(r.left, str, value, d);
		else{
			if(d == str.length()-1) r.value = value;
			else r.mid =  put(r.mid, str, value, d+1);
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
	
	//Questão 2 da lista de EDA
	public void select() {
		
	}
	*/
}
