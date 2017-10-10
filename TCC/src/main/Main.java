package main;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import models.pessoa.Pessoa;
import util.CosineCalculator;
import util.XmlLattesParser;

public class Main {

	public static void main(String[] args) throws IOException {
    	
		//Inicializa algumas variaveis
		InterpretadorSemantico interpretador = new InterpretadorSemantico();
		CosineCalculator cosineCalculator = new CosineCalculator();
		XmlLattesParser xmlParser = new XmlLattesParser();
		String pathCurriculos = "C:\\Users\\lucas\\Desktop\\Lucas\\Faculdade\\TCC\\Codigo\\TCC\\src\\util\\Curriculos";
		
		/** Pega todos os curriculos das pessoas**/
		List<Pessoa> pessoas = xmlParser.listFilesForFolder(new File(pathCurriculos));		
		
		for (int i=0; i<pessoas.size()-1; i++) {
			
			/** Currículo 1 **/
			Pessoa pessoa1 = pessoas.get(i);
			System.out.print("Curriculo 1, "+pessoa1.getNome()+"\n");			
			//HashMap<String, Double> vetor1 = interpretador.gerarVetorConceitos(pessoa1.getCurriculo());
			HashMap<String, Double> vetor1 = pessoa1.getVetorConceitos();
			for (int j=i+1; j<pessoas.size(); j++) {
				
				/** Currículo 2 **/
				Pessoa pessoa2 = pessoas.get(j);
				System.out.print("Curriculo 2, "+pessoa2.getNome());			
				//HashMap<String, Double> vetor2 = interpretador.gerarVetorConceitos(pessoa2.getCurriculo());
				HashMap<String, Double> vetor2 = pessoa2.getVetorConceitos();

				/** Gera vetor completo (contendo tokens dos 2 curriculos) para calculo do Cosine **/   	
		    	List<String> vetorCompleto = cosineCalculator.unirVetores(vetor1.keySet(), vetor2.keySet());
		    	
		    	/** Calcula a frequencia de cada conceito do vetor completo para cada curriculo **/
		    	Double[] a = cosineCalculator.calcularFrequenciaConceitosVetorCompleto(vetor1, vetorCompleto);
		    	Double[] b = cosineCalculator.calcularFrequenciaConceitosVetorCompleto(vetor2, vetorCompleto);
		    	
		    	/** Print dos conceitos que aparecem nos 2 curriculos **/
		    	System.out.println();
		    	printIguais(vetorCompleto,a,b);
		    	
		    	/** Calcula a similaridade atraves do metodo Cosine **/
		    	Double similaridade = cosineCalculator.cosineSimilarity(a,b);
		    	System.out.println("Similaridade = "+ similaridade + "\n");
			}			
		}
		//Gerar grafo
		//COPY (SELECT ROW_TO_JSON(t) FROM (SELECT * FROM grafo) t) TO '/pessoas.json';

    }
	
	public static void printIguais(List<String> vetorCompleto, Double[] a, Double[] b) {
		for (int i=0; i<vetorCompleto.size();i++) {
			if (a[i] != 0 && b[i] != 0)
				System.out.println(""+vetorCompleto.get(i)+", a:"+a[i]+", b:"+b[i]);
		}
	}
}