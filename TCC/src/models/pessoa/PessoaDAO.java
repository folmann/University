package models.pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.artigo.Artigo;
import util.jdbc.ConexaoBanco;

public class PessoaDAO {

	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;

	public PessoaDAO() {

	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConexaoBanco.getInstance().getConnection();
		return conn;
	}

	public void adicionarPessoa(Pessoa pessoa) {
		try {
			String queryString = "INSERT INTO Pessoa(id,nome,curriculo,instituicao) VALUES(?,?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, artigo.getTitle());
			ptmt.setString(2, artigo.getText());
			ptmt.executeUpdate();
			System.out.println("Pessoa ADICIONADO com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel adicionar a Pessoa. Já Existe.");
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

	public void alterarPessoa(Pessoa pessoa) {
		try {
			String queryString = "UPDATE Pessoa SET text=?, title=? WHERE id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, artigo.getText());
			ptmt.setString(2, artigo.getTitle());
			ptmt.setLong(3, artigo.getId());
			ptmt.executeUpdate();
			System.out.println("Pessoa ALTERADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel alterar a pessoa.");
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

	public void removerPessoa(Pessoa pessoa) {
		try {
			String queryString = "DELETE FROM Pessoa WHERE id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, artigo.getId());
			ptmt.executeUpdate();
			System.out.println("Pessoa DELETADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel deletar  pessoa.");
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

	public List<Pessoa> selectAll() {
		List<Pessoa> pessoa = new ArrayList<Pessoa>();
		try {
			String queryString = "SELECT * FROM Pessoa";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			Pessoa pessoa;

			while (resultSet.next()) {
				artigo = new Artigo();
				artigo.setId(resultSet.getLong("id"));
				artigo.setTitle(resultSet.getString("title"));
				artigo.setText(resultSet.getString("text"));
				artigos.add(artigo);
			}
			return pessoas;
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
		return pessoas;
	}

	public Artigo pesquisarPessoaPorNome(String nome) {
		Pessoa pessoa = new Pessoa();
		try {
			String queryString = "SELECT * FROM Pessoa WHERE nome=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, title);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {

				artigo.setTitle(resultSet.getString("title"));
				artigo.setText(resultSet.getString("text"));
			}
			return pessoa;
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
		return pessoa;
	}

}
