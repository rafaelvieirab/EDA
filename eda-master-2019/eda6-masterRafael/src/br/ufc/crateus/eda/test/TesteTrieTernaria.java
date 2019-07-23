package br.ufc.crateus.eda.test;

import java.util.Arrays;

import br.ufc.crateus.eda.tries.TrieRWay;
import br.ufc.crateus.eda.tries.TrieTernariaMap;

public class TesteTrieTernaria {

	public static void main(String[] args) {
		TrieTernariaMap<Integer> trieT = new TrieTernariaMap<Integer>();
		
		trieT.put("shells", 0);
		trieT.put("sells", 1);
		trieT.put("by", 2);
		trieT.put("are", 3);
		trieT.put("the", 4);
		trieT.put("she", 5);
		trieT.put("c", 6);
		trieT.put("shelss", 7);
		trieT.put("cachorro", 8);
		
		Iterable<String> words  = trieT.keys();
		for(String str : words)
			System.out.print(str+" ");
		
		String s = "cachorro";
		System.out.println("\n"+trieT.get(s));
		trieT.delete(s);
		System.out.println(trieT.get(s)+"-------------------------------");
		
		words  = trieT.keys();
		for(String str : words)
			System.out.print(str+" ");
		
		
		TrieRWay<Integer> trieR = new TrieRWay<Integer>();
		
//		trieR.put("shells", 0);
//		trieR.put("sells", 1);
//		trieR.put("by", 2);
//		trieR.put("are", 3);
//		trieR.put("the", 4);
//		trieR.put("she", 5);
//		trieR.put("c", 6);
//		trieR.put("shellss", 7);
//		trieR.put("cachorro", 8);
//		
//		Iterable<String> words = trieR.rank("a","z");
//		for(String w : words)
//			System.out.println(w);
	
	}
}
