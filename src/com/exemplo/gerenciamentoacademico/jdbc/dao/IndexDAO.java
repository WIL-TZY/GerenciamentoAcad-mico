package com.exemplo.gerenciamentoacademico.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.exemplo.gerenciamentoacademico.jdbc.DatabaseUtil;
import com.exemplo.gerenciamentoacademico.jdbc.model.Usuario;

public class IndexDAO {

    // Método para validar o login de professor
    public Usuario validarProfessor(String login, String senha) {
        String query = "SELECT id, nome FROM professor WHERE login = ? AND senha = ?";
        try (
            Connection conn = DatabaseUtil.getConexao();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para validar o login de aluno
    public Usuario validarAluno(String login, String senha) {
        String query = "SELECT id, nome FROM aluno WHERE login = ? AND senha = ?";
        try (
            Connection conn = DatabaseUtil.getConexao();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para validar o login de coordenador
    public Usuario validarCoordenador(String login, String senha) {
        String query = "SELECT id, nome FROM coordenador WHERE login = ? AND senha = ?";
        try (
            Connection conn = DatabaseUtil.getConexao();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
