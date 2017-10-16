package main.similaridade;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import main.interpretador.InterpretadorSemantico;
import models.pessoa.Pessoa;
import models.similaridade.Similaridade;
import models.similaridade.SimilaridadeDAO;
import util.parsers.XmlLattesParser;

public class CalcularSimilaridade {
	
	public CalcularSimilaridade(String pathCurriculos) throws IOException {    
		
		/** Inicializa algumas variaveis **/
		//InterpretadorSemantico interpretador = new InterpretadorSemantico();
		CosineCalculator cosineCalculator = new CosineCalculator();
		SimilaridadeDAO similaridadeDAO = new SimilaridadeDAO();
		InterpretadorSemantico interpretador = new InterpretadorSemantico();
		Similaridade similaridade;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<HashMap<String, Double>> resultThread1;
		Future<HashMap<String, Double>> resultThread2;
		HashMap<String, Double> vetor1 = null;
		HashMap<String, Double> vetor2 = null;
		/** Pega todos os curriculos e transforma em uma lista de objetos Pessoa**/
		List<Pessoa> pessoas = new XmlLattesParser().listFilesForFolder(new File(pathCurriculos));
		
		
		// Para cada pessoa da lista
		for (int i=0; i<pessoas.size()-1; i++) {
			
			/** Currículo 1 **/		
			Pessoa pessoa1 = pessoas.get(i);			
			resultThread1 = executor.submit(
					new Callable<HashMap<String, Double>>() {	
						public HashMap<String, Double> call() throws Exception {
							System.out.println("==Curriculo 1, "+pessoa1.getNome()+"==\n");	
							return interpretador.gerarVetorConceitos(pessoa1.getCurriculo());
						}
					}
			);
			//System.out.println(""+pessoa1.getCurriculo()+"\n");	
			//HashMap<String, Double> vetor1 = interpretador.gerarVetorConceitos(pessoa1.getCurriculo());
			//HashMap<String, Double> vetor1 = pessoa1.getKeyWords();
			//System.out.println(vetor1);
			
			/** Currículo 2 **/
			for (int j=i+1; j<pessoas.size(); j++) {
				Pessoa pessoa2 = pessoas.get(j);	
				resultThread2 = executor.submit(
						new Callable<HashMap<String, Double>>() {	
							public HashMap<String, Double> call() throws Exception {
								System.out.println("     ==Curriculo 2, "+pessoa2.getNome()+"==\n");	
								return interpretador.gerarVetorConceitos(pessoa2.getCurriculo());
							}
						}
				);
				try {
					 vetor1 = resultThread1.get();
					 vetor2 = resultThread2.get();
				} catch (Exception e) {
				    System.out.println(e.getMessage());
				}
				//System.out.println(""+pessoa2.getCurriculo()+"\n");	
				//HashMap<String, Double> vetor2 = interpretador.gerarVetorConceitos(pessoa2.getCurriculo());
				//HashMap<String, Double> vetor2 = pessoa2.getVetorConceitos();
				//System.out.println(vetor2);
				
				
				/** Gera vetor completo (contendo tokens dos 2 curriculos) para calculo do Cosine **/   	
		    	List<String> vetorCompleto = cosineCalculator.unirVetores(vetor1.keySet(), vetor2.keySet());
		    	
		    	
		    	/** Calcula a frequencia de cada conceito do vetor completo para cada curriculo **/
		    	Double[] a = cosineCalculator.calcularFrequenciaConceitosVetorCompleto(vetor1, vetorCompleto);
		    	Double[] b = cosineCalculator.calcularFrequenciaConceitosVetorCompleto(vetor2, vetorCompleto);
		    	
		    	
		    	/** Print dos conceitos que aparecem nos 2 curriculos **/
		    	//printIguais(vetorCompleto,a,b);
		    	
		    	
		    	/** Calcula a similaridade atraves do metodo Cosine **/
		    	Double coeficiente = cosineCalculator.cosineSimilarity(a,b);
		    	similaridade = new Similaridade(pessoa1,pessoa2,coeficiente);
		    	System.out.println("     Similaridade = "+ Math.round(coeficiente*100) + "%\n");
		    	try {
		    		similaridadeDAO.adicionarSimilaridade(similaridade);
		    	} catch (Exception e) {
		    		similaridadeDAO.alterarSimilaridade(similaridade);
		    	}
			}			
		}
		executor.shutdown();
	}
	
	private void printIguais(List<String> vetorCompleto, Double[] a, Double[] b) {
		for (int i=0; i<vetorCompleto.size();i++) {
			if (a[i] != 0 && b[i] != 0 && (a[i]>=2 || b[i]>=2) )
			//if (a[i] != b[i])
				System.out.println(""+vetorCompleto.get(i)+", a:"+a[i]+", b:"+b[i]);
		}
	}
}
