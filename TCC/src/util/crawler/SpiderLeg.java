package util.crawler;

import java.util.LinkedList;

import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import models.artigo.Artigo;
import models.artigo.ArtigoDAO;

public class SpiderLeg {
	// We'll use a fake USER_AGENT so the web server thinks the robot is a normal
	// web browser.
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private Document htmlDocument;
	private ArtigoDAO artigoDAO = new ArtigoDAO();

	public boolean crawl(String url, int cont) {
		try{
			conectar(url);
			
			String bodyText = this.htmlDocument.html().toString();
			String[] textSplit = null;
			// Se tiver flecha para próxima página
			if (!bodyText.contains("<span class=\"arrow next disable\">próximo</span>")) {
				// Pega o link para a próxima página
				textSplit = bodyText.split("class=\"arrow next\" data-page=\"");
				textSplit = textSplit[1].split("\">");
				int page = Integer.parseInt(textSplit[0]);
				String nextUrl = "https://www.catho.com.br/vagas/?where_search=1&how_search=2&faixa_sal_id_combinar=1&order=score&perfil_id=1&pais_id=31&page="
						+ page;
				this.links.add(nextUrl);
				return true;
			} else
				return false;
		}catch(Exception e){
			String nextUrl = "https://www.catho.com.br/vagas/?where_search=1&how_search=2&faixa_sal_id_combinar=1&order=score&perfil_id=1&pais_id=31&page="
					+ (cont+1);
			this.links.add(nextUrl);
			System.out.println(e);
			return false;
		}
	}

	public boolean conectar(String url) {
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
															// indicating that everything is great.
			{
				System.out.println("\nPágina: " + url);
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("Erro: Pegou algo que não era um HTML");
				return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean searchForVagasInPage(String url) {
		// Defensive coding. This method should only be used after a successful crawl.
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}

		//Pega todas as vagas que aparecem na página
		String bodyText = this.htmlDocument.html();
		String[] vagasHTML = bodyText.split(" <ul id=\"listagemVagas\" class=\"listagemVagas\">");
		vagasHTML = vagasHTML[1].split("<nav class=\"paginacao\">");
		vagasHTML = vagasHTML[0].split("</li>");
		
		//Para cada link de vaga que há na pagina
		try{
		for(int i=0; i<vagasHTML.length-1;i++) {
			String[] urlVaga = vagasHTML[i].split("<a href=\"");
			urlVaga = urlVaga[1].split("\" title=");
			
			//Entra na URL da vaga
			if(conectar(urlVaga[0])) {			
				String vagaHTML = this.htmlDocument.html().toString();
				
				Artigo vaga = new Artigo();
				
				//Adiciona o título da vaga
				String[] detalhesVaga = vagaHTML.split(" itemprop=\"title\">");
				detalhesVaga = detalhesVaga[1].split("</h1>");
				vaga.setTitle(detalhesVaga[0]);
				//System.out.println(detalhesVaga[0]);
				
				//Adiciona a descrição da vaga		
				detalhesVaga = vagaHTML.split(" itemprop=\"description\"> ");
				detalhesVaga = detalhesVaga[1].split("</p>");
				vaga.setText(detalhesVaga[0]);
				//System.out.println(detalhesVaga[0]);	
				
				//Adiciona no banco de dados
				artigoDAO.adicionarArtigo(vaga);
			}
		}
		return true;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
			
	}

	public List<String> getLinks() {
		return this.links;
	}

}