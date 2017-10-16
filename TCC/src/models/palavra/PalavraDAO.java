package models.palavra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.jdbc.ConexaoBanco;

public class PalavraDAO {

	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;

	public PalavraDAO() {

	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConexaoBanco.getInstance().getConnection();
		return conn;
	}

	public void adicionarPalavra(Palavra palavra) {
		try {
			String queryString = "INSERT INTO Palavra(palavra,artigo,artigoId,tf,idf,tfidf) VALUES(?,?,?,?,?,?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, palavra.getPalavra().toLowerCase());
			ptmt.setString(2, palavra.getArtigo());
			ptmt.setLong(3, palavra.getArtigoId());
			ptmt.setDouble(4, palavra.getTf());
			ptmt.setDouble(5, palavra.getIdf());
			ptmt.setDouble(6, palavra.getTfidf());
			ptmt.executeUpdate();
			// System.out.println("Palavra ["+palavra.getPalavra()+"] ADICIONADA com
			// sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel adicionar a palavra.");
			// e.printStackTrace();
		} finally {
			try {
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void alterarPalavra(Palavra palavra) {
		try {
			String queryString = "UPDATE Palavra SET artigo=?,artigoId=?,tf=?,idf=?,tfidf=?,palavra=? WHERE id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, palavra.getArtigo());
			ptmt.setLong(2, palavra.getArtigoId());
			ptmt.setDouble(3, palavra.getTf());
			ptmt.setDouble(4, palavra.getIdf());
			ptmt.setDouble(5, palavra.getTfidf());
			ptmt.setString(6, palavra.getPalavra());
			ptmt.setLong(7, palavra.getId());
			ptmt.executeUpdate();
			System.out.println("Palavra [" + palavra.getPalavra() + "] ALTERADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel alterar a palavra.");
			e.printStackTrace();
		} finally {
			try {
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removerPalavra(Palavra palavra) {
		try {
			String queryString = "DELETE FROM Palavra WHERE id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, palavra.getId());
			ptmt.executeUpdate();
			System.out.println("Palavra [" + palavra.getPalavra() + "] DELETADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel deletar a palavra.");
			e.printStackTrace();
		} finally {
			try {
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Palavra> selectAlmostAll() {
		List<Palavra> palavras = new ArrayList<Palavra>();
		try {
			String queryString = "SELECT * FROM Palavra Where tfidf = -1 or tfidf = 0";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			Palavra palavra;

			while (resultSet.next()) {
				palavra = new Palavra();
				palavra.setId(resultSet.getLong("id"));
				palavra.setPalavra(resultSet.getString("palavra"));
				palavra.setArtigo(resultSet.getString("artigo"));
				palavra.setArtigoId(resultSet.getLong("artigoId"));
				palavra.setTf(resultSet.getDouble("tf"));
				palavra.setIdf(resultSet.getDouble("idf"));
				palavra.setTfidf(resultSet.getDouble("tfidf"));
				palavras.add(palavra);
			}
			return palavras;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return palavras;
	}

	public List<Palavra> selectAll() {
		List<Palavra> palavras = new ArrayList<Palavra>();
		try {
			String queryString = "SELECT * FROM Palavra";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			Palavra palavra;

			while (resultSet.next()) {
				palavra = new Palavra();
				palavra.setId(resultSet.getLong("id"));
				palavra.setPalavra(resultSet.getString("palavra"));
				palavra.setArtigo(resultSet.getString("artigo"));
				palavra.setArtigoId(resultSet.getLong("artigoId"));
				palavra.setTf(resultSet.getDouble("tf"));
				palavra.setIdf(resultSet.getDouble("idf"));
				palavra.setTfidf(resultSet.getDouble("tfidf"));
				palavras.add(palavra);
			}
			return palavras;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return palavras;
	}

	public int selectCountPalavra(String palavra) {
		try {
			String queryString = "SELECT COUNT(*) FROM palavra WHERE palavra=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, palavra);
			resultSet = ptmt.executeQuery();
			resultSet.next();
			return resultSet.getInt("count");
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public int selectCountAllPalavra() {
		try {
			String queryString = "SELECT COUNT(*) FROM palavra";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			resultSet.next();
			return resultSet.getInt("count");
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public Palavra pesquisarPalavra(Palavra word) {
		Palavra palavra = new Palavra();
		try {
			String queryString = "SELECT * FROM palavra WHERE palavra=? AND artigo=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, word.getPalavra());
			ptmt.setString(2, word.getArtigo());
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				palavra.setId(resultSet.getLong("id"));
				palavra.setPalavra(resultSet.getString("palavra"));
				palavra.setArtigo(resultSet.getString("artigo"));
				palavra.setArtigoId(resultSet.getLong("artigoId"));
				palavra.setTf(resultSet.getDouble("tf"));
				palavra.setIdf(resultSet.getDouble("idf"));
				palavra.setTfidf(resultSet.getDouble("tfidf"));
			}
			return palavra;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return palavra;
	}

	public List<Palavra> pesquisarTopConcepts(String word) {
		List<Palavra> topConcepts = new ArrayList<Palavra>();
		try {
			String queryString = "SELECT * FROM topConcepts WHERE LOWER(palavra) LIKE ? ORDER BY tfidf DESC LIMIT 10";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, '%' + word.toLowerCase() + '%');
			resultSet = ptmt.executeQuery();

			Palavra palavra;

			while (resultSet.next()) {
				palavra = new Palavra();
				palavra.setId(resultSet.getLong("id"));
				palavra.setPalavra(resultSet.getString("palavra"));
				palavra.setArtigo(resultSet.getString("artigo"));
				// palavra.setArtigoId(resultSet.getLong("artigoId"));
				palavra.setTf(resultSet.getDouble("tf"));
				palavra.setIdf(resultSet.getDouble("idf"));
				palavra.setTfidf(resultSet.getDouble("tfidf"));
				topConcepts.add(palavra);
			}
			return topConcepts;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return topConcepts;
	}

	public HashMap<String, Double> pesquisarAllTopConceptsUnion(List<String> tokens) {

		HashMap<String, Double> vetorFrequenciaConceitos = new HashMap<String, Double>();

		int qtd = 0;
		String artigo = "";
		String query = "SELECT COUNT(artigo) AS qtd, artigo FROM (";

		query = query + "(SELECT artigo,palavra,tfidf FROM topConcepts WHERE LOWER(palavra) LIKE '" + tokens.get(0)
				+ "' ORDER BY tfidf DESC LIMIT 20)";
		for (String token : tokens.subList(1, tokens.size())) {
			query = query + "UNION (SELECT artigo,palavra,tfidf FROM topConcepts WHERE LOWER(palavra) LIKE '" + token
					+ "' ORDER BY tfidf DESC LIMIT 20)";
			//incrementaFrequenciaVetorConceitos(vetorFrequenciaConceitos, token, 1.0);
			/*if (!resultSet.next()) {
				artigo = token;
				incrementaFrequenciaVetorConceitos(vetorFrequenciaConceitos, artigo, 1.0);
				System.out.println("Palavra nao encontrada: " + artigo);
				}
			}*/
		}
		query = query + ") AS a GROUP BY artigo ORDER BY qtd DESC;";
		
		try {
			//System.out.println("\n...Pegando conceitos...\n\n");
			connection = getConnection();
			ptmt = connection.prepareStatement(query);
			resultSet = ptmt.executeQuery();
			
			while (resultSet.next()) {
				artigo = resultSet.getString("artigo");
				qtd = resultSet.getInt("qtd");					
				incrementaFrequenciaVetorConceitos(vetorFrequenciaConceitos, artigo, 0.0+qtd);
				//System.out.println(artigo + ": " + qtd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vetorFrequenciaConceitos;
	}
	
	public HashMap<String, Double> pesquisarAllTopConcepts(List<String> tokens) {
		HashMap<String, Double> vetorFrequenciaConceitos = new HashMap<String, Double>();
		try {			
			connection = getConnection();
			String query = "";
			String artigo = "";
			boolean achouResultado = false;
			
			for (String token : tokens) {
				try {
					query = "SELECT artigo,palavra,tfidf FROM topConcepts WHERE LOWER(palavra) = ? ORDER BY tfidf DESC LIMIT 20";
					ptmt = connection.prepareStatement(query);
					ptmt.setString(1, token.toLowerCase());
					resultSet = ptmt.executeQuery();
					achouResultado = false;
					while (resultSet.next()) {
						achouResultado = true;
						artigo = resultSet.getString("artigo");					
						vetorFrequenciaConceitos = incrementaFrequenciaVetorConceitos(vetorFrequenciaConceitos, artigo, 1.0);
					}
					if(achouResultado == false)
						vetorFrequenciaConceitos = incrementaFrequenciaVetorConceitos(vetorFrequenciaConceitos, token, 1.0);
				
				} catch (SQLException e) {
					e.getMessage();
				}
			}
			return vetorFrequenciaConceitos;
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vetorFrequenciaConceitos;
	}


	/**
	 * Incrementa a frequencia de um conceito dentro do hash contendo as frequencias
	 **/
	public HashMap<String, Double> incrementaFrequenciaVetorConceitos(HashMap<String, Double> vetorFrequenciaConceitos,
			String artigo, Double tfidf) {
		if (vetorFrequenciaConceitos.containsKey(artigo)) {
			// Obtem contagem atual
			Double contador = vetorFrequenciaConceitos.get(artigo);
			// Incrementa a contagem
			vetorFrequenciaConceitos.put(artigo, contador + 1.0);
		} else
			// Adiciona o novo conceito com contagem de 1
			vetorFrequenciaConceitos.put(artigo, tfidf);
		return vetorFrequenciaConceitos;
	}
}
