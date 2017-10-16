package main.interpretador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import models.artigo.ArtigoDAO;
import models.palavra.PalavraDAO;

public class InterpretadorSemantico {

	/**
	 * Gera uma MATRIZ onde cada palavra do documento sera associada a uma lista
	 * de CONCEITOS
	 **/
	public HashMap<String, Double> gerarVetorConceitos(String curriculo) {

		final Vectorizer vetorizador = new Vectorizer("/util/stopwords.txt");
		final PalavraDAO palavraDAO = new PalavraDAO();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<HashMap<String, Double>> resultThread1;
		Future<HashMap<String, Double>> resultThread2;
		HashMap<String, Double> ngramsArray = null;
		HashMap<String, Double> topConcepts = null;
		HashMap<String, Double> vetorFrequenciaConceitos = new HashMap<>();
		
		// Transforma tudo em letras minusculas
		final String curriculoTexto = curriculo.toLowerCase();

		resultThread1 = executor.submit(
				new Callable<HashMap<String, Double>>() {	
					public HashMap<String, Double> call() throws Exception {
						// Gera N Grams com os tokens
						List<String> ngramsArray = new NGramCalculator().vetorizar(6, curriculoTexto);
						// Remove palavras repitidas
						ngramsArray = removePalavrasRepetidas(ngramsArray);
						// Procura todos os titulos de artigos que tem o mesmo nome dos N-GRAMs
						return new ArtigoDAO().procurarTodosTitulosRelacionados(ngramsArray);
					}
				}
		);
		
		resultThread2 = executor.submit(
				new Callable<HashMap<String, Double>>() {	
					public HashMap<String, Double> call() throws Exception {
						// Tokeniza o currículo (transforma a String em uma Lista de palavras)
						List<String> curriculoVetorizado = vetorizador.tokenizar(curriculoTexto);
						// Remove as StopWords
						curriculoVetorizado = vetorizador.removerStopWords(curriculoVetorizado);
						// Remove palavras repitidas
						curriculoVetorizado = removePalavrasRepetidas(curriculoVetorizado);
						//Pega todos os conceitos mais relacionados as palavras do curriculo e a frequencia com que eles aparecem
						return palavraDAO.pesquisarAllTopConcepts(curriculoVetorizado);
					}
				}
		);
		
		try {
			 ngramsArray = resultThread1.get();
			 topConcepts = resultThread2.get();
		} catch (Exception e) {
		    System.out.println(e.getMessage());
		}
		executor.shutdown();
		
		vetorFrequenciaConceitos.putAll(ngramsArray);
		System.out.println(vetorFrequenciaConceitos);
		
		vetorFrequenciaConceitos.putAll(topConcepts);
		System.out.println(vetorFrequenciaConceitos);
		
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
	
	// Cria um Hash para garantir que nao tenha nenhuma palavra repetida
	public List<String> removePalavrasRepetidas(List<String> arrayPalavras) {
		HashSet<String> curriculoHashSet = new HashSet<String>(arrayPalavras);
		return (new ArrayList<String>(curriculoHashSet));
	}
			
	

}
