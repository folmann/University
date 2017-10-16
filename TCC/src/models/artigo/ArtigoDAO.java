package models.artigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.jdbc.ConexaoBanco;

public class ArtigoDAO {

    Connection connection = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;

    public ArtigoDAO() {

    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConexaoBanco.getInstance().getConnection();
        return conn;
    }

    public void adicionarArtigo(Artigo artigo) {
        try {
            String queryString = "INSERT INTO Artigo(titulo,texto) VALUES(?,?)";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, artigo.getTitulo());
            ptmt.setString(2, artigo.getTexto());
            ptmt.executeUpdate();
            System.out.println("Artigo ADICIONADO com sucesso.");
        } catch (SQLException e) {
        	System.out.println("Erro SQL: Nao foi possivel adicionar o artigo.");
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

    public void alterarArtigo(Artigo artigo) {
        try {
            String queryString = "UPDATE Artigo SET texto=?, titulo=? WHERE id=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, artigo.getTexto());
            ptmt.setString(2, artigo.getTitulo());
            ptmt.setLong(3, artigo.getId());
            ptmt.executeUpdate();
            System.out.println("Artigo ALTERADO com sucesso.");
        } catch (SQLException e) {
        	System.out.println("Erro SQL: Nao foi possivel alterar o artigo.");
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

    public void removerArtigo(Artigo artigo) {
        try {
            String queryString = "DELETE FROM Artigo WHERE id=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setLong(1, artigo.getId());
            ptmt.executeUpdate();
            System.out.println("Artigo DELETADO com sucesso.");
        } catch (SQLException e) {
        	System.out.println("Erro SQL: Nao foi possivel deletar o artigo.");
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
    
    public List<Artigo> selectAll() {
        List<Artigo> artigos = new ArrayList<Artigo>();
        try {
            String queryString = "SELECT id, titulo, lower(texto) AS texto FROM Artigo";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();
            Artigo artigo;
            
            while (resultSet.next()) {     
            	artigo = new Artigo();
            	artigo.setId(resultSet.getLong("id"));
                artigo.setTitulo(resultSet.getString("titulo"));
                artigo.setTexto(resultSet.getString("texto"));
                artigos.add(artigo);
            }
            return artigos;
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
        return artigos;
    }


    
    public Artigo pesquisarArtigoPorTitulo(String titulo) {
        Artigo artigo = new Artigo();
        try {
            String queryString = "SELECT * FROM artigo WHERE lower(titulo)=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, titulo.toLowerCase());
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {                
                artigo.setTitulo(resultSet.getString("titulo"));
                artigo.setTexto(resultSet.getString("texto"));
            }
            return artigo;
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
        return artigo;
    }
    
    public HashMap<String, Double> procurarTodosTitulosRelacionados(List<String> ngram) {
    	HashMap<String, Double> vetorFrequenciaConceitos =  new HashMap<>();
    	
        try {	        
	        connection = getConnection();
	        for (int i = 0; i < ngram.size(); i++) {
	        	String queryString = "SELECT * FROM artigo WHERE lower(titulo)=?";       		   			
		        ptmt = connection.prepareStatement(queryString);            
	            ptmt.setString(1, ngram.get(i).toLowerCase());                
	            resultSet = ptmt.executeQuery();
	            if(resultSet.next()) {                
	            	vetorFrequenciaConceitos.put(resultSet.getString("titulo"), 1.0);
            	}
	        }    
            return vetorFrequenciaConceitos;
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

}
