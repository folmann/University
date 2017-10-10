package old;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import models.artigo.Artigo;
import models.artigo.ArtigoDAO;
import models.palavra.Palavra;
import models.palavra.PalavraDAO;
import util.TFIDFCalculator;

public class VectorizerOriginal {

	private String stopwordsPath;
	private TFIDFCalculator calculator = new TFIDFCalculator();
	private PalavraDAO palavraDAO = new PalavraDAO();

	// Construtor, recebe a lista de StopWords
	public VectorizerOriginal(String stopwordsPath) {
		this.stopwordsPath = stopwordsPath;
	}

	/** ========================= **/
	/** Limpa e TOKENIZA o Texto **/
	public List<String> tokenizar(String texto) {
		// Remove caracteres especiais, mas mantem letras, numeros e acentos
		texto = texto.replaceAll("-", " ");
		texto = texto.replaceAll("[^a-zA-Z0-9 „ı√’·¡‡¿ƒ‰È…ÌÕÛ”˙⁄‚ÍÓÙ˚¬ Œ‘€‡ËÏÚ˘¿»Ã“Ÿ‰ÎÔˆ¸ƒÀœ÷‹Á«∫™π≤≥]", "");
		texto = texto.replaceAll("\\s+", " ");
		texto = texto.trim();

		// Vetoriza o texto (transforma o texto em uma lista de palavras
		String[] textSplit = texto.split(" ");

		// Transforma o vetor em um array
		HashSet<String> palavrasHashSet = new HashSet<String>(Arrays.asList(textSplit));
		List<String> vetorPalavras = new ArrayList<String>(palavrasHashSet);

		// Retorna um array de palavras
		return vetorPalavras;
	}

	/** ========================= **/
	/** Remove todas as StopWords **/
	public List<String> removerStopWords(List<String> document) {

		Scanner s = null;

		// Abre o arquivo com as stopwords
		try {
			URL url = getClass().getResource(stopwordsPath);
			s = new Scanner(new FileInputStream(url.getPath()), "UTF-8");
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
	 * texto em uma lista de tokens(palavras) cria uma lista com todos os
	 * artigos ja tokenizados (lista de uma lista de palavras) e por fim calcula
	 * o TF-IDF
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
			List<String> tokens = this.tokenizar(artigo.getText());

			// Tira as Stopwords
			tokens = this.removerStopWords(tokens);

			// Ultima PosiÁ„o È o tÌtulo do artigo
			tokens.add(artigo.getTitle());

			// Adicionar o artigo vetorizado na lista de documentos(artigos
			// tokenizados)
			documents.add(tokens);
		}
		return documents;
	}

	/** ========================= **/
	/** Calcula o TF-IDF de cada palavra **/
	public void calculaTF(List<List<String>> documents) {

		// Para cada documento(artigo tokenizado) da lista
		for (List<String> document : documents) {
			System.out.println(document.get(document.size() - 1));
		
			// Para cada palavra da lista, exceto a ultima posicao (que È o
			// tÌtulo do artigo)
			for (String word : document.subList(0, document.size() - 1)) {

				// Monta o objeto Palavra(token)
				Palavra palavra = new Palavra();
				palavra.setPalavra(word);
				palavra.setArtigo(document.get(document.size() - 1));

				// Se ainda nao existe no banco (nao foi calculado)
				if (!(palavra.getPalavra().equals(""))) {
					if (palavraDAO.pesquisarPalavra(palavra).getPalavra() == null) {

						// Calcula o TF-IDF
						double tf = calculator.tf(document, word);
						double idf = calculator.idf(documents, word);
						double tfidf = tf * idf;

						palavra.setTf(tf);
						palavra.setIdf(idf);
						palavra.setTfidf(tfidf);

						// Adiciona no Banco SQL
						palavraDAO.adicionarPalavra(palavra);
					}
				}
			}

		}
	}
	

	
}
