package br.ufc.crateus.eda.test;

import br.ufc.crateus.eda.st.TreeAVLMap;

public class TesteAVL {

	public static void main(String[] args) {
		TreeAVLMap<Integer,Integer> avl = new TreeAVLMap<Integer,Integer>();
		
		for(int i=0;i<7;i++) {
			avl.put(i, i);
		}
		
		System.out.println(avl.estaBalanceada());
	}

}
