package com.exemplo.gerenciamentoacademico.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

	private static final String url = "jdbc:mysql://localhost:3306/gerenciamento_academico?serverTimezone=UTC";
	private static final String user = "root";
	private static final String password = "Aluna";

	private static Connection conn;

	public static Connection getConexao() {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        if (conn == null || conn.isClosed()) {
	            conn = DriverManager.getConnection(url, user, password);
	        }
	        return conn;
	    } catch (ClassNotFoundException | SQLException e) {
	        System.out.println("Erro ao carregar o driver JDBC");
	        e.printStackTrace();
	        return null;
	    }
	}

	public static void main(String[] args) {
		// Testar a conexão no método main
		Connection conexao = DatabaseUtil.getConexao();

		if (conexao != null) {
			System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
			try {
				conexao.close(); // Fechar a conexão após o teste
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Falha ao conectar com o banco de dados.");
		}
	}
	
	// Método para fechar recursos JDBC
    private static void close(Connection myConn, Statement myStmt, ResultSet myRs) {
        try {
            if (myRs != null) {
                myRs.close();
            }
            if (myStmt != null) {
                myStmt.close();
            }
            if (myConn != null) {
                myConn.close();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

	public List<Usuario> getUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		myConn = getConexao();
		String sql = "SELECT * FROM USUARIO";
		
		
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery(sql);
			
			while(myRs.next()) {
				int id = myRs.getInt("id");
				String login = myRs.getString("login");
				String email = myRs.getString("email");
				String senha = myRs.getString("senha");
				String tipo_usuario = myRs.getString("tipo_usuario");
				
				Usuario inputUsuario = new Usuario(id, login, email, senha, tipo_usuario);
				usuarios.add(inputUsuario);
				
				
				
			}
			
			return usuarios;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 close(myConn, myStmt, myRs);
		}
		

		return null;
	}
	
	public void addUsuario(Usuario usuario) {
	    Connection myConn = null;
	    Statement myStmt = null;

	    try {
	        myConn = getConexao();
	        String sql = String.format(
	            "INSERT INTO usuario (login, email, senha, tipo_usuario) VALUES ('%s', '%s', '%s', '%s')",
	            usuario.getLogin(), usuario.getEmail(), usuario.getSenha(), usuario.gettipoUsuario()
	        );

	        myStmt = myConn.createStatement();
	        myStmt.executeUpdate(sql);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close(myConn, myStmt, null);
	    }
	}

}
