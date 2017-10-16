package util.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import models.artigo.Artigo;

public class XmlWikiParser {
	static String pathWikiDump = "C:\\Users\\lucas\\Desktop\\Lucas\\Faculdade\\TCC\\WikiDump\\ptwiki-20170801-pages-articles-multistream.xml";
	static File fileEntry = new File(pathWikiDump);
	
	public static void main(String args[]) {
			try {
				new XmlWikiParser().parseFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	
	public void parseFile() throws IOException  {
		
		//Carrega o arquivo
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileEntry), "UTF8"));
		
		//Declara variaveis
		String str = "", page="";
		Artigo artigo = new Artigo();
		List<Artigo> artigos = new ArrayList<Artigo>();
		boolean isPage = false;
		int cont =0;
		
		
		//Le as linhas do arquivo
		while ((str = in.readLine()) != null) {
			//Se encontrar um Tag <page>
			if (str.contains("<page>"))
				isPage = true;
			//Ao encontrar o final de uma pagina
			else if (str.contains("</page>")) {			
				page = page + str;	
				isPage = false;					
				
				artigo.setTitulo(getTitle(page));
				artigo.setTexto(getText(page));
				artigos.add(artigo);
				
				artigo = new Artigo();	
				page = new String();
				cont++;
			}
			if (isPage)
				page = page + str;	
			
			if(cont==11)	{
				for (Artigo artigo2 : artigos) {
					System.out.println(""+artigo2.getTitulo()+"\n");
					System.out.println(""+artigo2.getTexto()+"\n");
					System.exit(1);
					System.out.println("\n\n");
				}
				
			}
		}
		in.close();
		/*InputStream inputStream= new FileInputStream(fileEntry);
	    Reader reader = new InputStreamReader(inputStream,"UTF-8");
	    InputSource is = new InputSource(reader);
	    is.setEncoding("UTF-8");
	    
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(is);
		
		Element htmlTag = doc.getDocumentElement();
		getPages(htmlTag);*/
	}
	
	public String getTitle(String page) {
		String title = "";
		String[] pageSplit;
		pageSplit = page.split("<title>");
		pageSplit = pageSplit[1].split("</title>");
		title = pageSplit[0];
		return title;
	}
	
	public String getText(String page) {
		String text = "";
		String[] pageSplit;
		pageSplit = page.split("(<text>|<text xml:space=\"preserve\">)");
		pageSplit = pageSplit[1].split("</text>");
		text = pageSplit[0];
		
		text = text.replaceAll("(#REDIRECIONAMENTO|Imagem:|\\.jpg|thumb)", "");		
		text = text.replaceAll("(-|\\. |\\.|, |,)", "\" \"");
		//text = text.replaceAll("[^a-zA-Z „ı√’·¡‡¿ƒ‰È…ÌÕÛ”˙⁄‚ÍÓÙ˚¬ Œ‘€‡ËÏÚ˘¿»Ã“Ÿ‰ÎÔˆ¸ƒÀœ÷‹Á«∫™π≤≥]", "");
		//text = text.replaceAll("\\s+", " ");	
		text = text.trim();
		System.out.println(text);
		System.exit(1);
		return text;
	}
	
	
}
