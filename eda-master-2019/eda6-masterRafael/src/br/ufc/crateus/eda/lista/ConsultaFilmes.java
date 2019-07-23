package br.ufc.crateus.eda.lista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import br.ufc.crateus.eda.tries.TrieRWay;

public class ConsultaFilmes {
	private TrieRWay<Integer> trw;
	
	public ConsultaFilmes() {
		trw = lerArquivo();
	}
	
	public void consulta(int op,String str) {
		String s = conversao(str);
		//lerArquivo();
		Iterable<String> filmes;
		
		switch(op) {
			case 1:
				System.out.println("(1)-Títulos dos filmes que tenham "+s+" como prefixo:");
				filmes = trw.keysWithPrefix(s);
				for(String nome : filmes)
					System.out.println(nome);
				break;
			case 2:
				System.out.println("(2)-Mais longo prefixo de "+s+"  que seja um título de filme:");
				String preffix = trw.longestPrefixOf(s);
				System.out.println(preffix);
				break;
			
			case 3:
				System.out.println("(3)-Títulos dos filmes que casam com "+s+": ");
				filmes = trw.keysThatMatch(s);
				for(String nome : filmes)
					System.out.println(nome);
				break;
			
			default:
				System.out.println("\nVALOR INVALIDO!!!");
				break;
		}
		
	}
	
	private TrieRWay<Integer> lerArquivo() {
		try {
			File arq = new File("movies.txt");
			FileReader fileReader = new FileReader(arq);
			BufferedReader br = new BufferedReader(fileReader);
		
			TrieRWay<Integer> tmp = new TrieRWay<Integer>();
			
			String linha = br.readLine();
			
			while (linha != null){
				String aux[] = linha.split("/");
				String aux2 = aux[0].substring(0,aux[0].length()-6);
				String filme = conversao(aux2.trim());
				tmp.put(filme,0);
				linha = br.readLine();
			}		
			fileReader.close();
			br.close();
			return tmp;
		} catch (IOException e) {
	    	System.out.println("Falha ao ler o arquivo movies.txt");
		}
		return null;
	}
	
	private String conversao(String str) {
		str = str.toLowerCase();
		char[] filme = str.toCharArray();
		
		for(int i = 0; i<str.length(); i++) {
			if((filme[i] >= 'a' && filme[i] <= 'z') || filme[i] == ' ' || (filme[i] >= '0' && filme[i] <= '9')) continue;
			else if(filme[i] == 'ç') filme[i]= 'c';
			else if(filme[i] == 'ñ') filme[i]= 'n';
			else if(filme[i] == 'ü' || filme[i] == 'û' || filme[i] == 'ù' || filme[i] == 'Ü' || filme[i] =='ú' || filme[i] == 230 || (filme[i] >= 'Ú' && filme[i] <='Ù')) filme[i]= 'u';
			else if(filme[i] == 'é'  || (filme[i] >= 'ê' && filme[i] <= 'è')) filme[i]= 'e';
			else if((filme[i] == 'â' && filme[i] <= 134 ) || filme[i] == 142 || filme[i] == 143 || filme[i] == 'á' || filme[i] == 166 ||filme[i] == 'ã') filme[i]= 'a';
			else if((filme[i] == 'ï' && filme[i] <= 141) || filme[i] == 'í') filme[i]= 'i';
			else if((filme[i] == 'ô' && filme[i] <= 'ò') || filme[i] == 'Ö' || filme[i] == 'ó' || filme[i] == 167 || filme[i] == 208) filme[i]= 'o';
			else if(filme[i] == 'ÿ' || filme[i] == 'ý') filme[i]= 'y';
			else if(filme[i] == 158) filme[i]= 'x';
			else if(filme[i] == 159) filme[i]= 'f';
			else if(filme[i] == 169) filme[i]= 'r';
			else if(filme[i] == 184 || filme[i] == 189) filme[i]= 'c';
			else if(filme[i] == 251) filme[i]= '1';
			else if(filme[i] == 252) filme[i]= '2';
			else if(filme[i] == 253) filme[i]= '3';
			else filme[i] = '?';
		}
		String nome ="";
		for(int i = 0; i<str.length(); i++) {
			nome += filme[i];
		}
		return nome;
	}
	
	public static void main(String[] args) {

		ConsultaFilmes search = new ConsultaFilmes();
		Scanner scan = new Scanner(System.in);
		int opcao;
		String str;
		
		while(true){
			System.out.println("1 - Títulos dos filmes que tenham s como prefixo:");
			System.out.println("2 - Mais longo prefixo de s  que seja um título de filme:");
			System.out.println("3 - Títulos dos filmes que casam com s: ");
			System.out.println("0 - Sair");
			
			System.out.println("Digite sua opção: ");
			opcao = scan.nextInt();
			if(opcao == 0) break;
			
			System.out.println("Digite a String: ");
			scan.nextLine();
			str = scan.nextLine();
			System.out.println();
			search.consulta(opcao,str);
			System.out.println();
		}
		
	}

}
