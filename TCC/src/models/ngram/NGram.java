package models.ngram;

public class NGram {
	
	private String conceito;
	private String ngram;
	private int frequencia;
	private int tamanho;
	
	public int getTamanho() {
		return tamanho;
	}
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	public String getConceito() {
		return conceito;
	}
	public void setConceito(String conceito) {
		this.conceito = conceito;
	}
	public String getNgram() {
		return ngram;
	}
	public void setNgram(String ngram) {
		this.ngram = ngram;
	}
	public int getFrequencia() {
		return frequencia;
	}
	public void setFrequencia(int frequencia) {
		this.frequencia = frequencia;
	}
	
	

}
