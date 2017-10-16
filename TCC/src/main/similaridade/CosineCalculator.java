package main.similaridade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CosineCalculator {
	
	/** Une o conteudo dos 2 vetores **/
	public List<String> unirVetores(Set<String> set, Set<String> set2) {
		HashSet<String> vetor = new HashSet<String>();
		// Adiciona todo conteudo do vetor1
		for (String conceito : set) {
			vetor.add(conceito);
		}
		// Adiciona todo conteudo do vetor2
		for (String conceito : set2) {
			vetor.add(conceito);
		}
		List<String> vetorCompleto = new ArrayList<String>(vetor);
		// Ordena o novo vetor
		Collections.sort(vetorCompleto);
		return vetorCompleto;
	}

	/** Monta os vetores de frequencia para o calculo do Cosine **/
	public Double[] calcularFrequenciaConceitosVetorCompleto(HashMap<String, Double> documento, List<String> vetorCompleto) {
		Double vetor[] = new Double[vetorCompleto.size()];
		int cont = 0;
		// Para cada palavra do vetor completo
		for (String palavra : vetorCompleto) {
			// Se o documento contem a palavra
			if (documento.containsKey(palavra)) {
				// Adicionar a frequencia com que a palavra aparece no documento
				vetor[cont] = documento.get(palavra).doubleValue();
			} else
				// Se nao contem a palavra, a frequencia = 0.0
				vetor[cont] = 0.0;
			cont++;
		}
		return vetor;
	}

	/** Calcula a similaridade atraves do metodo Cosine **/
	public Double cosineSimilarity(Double[] vectorA, Double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

}
