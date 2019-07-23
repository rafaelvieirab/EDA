package br.ufc.crateus.eda.st;

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

public class TrieRWay <V>{
	public static final int R = 256;
	
	public static class Node{
		Object value;
		Node[] next = new Node[R];
		
	}
	
	private Node root = new Node();
	
	private Node put(Node r, String str, V value, int i) {
		if(r==null) r = new Node();
		
		if(i== str.length()) r.value = value;
		else {
			char ch = str.charAt(i);
			r.next[ch] = put (r.next[ch], str, value, i+1);
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
			return getNode(r.next[ch], str, i+1);
		}
	}
	
	public V get(String key) {
		Node r = getNode(root, key, 0);
		return r == null ? null : ((V) r.value);
	}
	
	public void delete(String str) {
		root = delete(root,str,0); 
	}
	
	private Node delete(Node r, String str, int i) {
		if(r == null) return null;
		
		if(i < str.length()) {
			char c = str.charAt(i);
			r.next[c] = delete(r.next[c],str,i+1);
		}
		
		if(i == str.length()) r.value = null;
		
		for(int j = 0; j< R; j++) {
			if(r.next[j] != null  || r.value != null)
				return r;
		}	
		return null;
	}

	public Iterable<String> keys(){
		Queue<String> q = new LinkedList<String>();
		collect(root,"",q);
		return q;
	}
	
	private void collect(Node r, String acc, Queue<String> q){
		if(r == null) return;
		
		if(r.value != null) q.add(acc);
		
		for(char ch= 0; ch< R; ch++)
			if(r.next[ch]!= null)
				collect(r.next[ch],acc+ch,q);
	}


	//Retorna todas as chaves que começam com um determinado prefixo
	public Iterable<String> keysWithPrefix(String prefix){
		Node r = getNode(root, prefix,0);
		Queue<String> q = new LinkedList<String>();
		collect(r,prefix,q);
		return q;
	}
	
	//A maior chave que é prefixo de str
	public String longestPrefixOf(String str) {
		int length = search(root,str, 0,0);
		String longPrefix = str.substring(0, length); 
		return longPrefix;
	}
	
	private int search(Node r,String query, int d, int length) {
		if(r == null || d == query.length()) return length;
		
		if(r.value != null) length = d;
		
		char ch = query.charAt(d);
		return search(r.next[ch],query, d+1, length);
	}
	
	@SuppressWarnings("rawtypes")
	public Iterable<String> keysThatMatch(String str) {
		Queue<String> q = new LinkedList<String>();
		keysThatMatch(root, str, "", 0, q);
		return q;
	}
	
	@SuppressWarnings("unchecked")
	private void keysThatMatch(Node r, String str, String acc, int d, Queue<String> q) {
		if(r == null) return;
		
		if(d == str.length()) {
			if(r.value != null) q.add(acc);
			return;
		}
		
		char ch = str.charAt(d);
		if(ch != '?') keysThatMatch(r.next[ch],str,acc+ch,d+1,q);
		else {
			for(ch = 0; ch<R ;ch++) 
				if(r.next[ch] != null) 
					keysThatMatch(r.next[ch],str,acc+ch,d+1,q);
		}
	}
	
	public String floor(String str) {
		return "";
	}

	public String ceiling(String str) {
		return "";
	}

	public String rank(String str1, String str2) {
		return "";
	}

	public void select() {
		
	}
		
}