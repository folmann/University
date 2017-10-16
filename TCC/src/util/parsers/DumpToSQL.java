package util.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;

import models.artigo.Artigo;
import models.artigo.ArtigoDAO;


public class DumpToSQL {
	
	//Pega todos os arquivos de uma pasta/subpasta
	public void listFilesForFolder(final File folder) throws IOException {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	separarArtigos(fileEntry);
	        }
	    }
	}
	
	//Separa os artigos contidos em um mesmo arquivo
	public void separarArtigos(File arquivo) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
		
		String linha = "";	
		String textoCompleto = "";
		String splitedTexts[];
		String[] artigos;
		Artigo artigo = new Artigo();
		ArtigoDAO artigoDAO = new ArtigoDAO();
		
		//Pega todo o texto de um documento
		while(br.ready()){			
			linha = br.readLine();
			textoCompleto = textoCompleto.concat(" "+linha);			
		}
		

		//Divide em artigos
		try{
			artigos = textoCompleto.split("</doc>");
			//Separa o titulo e o texto
			for (int i=0;i<artigos.length;i++){
				splitedTexts = artigos[i].split("title=\"|\">");
				artigo.setTitulo(splitedTexts[1].toLowerCase());
				
				//Limpa restos de html do texto
				splitedTexts = artigos[i].split("  ");
				artigo.setTexto(Jsoup.parse(splitedTexts[1]).text().toLowerCase());	
				
				//Adiciona o artigo no banco SQL
				try{
					//Se nao estiver vazio, adiciona no banco SQL
					if (!(artigo.getTexto().equals("")||artigo.getTitulo().equals("")))
						artigoDAO.adicionarArtigo(artigo);
				}catch (Exception e) {}//System.out.println("Erro na insercao SQL: "+e);}	
			}
		}catch (Exception e) {}//System.out.println("Erro no split do texto: "+e);}
		br.close();
	}	

}
