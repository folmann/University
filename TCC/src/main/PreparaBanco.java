package main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import util.DumpToSQL;
import util.crawler.Spider;
import util.jdbc.ConexaoBanco;

@SuppressWarnings("unused")
public class PreparaBanco {
	
	/** Aviso: Pode demorar dias montando o banco **/
	public static void main(String[] args) throws IOException {

		PreparaBanco banco = new PreparaBanco();
		final String dumpPath = "C:\\Users\\Lucas\\Desktop\\explicit-semantic-analysis-master\\ptwiki-20170801-pages-articles-multistream.xml\\wikiextractor-master\\text";
		
		//banco.criaBancoSQL();
		//banco.armazenarWikipediaDump(dumpPath);
		//new Spider().start("https://www.catho.com.br/vagas/?where_search=1&how_search=2&faixa_sal_id_combinar=1&order=score&perfil_id=1&pais_id=31&page=", 1, 3000 );
		banco.vetorizarBanco();    	 	
	}
	
	/** Cria as tabelas do banco de dados **/
	public void criaBancoSQL() {
		try {
		Connection conn = ConexaoBanco.getInstance().getConnection();
		PreparedStatement ptmt = null;
			//Cria Tabela Artigo
			try {			
				String queryArtigo = "CREATE TABLE artigo(\r\n" +
						"    id serial,\r\n" + 
						"    title varchar(150) NOT NULL,\r\n" + 
						"    text text NOT NULL,\r\n" + 
						"    CONSTRAINT artigo_pkey PRIMARY KEY (title,text)\r\n" + 
						")";
				ptmt = conn.prepareStatement(queryArtigo);
				ptmt.executeUpdate();
				System.out.println("Tabela Artigo criada com sucesso.");
			} 
			catch(Exception e){
				System.out.println(e);
			}
			//Cria Tabela Palavra
			try {
				String queryPalavra = "CREATE TABLE palavra(\r\n" + 
						"	 id serial,\r\n" + 
						"    palavra varchar(150) NOT NULL,\r\n" + 
						"    artigo varchar(150) NOT NULL,\r\n" + 
						"    artigoId numeric NOT NULL,\r\n" + 
						"    tf numeric,\r\n" + 
						"    idf numeric,\r\n" + 
						"    tfidf numeric,\r\n" + 
						"    CONSTRAINT palavra_artigo PRIMARY KEY (palavra,artigoId)   \r\n" + 
						")";
				ptmt = conn.prepareStatement(queryPalavra);
				ptmt.executeUpdate();
				System.out.println("Tabela Palavra criada com sucesso.");
			}	catch(Exception e){
				System.out.println(e);
			}finally {
	            try {
	                if (ptmt != null) {
	                    ptmt.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
	                System.out.println("Conexão com o banco finalizada.");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }	            
	        }
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void armazenarWikipediaDump(String path) {
		try {
			//Wikipedia Dump pre-parseado, formato .txt
			final File dumpFolder = new File(path);		
			DumpToSQL dumper = new DumpToSQL();
			dumper.listFilesForFolder(dumpFolder);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void vetorizarBanco() {
		try {
			//Vetoriza e calcula o TFIDF dos artigos armazenados no banco SQL  
			Vectorizer vetorizador = new Vectorizer("/util/stopwords.txt");
			List<List<String>> documents = vetorizador.vetorizarBanco();
			
			//Calcula e armazena no banco o TF das palavras
			System.out.println("Comecou calcular os TFs");
	    	//vetorizador.calculaTF(documents);
	    	
	    	//Calcula e armazena no banco o TFIDF das palavras
	    	System.out.println("Comecou a calcular os TFIDFs");
	    	vetorizador.calculaTFIDF();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

}
