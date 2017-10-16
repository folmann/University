package main;

import java.io.IOException;

import main.similaridade.CalcularSimilaridade;
import util.GenerateJSONGraph;

public class Main {
	public static void main(String args[]) {
		final String pathCurriculos_IN = "C:\\Users\\lucas\\Desktop\\Lucas\\Faculdade\\TCC\\Codigo\\TCC\\src\\util\\Curriculos";
		final String pathJSON_OUT = "C:\\Users\\lucas\\Desktop\\Lucas\\Faculdade\\TCC\\Codigo\\TCC\\grafo\\";
		
		try {			
			
			new CalcularSimilaridade(pathCurriculos_IN);
			new GenerateJSONGraph(pathJSON_OUT);	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
}