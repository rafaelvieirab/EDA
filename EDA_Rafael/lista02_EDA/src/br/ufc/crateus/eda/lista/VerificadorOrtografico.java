package br.ufc.crateus.eda.lista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.ufc.crateus.eda.st.SeparateChainingHashMap;

//Questão 4 da 2° Lista de Exercicios
public class VerificadorOrtografico {
	
	public void corretorOrtografico() {
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
						removeChar(words[i], tmp);
						inverteChar(words[i], tmp);
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

			SeparateChainingHashMap<String, String> dic = new SeparateChainingHashMap<String, String>();

			String linha = br.readLine();
			
			while (linha != null) {
				String words[] = linha.split(" ");
				for (int i = 0; i < words.length; i++)
					dic.put(words[i], words[i]);
				linha = br.readLine();
			}
			fileReader.close();
			br.close();
			return dic;
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

	public static void main(String[] args) {
		VerificadorOrtografico co = new VerificadorOrtografico();
		co.corretorOrtografico();

	}

}
