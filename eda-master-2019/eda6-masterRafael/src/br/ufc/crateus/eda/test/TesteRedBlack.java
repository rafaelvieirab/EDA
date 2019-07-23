package br.ufc.crateus.eda.test;

import br.ufc.crateus.eda.st.RedBlackMap;

public class TesteRedBlack {

	public static void main(String[] args) {
		RedBlackMap<Integer,Character> rbm = new RedBlackMap<Integer,Character>();
		
		rbm.put(0, 'S');
		rbm.put(1, 'E');
		rbm.put(2, 'A');
		rbm.put(3, 'R');
		rbm.put(4, 'C');
		rbm.put(5, 'H');
		rbm.put(6, 'X');
		rbm.put(7, 'P');
		rbm.put(4, 'L');
		
		System.out.println(rbm.heightBlack());
		
	}

}
