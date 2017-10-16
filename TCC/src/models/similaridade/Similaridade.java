package models.similaridade;

import models.pessoa.Pessoa;

public class Similaridade {
	
	private String pessoa1;
	private String pessoa2;
	private Double coeficiente;
	
	public Similaridade() {
		
	}
	
	public Similaridade(Pessoa pessoa1, Pessoa pessoa2, Double coeficiente) {
		this.pessoa1 = pessoa1.getNome();
		this.pessoa2 = pessoa2.getNome();
		this.coeficiente = coeficiente;
	}

	public String getPessoa1() {
		return pessoa1;
	}

	public void setPessoa1(String pessoa1) {
		this.pessoa1 = pessoa1;
	}

	public String getPessoa2() {
		return pessoa2;
	}

	public void setPessoa2(String pessoa2) {
		this.pessoa2 = pessoa2;
	}

	public Double getCoeficiente() {
		return coeficiente;
	}

	public void setCoeficiente(Double coeficiente) {
		this.coeficiente = coeficiente;
	}
	

}
