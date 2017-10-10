package util.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {


	  private Set<String> pagesVisited = new HashSet<String>();
	  private List<String> pagesToVisit = new LinkedList<String>();
	  private int cont;
	  /**
	   * @param url
	   *            - The starting point of the spider
	   * @param searchWord
	   *            - The word or string that you are searching for
	   */
	  public void start(String url, int cont, int MAX_PAGES_TO_SEARCH)
	  {
	      while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
	      {

		          String currentUrl;
		          SpiderLeg leg = new SpiderLeg();
		          //Se "pagesToVisit" estiver vazio, visitar url inicial
		          if(this.pagesToVisit.isEmpty())
		          {
		        	  this.cont = cont;
		              currentUrl = url+cont;
		              this.pagesVisited.add(url);
		          }
		          //Senao, visitar a proxima url
		          else
		          {
		              currentUrl = this.nextUrl();
		              this.cont++;
		          }
		          //Faz varias coisas, ver a classe SpiderLeg
		          leg.crawl(currentUrl,this.cont); 
		          //Procura as vagas em uma URL
		          leg.searchForVagasInPage(currentUrl);
		          //Adiciona o link para as proximas paginas
		          this.pagesToVisit.addAll(leg.getLinks());

	      }
	      System.out.println("\n**Finalizado** Visitou: " + this.pagesVisited.size() + " web page(s)");
	  }

	  /**
	   * Returns the next URL to visit (in the order that they were found). We also do a check to make
	   * sure this method doesn't return a URL that has already been visited.
	   * 
	   * @return
	   */
	  private String nextUrl()
	  {
	      String nextUrl;
	      do
	      {
	          nextUrl = this.pagesToVisit.remove(0);
	      } while(this.pagesVisited.contains(nextUrl));
	      this.pagesVisited.add(nextUrl);
	      return nextUrl;
	  }
}
	  


