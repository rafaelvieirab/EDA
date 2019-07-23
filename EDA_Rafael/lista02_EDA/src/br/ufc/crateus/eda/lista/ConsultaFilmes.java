package br.ufc.crateus.eda.lista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import br.ufc.crateus.eda.st.TrieRWay;

//Questão 16 da 2° Lista de Exercicios
public class ConsultaFilmes {
	private TrieRWay<Integer> trw;
	
	public ConsultaFilmes() {
		trw = lerArquivo();
	}
	
	public void consulta(int op,String str) {
		String s = conversao(str);
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
			scan.nextLine();
			
			System.out.println("Digite a String: ");
			str = scan.nextLine();
			System.out.println();
			
			long time = System.currentTimeMillis();
			
			search.consulta(opcao,str);
			System.out.println();
			
			System.out.println("Levou "+(System.currentTimeMillis() - time) +" ms");
		}
		
	}
	/*
	 * Obs.: Na classe TrieRWay, mudei o metodo coringa de '.' para '?', pois não existe
	 * ponto no alfabeto que foi definido na questão.
	 * */
	
	//Resultados de consultas:
	
	/* Opção 1:
	 *  > String pesquisada: "100"
	 *  Tempo que levou para realizar a consulta: 25 ms
	 *  Resultados: 
	 *  	"100 girls"
	 *		"100 rifles"
	 *		"1000 convicts and a woman"
	 *  
	 *  > String pesquisada: "Acc"
	 *  Tempo que levou para realizar a consulta: 3 ms
	 *  Resultados: 
	 *  	"acceptable risks"
	 *		"access denied"
	 *		"accident"
	 *  	"accidental meeting"
	 *  	"accidental tourist? the"
	 *  	"accused of murder"
	 *  	"accused? the"
	 *   
	 *   > String pesquisada: "Fol"
	 *  Tempo que levou para realizar a consulta: 1 ms
	 *  Resultados: 
	 * 		"folks?"
	 *		"follow me"
	 *		"follow me home"
	 *  	"follow the bitch"
	 *  	"follow the river"
	 *  	"follow your heart"
	 *  	"folterkammer des dr? fu man chu? die"
	 *  	"following"
	 * */
	
	/* Opção 2:
	 *  > String pesquisada: "Flipper"
	 *  Tempo que levou para realizar a consulta: 1 ms
	 *  Resultados: 
	 *  	"flipper"
	 *  
	 *  > String pesquisada: "Flirtings"
	 *  Tempo que levou para realizar a consulta: 1 ms
	 *  Resultados: 
	 *  	"flirtings"
	 *   
	 *   > String pesquisada: "For Love of the Game "
	 *  Tempo que levou para realizar a consulta: 0 ms
	 *  Resultados: 
	 * 		"for love of the game"
	 * */

	/* Opção 3:
	 *  > String pesquisada: "??????????????????????????????????????????????"
	 *  Tempo que levou para realizar a consulta: 268 ms
	 *  Resultados: 
	 *  	"big one? the great los angeles earthquake? the"
	 *  	"charlie chan and the curse of the dragon queen"
	 *  	"corpi presentano tracce di violenza carnale? i"
	 *  	"everything you always wanted to know about sex"
	 *  	"for love or country? the arturo sandoval story"
	 *  	"gorillas in the mist? the story of dian fossey"
	 *  	"land before time v? the mysterious island? the"
	 *  	"lord of the rings? the return of the king? the"
	 *  	"nightmare on elm street 4? the dream master? a"
	 *  	"rats are coming? the werewolves are here?? the"
	 *  	"star wars? episode v ? the empire strikes back"
	 *  
	 *  > String pesquisada: "a??a"
	 *  Tempo que levou para realizar a consulta: 0 ms
	 *  Resultados: 
	 *  	"anna"
	 *  	"aria"
	 *   
	 *   > String pesquisada: "..:&*&()()(&&¨*¨&¨%¨&%&¨%&¨%*&¨*&¨*&¨"
	 *  Tempo que levou para realizar a consulta: 156 ms
	 *  Resultados: 
	 * 		"adventures of elmo in grouchland? the"
	 * 		"adventures of rocky ? bullwinkle? the"
	 * 		"alice cooper? welcome to my nightmare"
	 * 		"american psycho ii? all american girl
	 * 		"amico? stammi lontano almeno un palmo
	 * 		"another pair of aces? three of a kind
	 * 		"austin powers? the spy who shagged me
	 * 		"batman beyond? return of the joker ?2
	 * 		"best of intimate sessions vol? 2? the
	 * 		"care bears movie ii? a new generation
	 * 		"court?martial of jackie robinson? the
	 * 		"davy crockett? rainbow in the thunder
	 * 		"daydream believers? the monkees story
	 * 		"dead ahead? the exxon valdez disaster
	 * 		"delta force 2? operation stranglehold
	 * 		"eight hundred leagues down the amazon
	 * 		"extreme adventures of super dave? the
	 * 		"fighting dragon vs? deadly tiger? the
	 * 		"fugitive nights? danger in the desert
	 * 		"great northfield? minnesota raid? the
	 * 		"halloween? the curse of michael myers
	 * 		"i still know what you did last summer
	 * 		"in the heat of passion ii? unfaithful
	 * 		"in the line of duty? hunt for justice
	 * 		"jing cha gu shi iii? chao ji jing cha
	 * 		"kaija?ta? no kessen? gojira no musuko
	 * 		"legend of zelda? ocarina of time? the
	 * 		"life ? adventures of santa claus? the
	 * 		"life and times of hank greenberg? the
	 * 		"life and times of judge roy bean? the
	 * 		"madonna live? drowned world tour 2001
	 * 		"moments of truth with stephen ambrose
	 * 		"night in the life of jimmy reardon? a
	 * 		"on the air live with captain midnight
	 * 		"private files of j? edgar hoover? the
	 * 		"roller blade warriors? taken by force
	 * 		"rosencrantz and guildenstern are dead
	 * 		"secret passion of robert clayton? the
	 * 		"sleepaway camp iii? teenage wasteland
	 * 		"year in the death of jack richards? a"
	 * 		"wong fei?hung tsi sam? siwong tsangba"
	 * 		"white fang ii? myth of the white wolf"
	 * 		"virgin queen of st? francis high? the"
	 * 		"un arlarphant aoa trompe arnormarment"
	 * 		"threshold? the blue angels experience"
	 * 		"street fighter ii? the animated movie"
	 * */
}
