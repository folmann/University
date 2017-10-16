package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import models.pessoa.Pessoa;
import models.pessoa.PessoaDAO;
import models.similaridade.Similaridade;
import models.similaridade.SimilaridadeDAO;

public class GenerateJSONGraph {
	
	public GenerateJSONGraph(String pathJSON) throws IOException {
		
		SimilaridadeDAO similaridadeDAO = new SimilaridadeDAO();
		PessoaDAO pessoaDAO = new PessoaDAO();
		List<Pessoa> pessoas = pessoaDAO.selectAll();
		List<Similaridade> similaridades = similaridadeDAO.selectAll();
		Similaridade similaridade;
		
		File fout = new File(""+pathJSON+"similaridade.json");
		FileOutputStream fos = new FileOutputStream(fout);	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		
		// Inicia escrita do JSON
		bw.write("{"); 
		bw.newLine();
		// Coloca os nodes com o nome das pessoas
		bw.write("\t\"nodes\": ["); 
		bw.newLine();			
		for (int i=0; i<pessoas.size(); i++) {
			if (i!=pessoas.size()-1)
				bw.write("\t\t{\"id\": \"" + pessoas.get(i).getNome() + "\", \"group\": 1},");
			else
				bw.write("\t\t{\"id\": \"" + pessoas.get(i).getNome() + "\", \"group\": 1}");
			bw.newLine();
		}
		bw.write("\t],");
		bw.newLine();
		
		// Coloca as arestas com o valor da similaridade		
		bw.write("\t\"links\": [");
		bw.newLine();		
		for (int i=0; i<similaridades.size(); i++) {
			similaridade = similaridades.get(i);
			if (i!=similaridades.size()-1)
				bw.write("{\"source\": \"" + similaridade.getPessoa1() + "\", \"target\": \""+similaridade.getPessoa2()+"\", \"value\": "+similaridade.getCoeficiente()+"},");
			else
				bw.write("{\"source\": \"" + similaridade.getPessoa1() + "\", \"target\": \""+similaridade.getPessoa2()+"\", \"value\": "+similaridade.getCoeficiente()+"}");
			bw.newLine();
		}
		bw.write("\t]");
		bw.newLine();
		bw.write("}"); 
		bw.close();
	}
}
