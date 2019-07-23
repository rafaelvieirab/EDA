package br.ufc.crateus.eda.test;

import br.ufc.crateus.eda.st.LinkedListMap;
import br.ufc.crateus.eda.st.Map;

public class TestLinkedList {
	public static void main(String[] args) {
		Map<String, Integer> map = new LinkedListMap<>();
		map.put("João", 23);
		map.put("Maria", 33);
		map.put("Paulo", 43);
		map.put("Paulo", 53);
		map.put("Tereza", 63);
		
		map.remove("Tereza");
		
		System.out.println(map.get("Paulo"));
		
		for (String key : map.keys())
			System.out.println(key);
	}
}
