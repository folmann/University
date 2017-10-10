package util;

import java.util.List;

public class TFIDFCalculator {
	
    /**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     * Calcula a frequencia com que o termo aparece no documento
     */
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     * Ele ve em quantos documentos esse termo apareceu e calcula log(total de documentos / quantidade de vezes que apareceu)
     */
    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
            	//System.out.println("\nTerm("+term+") : Word("+word+")");
                if (term.equalsIgnoreCase(word)) {
                	//System.out.println("\nTerm("+term+") = Word("+word+")");
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {   
 
    	return tf(doc, term) * idf(docs, term);	
    }
    
}