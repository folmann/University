package main.interpretador;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import models.ngram.NGram;
import models.ngram.NGramDAO;
import models.pessoa.Pessoa;
import models.pessoa.PessoaDAO;


public class NGramCalculator {
	
	Vectorizer vetorizador = new Vectorizer("/util/stopwords.txt");
	
    public List<String> ngram(int n, String str) {
        List<String> ngrams = new ArrayList<>();
        
        		
        //String[] words = str.split(" ");
        List<String> words = vetorizador.tokenizar(str);
        words = vetorizador.removerStopWords(words);
        
        for (int i = 0; i < words.size() - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    public String concat(List<String> words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words.get(i));
        return sb.toString();
    }

    public List<List<String>> ngrams(int n, String frase) {
    	List<List<String>> ngramMatriz = new ArrayList<>();
    	List<String> ngram;
        for (int i = n; i > 0; i--) {
        	ngram = ngram(i, frase);
        	ngramMatriz.add(ngram);
        }
        return ngramMatriz;
    }
    
    public List<String> vetorizar(int n, String curriculo) {
    	List<String> ngram;
    	
    	// Tokeniza o currículo (transforma a String em uma Lista de palavras)
    	List<String> curriculoVetorizado = vetorizador.tokenizar(curriculo);
    			
    	// Remove as StopWords
    	curriculoVetorizado = vetorizador.removerStopWords(curriculoVetorizado);
    	
        for (int i = n; i > 0; i--) {
        	ngram = ngram(i, curriculo);
        	for (String frase : ngram) {
        		if(new NGramDAO().verificarNGram(frase)) {
        			for (String splitedWord : Arrays.asList(frase.split(" "))) {
        				if(curriculoVetorizado.contains(splitedWord)) {        					
        					curriculoVetorizado.remove(splitedWord);
        				}        					
					}
        			curriculoVetorizado.add(frase);
        		}				
			}
        }
        return curriculoVetorizado;
    }
    
    public void salvarAllNGrams(int n) {
    	/** Pega todos os curriculos e salva os NGRAMS deles**/
    	NGram ngram = new NGram();
    	
    	List<Pessoa> pessoas = new PessoaDAO().selectAll();
    	for (Pessoa pessoa : pessoas) {
    		List<List<String>> ngramMatriz = ngrams(n, pessoa.getCurriculo());
    		for (List<String> n_gram : ngramMatriz) {
				for (String frase : n_gram) {
					ngram = new NGram();
					ngram.setFrequencia(Collections.frequency(n_gram, frase));
					ngram.setTamanho(n-ngramMatriz.lastIndexOf(n_gram));
					ngram.setConceito(pessoa.getNome());
					ngram.setNgram(frase);					
					new NGramDAO().adicionarNGRAM(ngram);
				}
			}
		}
    	/*List<Artigo> artigos = new ArtigoDAO().selectAll();
    	for (Artigo artigo : artigos) {
    		List<List<String>> ngramMatriz = getN_Grams(6, artigo.getTexto());
    		for (List<String> n : ngramMatriz) {
				for (String frase : n) {
					ngram = new NGram();
					ngram.setFrequencia(Collections.frequency(n_gram, frase));
					ngram.setTamanho(n-ngramMatriz.lastIndexOf(n_gram));
					ngram.setConceito(artigo.getTitulo());
					ngram.setNgram(frase);					
					new NGramDAO().adicionarNGRAM(ngram);
				}
			}
		}*/
    }    
}
