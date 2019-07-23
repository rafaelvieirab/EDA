package br.ufc.crateus.eda.test;

import br.ufc.crateus.eda.hash.SeparateChainingHashMap;

public class TesteSeparateChainingHash {

	public static void main(String[] args) {
		SeparateChainingHashMap<String, String> tmp = new SeparateChainingHashMap<String, String>();
		
		/*
			int pos = tmp.encontraString("ixe","peipeixe");
			System.out.println("Posição " + pos);
		*/
		tmp.verificadorOrtografico();
		
	}

}
