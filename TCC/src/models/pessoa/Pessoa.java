package models.pessoa;

import java.util.HashMap;
import java.util.List;

public class Pessoa {

	private String nome;
	private String curriculo;
	private String instituicao;
	private List<String> palavrasChave;
	private List<String> areasConhecimento;
	private HashMap<String, Double> vetorConceitos;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCurriculo() {
		return curriculo;
	}
	public void setCurriculo(String curriculo) {
		this.curriculo = curriculo;
	}
	public String getInstituicao() {
		return instituicao;
	}
	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}
	public List<String> getPalavrasChave() {
		return palavrasChave;
	}
	public void setPalavrasChave(List<String> palavrasChave) {
		this.palavrasChave = palavrasChave;
	}
	public List<String> getAreasConhecimento() {
		return areasConhecimento;
	}
	public void setAreasConhecimento(List<String> areasConhecimento) {
		this.areasConhecimento = areasConhecimento;
	}
	
	public void setVetorConceitos() {
		this.vetorConceitos = new HashMap<>();
		this.areasConhecimento.addAll(this.palavrasChave);
		for (String conceito : areasConhecimento) {
			if (vetorConceitos.containsKey(conceito)) {
				vetorConceitos.put(conceito, 1.0);
			} else
				// Adiciona o novo conceito
				vetorConceitos.put(conceito, 1.0);
		}			
	}
	
	public HashMap<String, Double> getVetorConceitos() {
		return vetorConceitos;
	}
	
}
