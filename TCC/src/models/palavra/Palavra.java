package models.palavra;

public class Palavra {

	private long id;
	private String palavra;
	private Long artigoId;
	private String artigo;
	private double tf;
	private double idf;
	private double tfidf;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public String getArtigo() {
		return artigo;
	}

	public void setArtigo(String artigo) {
		this.artigo = artigo;
	}
	
	public void setArtigoId(Long artigoId) {
		this.artigoId = artigoId;
	}
	
	public Long getArtigoId() {
		return artigoId;
	}

	public double getTf() {
		return tf;
	}

	public void setTf(double tf) {
		this.tf = tf;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}

}
