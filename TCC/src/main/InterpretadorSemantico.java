package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import models.palavra.Palavra;
import models.palavra.PalavraDAO;

public class InterpretadorSemantico {

	/** ========================= **/
	/**
	 * Gera uma MATRIZ onde cada palavra do documento sera associada a uma lista
	 * de CONCEITOS
	 **/
	public HashMap<String, Double> gerarVetorConceitos(String curriculo) {

		final Vectorizer vetorizador = new Vectorizer("/util/stopwords.txt");
		final PalavraDAO palavraDAO = new PalavraDAO();
		
		// Transforma tudo em letras minusculas
		curriculo = curriculo.toLowerCase();
		
		// Tokeniza o currículo (transforma a String em uma Lista de palavras)
		List<String> curriculoVetorizado = vetorizador.tokenizar(curriculo);
		
		// Remove as StopWords
		curriculoVetorizado = vetorizador.removerStopWords(curriculoVetorizado);

		// Cria um Hash para garantir que nao tenha nenhuma palavra repetida
		HashSet<String> curriculoHashSet = new HashSet<String>(curriculoVetorizado);
		curriculoVetorizado = new ArrayList<String>(curriculoHashSet);
		
        //Pega todos os conceitos relacionados as palavras do curriculo e a frequencia com que eles aparecem
		HashMap<String, Double> vetorFrequenciaConceitos = palavraDAO.pesquisarAllTopConcepts(curriculoVetorizado);
		return vetorFrequenciaConceitos;
		
		/*
		// Cria vetor para conter todos os conceitos e pesos
		List<List<Palavra>> matrizConceitos = new ArrayList<>();

		// Para cada Token gerado do curriculo
		for (String token : curriculoHashSet) {
			// System.out.println("\n~" + token + "~");

			// Calcula o TF baseado no vetor completo (contendo palavras
			// repetidas)
			double tf = calculator.tf(curriculoVetorizado, token);

			// Pesquisa no banco de dados os conceitos mais relacionados com
			// certa palavra (token)
			List<Palavra> topConcepts = palavraDAO.pesquisarTopConcepts(token);

			// Se for um numero, ou nao tiver encontrado conceitos referentes a palavra
			if (token.matches("^[0-9]*$") || topConcepts.size() == 0) {
				// O conceito sera a propria palavra (token)
				Palavra conceito = new Palavra();
				conceito.setArtigo(token);
				conceito.setPalavra(token);
				topConcepts = new ArrayList<>();
				topConcepts.add(conceito);
				vetorFrequenciaConceitos = incrementaQuantidadeVetor(vetorFrequenciaConceitos, conceito.getArtigo());
			} else {
				// Multiplicar o TF da palavra pelos TF-IDFs dos TopConcepts
				for (int i = 0; i < topConcepts.size(); i++) {
					Palavra conceito = topConcepts.get(i);
					// Peso da palavra(tf) * tfidf
					conceito.setTfidf(tf * conceito.getTfidf());
					// System.out.println("[" + concept.getArtigo() + "]{" + concept.getTfidf() + "}, ");
					topConcepts.set(i, conceito);
					vetorFrequenciaConceitos = incrementaQuantidadeVetor(vetorFrequenciaConceitos, conceito.getArtigo());				
				}
			}
			
			// Cada palavra sera associada a uma lista de conceitos
			matrizConceitos.add(topConcepts);
		}	*/	
	}
	
	
	
	/** Imprime o vetor de conceitos de cada Token **/
	public void printMatrizConceitos(List<List<Palavra>> matrizConceitos) {
		int i = 0;
		for (List<Palavra> palavra : matrizConceitos) {
			System.out.println("~" + palavra.get(i).getPalavra() + "~");
			for (Palavra conceito : palavra) {
				System.out.println("[" + conceito.getArtigo() + "]{" + conceito.getTfidf() + "}, ");
			}
			i++;
		}
	}
	
	/** Imprime o conteudo de um hashmap **/
	public static void printMap(HashMap<String, Integer> mp) {
	    Iterator<Entry<String, Integer>> it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String, Integer> pair = it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
