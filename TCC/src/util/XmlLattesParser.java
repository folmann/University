package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import models.pessoa.Pessoa;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlLattesParser {


	public List<Pessoa> listFilesForFolder(final File folder) throws IOException {	
		
		Pessoa pessoa;
		List<Pessoa> pessoas = new ArrayList<>();
		
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				try {

					Element htmlTag = parseLattes(fileEntry);
					
					pessoa = new Pessoa();					
					pessoa.setNome(this.getNome(htmlTag));
					pessoa.setCurriculo(this.getCurriculo(htmlTag));
					pessoa.setInstituicao(this.getInstituicao(htmlTag));
					pessoa.setPalavrasChave(this.getPalavrasChave(htmlTag));
					pessoa.setAreasConhecimento(this.getAreasConhecimento(htmlTag));
					pessoa.setVetorConceitos();
					pessoas.add(pessoa);
					
				} catch (SAXException | ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
		return pessoas;
	}

	public Element parseLattes(File fileEntry) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(fileEntry);
		Element htmlTag = doc.getDocumentElement();
		return htmlTag;
	}

	public String getNome(Element htmlTag) {
		Element dadosGerais = (Element) htmlTag.getElementsByTagName("DADOS-GERAIS").item(0);
		String nome = dadosGerais.getAttribute("NOME-COMPLETO");
		return nome;
	}
	
	public String getCurriculo(Element htmlTag) {
		Element resumoCV = (Element) htmlTag.getElementsByTagName("RESUMO-CV").item(0);
		String resumo = resumoCV.getAttribute("TEXTO-RESUMO-CV-RH");
		return resumo;
	}
	
	public String getInstituicao(Element htmlTag) {
		Element endereco = (Element) htmlTag.getElementsByTagName("ENDERECO-PROFISSIONAL").item(0);
		String nomeEmpresa = endereco.getAttribute("NOME-INSTITUICAO-EMPRESA");
		return nomeEmpresa;
	}
	
	public List<String> getPalavrasChave(Element htmlTag) {
		List<String> palavrasChave = new ArrayList<>();
		
		NodeList nodes = htmlTag.getElementsByTagName("PALAVRAS-CHAVE");
		Element palavraChave = null;
		
		for(int j=0;j<nodes.getLength();j++) {
			palavraChave = (Element) nodes.item(j);
			for(int i=1;i<=palavraChave.getAttributes().getLength();i++) {
				String palavra = palavraChave.getAttribute("PALAVRA-CHAVE-"+i);
				if(!palavra.isEmpty())
					palavrasChave.add(palavra);
			}
		}
		return palavrasChave;
	}
	
	public List<String> getAreasConhecimento(Element htmlTag) {
		List<String> areasConhecimento = new ArrayList<>();
		
		NodeList nodes = htmlTag.getElementsByTagName("AREAS-DO-CONHECIMENTO");
		NodeList areas = null;
		
		for(int j=0;j<nodes.getLength();j++) {
			areas = (NodeList) nodes.item(j);
	
			for(int i=0;i<areas.getLength();i++) {
				String area = ((Element)areas.item(i)).getAttribute("NOME-DA-SUB-AREA-DO-CONHECIMENTO");
				if(!area.isEmpty())
					areasConhecimento.add(area);
			}
		}
		return areasConhecimento;
	}
	

}
