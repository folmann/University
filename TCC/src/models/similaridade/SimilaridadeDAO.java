package models.similaridade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.jdbc.ConexaoBanco;

public class SimilaridadeDAO {
	
	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConexaoBanco.getInstance().getConnection();
		return conn;
	}
	
	public void adicionarSimilaridade(Similaridade similaridade) {
		try {
			String queryString = "INSERT INTO Similaridade(pessoa1, pessoa2, coeficiente) VALUES(?,?,?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, similaridade.getPessoa1());
			ptmt.setString(2, similaridade.getPessoa2());
			ptmt.setDouble(3, similaridade.getCoeficiente());
			ptmt.executeUpdate();
			System.out.println("Similaridade ADICIONADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel adicionar a Similaridade.");
			System.out.println(e.getMessage());
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

	public void alterarSimilaridade(Similaridade similaridade) {
		try {
			String queryString = "UPDATE Similaridade SET coeficiente=? WHERE (pessoa1=? AND pessoa2=?) OR (pessoa1=? AND pessoa2=?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setDouble(1, similaridade.getCoeficiente());
			ptmt.setString(2, similaridade.getPessoa1());
			ptmt.setString(3, similaridade.getPessoa2());
			ptmt.setString(4, similaridade.getPessoa2());
			ptmt.setString(5, similaridade.getPessoa1());
			ptmt.executeUpdate();
			System.out.println("Similaridade ALTERADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel alterar a Similaridade.");
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

	public void removerSimilaridade(Similaridade similaridade) {
		try {
			String queryString = "DELETE FROM Similaridade WHERE (pessoa1=? AND pessoa2=?) OR (pessoa1=? AND pessoa2=?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, similaridade.getPessoa1());
			ptmt.setString(2, similaridade.getPessoa2());
			ptmt.setString(3, similaridade.getPessoa2());
			ptmt.setString(4, similaridade.getPessoa1());
			ptmt.executeUpdate();
			System.out.println("Similaridade DELETADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel deletar a Similaridade.");
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

	public List<Similaridade> selectAll() {
		List<Similaridade> similaridades = new ArrayList<Similaridade>();
		try {
			String queryString = "SELECT * FROM Similaridade";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			Similaridade similaridade;

			while (resultSet.next()) {
				similaridade = new Similaridade();
				similaridade.setPessoa1(resultSet.getString("pessoa1"));
				similaridade.setPessoa2(resultSet.getString("pessoa2"));
				similaridade.setCoeficiente((resultSet.getDouble("coeficiente")));
				similaridades.add(similaridade);
			}
			return similaridades;
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
		return similaridades;
	}
}
