package models.ngram;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.jdbc.ConexaoBanco;

public class NGramDAO {
	
	Connection connection = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;


    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConexaoBanco.getInstance().getConnection();
        return conn;
    }

    public void adicionarNGRAM(NGram ngram) {
        try {
            String queryString = "INSERT INTO ngram(conceito, ngram, frequencia, tamanho) VALUES(?,?,?,?)";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, ngram.getConceito());
            ptmt.setString(2, ngram.getNgram());
            ptmt.setInt(3, ngram.getFrequencia());
            ptmt.setInt(4, ngram.getTamanho());
            ptmt.executeUpdate();
            System.out.println("Ngram ADICIONADO com sucesso.");
        } catch (SQLException e) {
        	System.out.println("Erro SQL: Nao foi possivel adicionar o Ngram.");
            //e.printStackTrace();
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
    
    public Boolean verificarNGram(String frase) {
		try {
			String queryString = "SELECT * FROM (SELECT COUNT(*) AS count, ngram, tamanho FROM ngram GROUP BY ngram, tamanho ORDER BY count DESC) AS a WHERE count>1 AND tamanho>1 AND LOWER(ngram)=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, frase.toLowerCase());
			resultSet = ptmt.executeQuery();

			if(resultSet.next()) {
				return true;
			}			
			return false;
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
		return false;
	}

}
