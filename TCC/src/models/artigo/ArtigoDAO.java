package models.artigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            String queryString = "INSERT INTO Artigo(title,text) VALUES(?,?)";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, artigo.getTitle());
            ptmt.setString(2, artigo.getText());
            ptmt.executeUpdate();
            System.out.println("Artigo ADICIONADO com sucesso.");
        } catch (SQLException e) {
        	System.out.println("Erro SQL: Nao foi possivel adicionar o artigo. Já Existe.");
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
            String queryString = "UPDATE Artigo SET text=?, title=? WHERE id=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, artigo.getText());
            ptmt.setString(2, artigo.getTitle());
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
            String queryString = "SELECT id, title, lower(text) AS text FROM Artigo";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();
            Artigo artigo;
            
            while (resultSet.next()) {     
            	artigo = new Artigo();
            	artigo.setId(resultSet.getLong("id"));
                artigo.setTitle(resultSet.getString("title"));
                artigo.setText(resultSet.getString("text"));
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

    public Artigo pesquisarArtigoPorTitulo(String title) {
        Artigo artigo = new Artigo();
        try {
            String queryString = "SELECT * FROM Artigo WHERE title=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, title);
            resultSet = ptmt.executeQuery();

            while (resultSet.next()) {
                
                artigo.setTitle(resultSet.getString("title"));
                artigo.setText(resultSet.getString("text"));
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

}
