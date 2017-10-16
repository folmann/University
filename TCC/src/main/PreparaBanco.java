package main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import main.interpretador.NGramCalculator;
import main.interpretador.Vectorizer;
import models.CriarBancoSQL;
import models.pessoa.PessoaDAO;
import util.crawler.CathoSpider;
import util.jdbc.ConexaoBanco;
import util.parsers.DumpToSQL;

@SuppressWarnings("unused")
public class PreparaBanco {

	/** Aviso: Pode demorar dias montando o banco **/
	public static void main(String[] args) throws IOException {

		PreparaBanco banco = new PreparaBanco();
		NGramCalculator nGramer = new NGramCalculator();
		final String dumpPath = "C:\\Users\\Lucas\\Desktop\\explicit-semantic-analysis-master\\ptwiki-20170801-pages-articles-multistream.xml\\wikiextractor-master\\text";
		final String pathCurriculos_IN = "C:\\Users\\lucas\\Desktop\\Lucas\\Faculdade\\TCC\\Codigo\\TCC\\src\\util\\Curriculos";
		
		/** Cria todas as tabelas do Banco SQL **/
		new CriarBancoSQL();
		
		/** Adiciona no banco o curriculo de todas as pessoas **/
		new PessoaDAO().insertAll(pathCurriculos_IN);
		
		/** Parsear o wikipedia e armazenar os dados no banco **/
		// banco.armazenarWikipediaDump(dumpPath);
		
		/** Coletar dados sobre vagas de emprego do site da CATHO.com.br e armazenar no banco **/
		// new Spider().start("https://www.catho.com.br/vagas/?where_search=1&how_search=2&faixa_sal_id_combinar=1&order=score&perfil_id=1&pais_id=31&page=", 1, 3000 );
		
		/** Salva no banco de dados um dicionario com todos os Ngrams formados dos curriculos **/
		nGramer.salvarAllNGrams(6);
		
		/** Transformar em token as descricoes sobre sobre cada conceito salvo **/
		//banco.vetorizarBanco();
	}

	public void armazenarWikipediaDump(String path) {
		try {
			// Wikipedia Dump pre-parseado, formato .txt
			final File dumpFolder = new File(path);
			DumpToSQL dumper = new DumpToSQL();
			dumper.listFilesForFolder(dumpFolder);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void vetorizarBanco() {
		try {
			// Vetoriza e calcula o TFIDF dos artigos armazenados no banco SQL
			Vectorizer vetorizador = new Vectorizer("/util/stopwords.txt");
			List<List<String>> documents = vetorizador.vetorizarBanco();

			// Calcula e armazena no banco o TF das palavras
			System.out.println("Comecou calcular os TFs");
			// vetorizador.calculaTF(documents);

			// Calcula e armazena no banco o TFIDF das palavras
			System.out.println("Comecou a calcular os TFIDFs");
			vetorizador.calculaTFIDF();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
