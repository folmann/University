package main.interpretador;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import models.artigo.Artigo;
import models.artigo.ArtigoDAO;
import models.palavra.Palavra;
import models.palavra.PalavraDAO;

public class Vectorizer {

	private String stopwordsPath;
	private TFIDFCalculator calculator = new TFIDFCalculator();
	private PalavraDAO palavraDAO = new PalavraDAO();

	/** Construtor, recebe a lista de StopWords **/
	public Vectorizer(String stopwordsPath) {
		this.stopwordsPath = stopwordsPath;
	}


	/** Limpa e TOKENIZA o Texto **/
	public List<String> tokenizar(String texto) {
		// Remove caracteres especiais, mas mantem letras, numeros e acentos
		texto = texto.replaceAll("-", " ");
		texto = texto.replaceAll("[^a-zA-Z „ı√’·¡‡¿ƒ‰È…ÌÕÛ”˙⁄‚ÍÓÙ˚¬ Œ‘€‡ËÏÚ˘¿»Ã“Ÿ‰ÎÔˆ¸ƒÀœ÷‹Á«∫™π≤≥]", "");
		texto = texto.replaceAll("\\s+", " ");
		texto = texto.trim();

		// Vetoriza o texto (transforma o texto em uma lista de palavras
		String[] textSplit = texto.split(" ");

		// Transforma o vetor em um array
		List<String> vetorPalavras = new ArrayList<String>(Arrays.asList(textSplit));
		vetorPalavras.remove(" ");
		vetorPalavras.remove("");
		
		// Retorna um array de palavras
		return vetorPalavras;
	}
	

	/** Remove todas as StopWords **/
	public List<String> removerStopWords(List<String> document) {

		Scanner s = null;

		// Abre o arquivo com as stopwords
		try {
			URL url = getClass().getResource(stopwordsPath);
			s = new Scanner(new FileInputStream(url.getPath()));
		} catch (FileNotFoundException e) {
			System.out.println("Erro: nao foi possivel carregar as StopWords.");
			e.printStackTrace();
		}

		// Coloca as stopwords do arquivo em um array
		List<String> stopwords = new ArrayList<>();
		while (s.hasNext()) {
			stopwords.add(s.next());
		}
		s.close();

		for (String stopword : stopwords) {
			while (document.contains(stopword)) {
				document.remove(stopword);// remove do texto
			}
		}
		return document;
	}

	/**
	 * Vetorizar: limpa os textos armazenados no banco de dados transforma cada
	 * texto em uma lista de tokens(palavras) cria uma lista com todos os artigos ja
	 * tokenizados (lista de uma lista de palavras) e por fim calcula o TF-IDF
	 **/
	public List<List<String>> vetorizarBanco() {

		ArtigoDAO artigoDAO = new ArtigoDAO();

		// Cria uma lista para conter os artigos tokenizados
		List<List<String>> documents = new ArrayList<List<String>>();

		// Pega Todos os Artigos do banco
		List<Artigo> artigos = artigoDAO.selectAll();

		// Para cada artigo..
		for (Artigo artigo : artigos) {

			// Tokenize: Transforma o texto em tokens (palavras)
			List<String> tokens = this.tokenizar(artigo.getTexto());

			// Tira as Stopwords
			tokens = this.removerStopWords(tokens);

			// As 2 ˙ltimas posiÁıes s„o o tÌtulo e id do artigo
			tokens.add(artigo.getTitulo());
			tokens.add(""+artigo.getId());

			// Adicionar o artigo vetorizado na lista de documentos(artigos
			// tokenizados)
			documents.add(tokens);
		}
		return documents;
	}


	/** Calcula o TF de cada palavra **/
	public void calculaTF(List<List<String>> documents) {

		// Para cada documento(artigo tokenizado) da lista
		for (List<String> document : documents) {
			//System.out.println(document.get(document.size() - 2));

			// Para cada palavra da lista, exceto as 2 ultimas posicıes 
			// (que s„o o tÌtulo(-2) e o id(-1) do artigo)
			for (String word : document.subList(0, document.size() - 2)) {

				// Monta o objeto Palavra(token)
				Palavra palavra = new Palavra();
				palavra.setPalavra(word);
				palavra.setArtigo(document.get(document.size() - 2));
				palavra.setArtigoId(Long.parseLong(document.get(document.size() - 1)));

				// Se Palavra n„o for vazia
				if (!(palavra.getPalavra().equals(""))) {
					
					// Calcula o TF-IDF
					double tf = calculator.tf(document, word);
					palavra.setTf(tf);

					// Adiciona no Banco SQL
					palavraDAO.adicionarPalavra(palavra);
				}
			} 

		}
	}


	/** Calcula o TF-IDF de cada palavra **/
	public void calculaTFIDF() {
		PalavraDAO palavraDAO = new PalavraDAO();

		// Pega todas as palavras do banco
		List<Palavra> palavras = palavraDAO.selectAlmostAll();

		// Para cada Palavra
		for (Palavra palavra : palavras) {
			// Calcula o IDF
			int n = palavraDAO.selectCountPalavra(palavra.getPalavra());
			double idf = Math.log(palavraDAO.selectCountAllPalavra() / n);
			// Calcula o TFIDF
			double tfidf = palavra.getTf() * idf;
			// Salva no banco
			palavra.setIdf(idf);
			palavra.setTfidf(tfidf);
			palavraDAO.alterarPalavra(palavra);
		}
	}	

}
