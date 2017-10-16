package models.pessoa;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.jdbc.ConexaoBanco;
import util.parsers.XmlLattesParser;

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

	public void insertAll(String pathCurriculos) {
		try {
			List<Pessoa> pessoas = new XmlLattesParser().listFilesForFolder(new File(pathCurriculos));
			for (Pessoa pessoa : pessoas) {
				adicionarPessoa(pessoa);	
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	public void adicionarPessoa(Pessoa pessoa) {
		try {
			String queryString = "INSERT INTO Pessoa(nome,curriculo,instituicao) VALUES(?,?,?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, pessoa.getNome());
			ptmt.setString(2, pessoa.getCurriculo());
			ptmt.setString(3, pessoa.getInstituicao());
			ptmt.executeUpdate();
			System.out.println("Pessoa ADICIONADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel adicionar a Pessoa.");
			System.out.println(e.getMessage());
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
			String queryString = "UPDATE Pessoa SET nome=?, curriculo=?, instituicao=? WHERE id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, pessoa.getNome());
			ptmt.setString(2, pessoa.getCurriculo());
			ptmt.setString(3, pessoa.getInstituicao());
			ptmt.setLong(4, pessoa.getId());
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
			ptmt.setLong(1, pessoa.getId());
			ptmt.executeUpdate();
			System.out.println("Pessoa DELETADA com sucesso.");
		} catch (SQLException e) {
			System.out.println("Erro SQL: Nao foi possivel deletar a pessoa.");
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
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		try {
			String queryString = "SELECT * FROM Pessoa";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			Pessoa pessoa;

			while (resultSet.next()) {
				pessoa = new Pessoa();
				pessoa.setId(resultSet.getLong("id"));
				pessoa.setNome(resultSet.getString("nome"));
				pessoa.setCurriculo(resultSet.getString("curriculo"));
				pessoa.setInstituicao(resultSet.getString("instituicao"));
				pessoas.add(pessoa);
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

	public Pessoa pesquisarPessoaPorNome(String nome) {
		Pessoa pessoa = new Pessoa();
		try {
			String queryString = "SELECT * FROM Pessoa WHERE nome=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, nome);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				pessoa.setNome(resultSet.getString("nome"));
				pessoa.setCurriculo(resultSet.getString("curriculo"));
				pessoa.setInstituicao(resultSet.getString("instituicao"));
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
