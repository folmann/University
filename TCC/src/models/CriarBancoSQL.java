package models;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.jdbc.ConexaoBanco;

public class CriarBancoSQL {

	private Connection conn = null;
	private PreparedStatement ptmt = null;

	private void createTabela(String nome, String query) {
		try {
			this.conn = ConexaoBanco.getInstance().getConnection();
			ptmt = conn.prepareStatement(query);
			ptmt.executeUpdate();
			System.out.println(""+nome+" criada com sucesso.");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
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
	}


	/** Cria as tabelas do banco de dados **/
	public CriarBancoSQL() {
		try {
			String queryArtigo = "CREATE TABLE artigo(\r\n" + "    id varchar(50),\r\n"
					+ "    titulo varchar(255) NOT NULL,\r\n" + "    texto text NOT NULL,\r\n"
					+ "    linguagem varchar(10), \r\n"
					+ "    CONSTRAINT artigo_pkey PRIMARY KEY (titulo,texto)\r\n" + ")";
			
			String queryPalavra = "CREATE TABLE palavra(\r\n" + "	 id serial,\r\n"
					+ "    palavra varchar(255) NOT NULL,\r\n" + "    artigo varchar(255) NOT NULL,\r\n"
					+ "    artigoId varchar(50) NOT NULL,\r\n" + "    tf numeric,\r\n" + "    idf numeric,\r\n"
					+ "    tfidf numeric,\r\n" + "    CONSTRAINT palavra_artigo PRIMARY KEY (palavra,artigoId)   \r\n"
					+ ")";
			
			String queryPessoa = "CREATE TABLE pessoa (\r\n" + 
					"	 id serial,\r\n" + 
					"    nome varchar(150) PRIMARY KEY,\r\n" + 
					"    instituicao varchar(150),\r\n" + 
					"    curriculo text\r\n" + ");";
			
			String querySimilaridade = "CREATE TABLE similaridade(\r\n" + 
					"	 pessoa1 varchar(255) REFERENCES pessoa(nome),\r\n" + 
					"    pessoa2 varchar(255) REFERENCES pessoa(nome),\r\n" + 
					"    coeficiente float,\r\n" + 
					"    PRIMARY KEY (pessoa1, pessoa2)\r\n" + 
					")";
			String queryTopConcepts = "CREATE VIEW topConcepts AS SELECT * FROM palavra WHERE tfidf > 0.6;";
			String queryNGram = "CREATE TABLE ngram(conceito varchar(255), ngram varchar(255), frequencia int, tamanho int, primary key (conceito, ngram));";
			
			this.createTabela("Tabela Artigo", queryArtigo);
			this.createTabela("Tabela Palavra", queryPalavra);
			this.createTabela("Tabela Pessoa", queryPessoa);
			this.createTabela("Tabela Similaridade", querySimilaridade);
			this.createTabela("Table NGram", queryNGram);
			this.createTabela("View TopConcepts", queryTopConcepts);	
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}
