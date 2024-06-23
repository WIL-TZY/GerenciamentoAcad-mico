package com.exemplo.gerenciamentoacademico.jdbc.dao;

import com.exemplo.gerenciamentoacademico.jdbc.DatabaseUtil;
import com.exemplo.gerenciamentoacademico.jdbc.model.Atividade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAO {

	

    public List<Atividade> getTodasAtividades() {
        String sql = "SELECT a.*, p.titulo AS projetoTitulo " +
                     "FROM atividade a " +
                     "JOIN projeto p ON a.projeto_id = p.id";
        List<Atividade> atividades = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Atividade atividade = new Atividade(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("conteudo"),
                        rs.getDate("dataInicial").toLocalDate(),
                        rs.getDate("dataFinal").toLocalDate(),
                        rs.getInt("projeto_id"),
                        rs.getString("projetoTitulo")
                );
                atividades.add(atividade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atividades;
    }
    
    public List<Atividade> getAtividadesPorProfessor(int professorId) {
        String sql = "SELECT a.*, p.titulo AS projetoTitulo " +
                     "FROM atividade a " +
                     "JOIN projeto p ON a.projeto_id = p.id " +
                     "WHERE p.professor_id = ?";
        List<Atividade> atividades = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Atividade atividade = new Atividade(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("conteudo"),
                            rs.getDate("dataInicial").toLocalDate(),
                            rs.getDate("dataFinal").toLocalDate(),
                            rs.getInt("projeto_id"),
                            rs.getString("projetoTitulo")
                    );
                    atividades.add(atividade);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atividades;
    }
    
    public void inserirAtividade(Atividade atividade, int usuarioId) throws SQLException {
        String sqlProjeto = "SELECT id FROM projeto WHERE professor_id = ?";
        String sqlInserir = "INSERT INTO atividade (titulo, conteudo, dataInicial, dataFinal, projeto_id) " +
                            "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmtProjeto = null;
        PreparedStatement stmtInserir = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConexao();

            // Consultar o projeto_id com base no professor_id (usuarioId)
            stmtProjeto = conn.prepareStatement(sqlProjeto);
            stmtProjeto.setInt(1, usuarioId);
            rs = stmtProjeto.executeQuery();

            int projetoId = 0; // Inicializa projetoId

            if (rs.next()) {
                projetoId = rs.getInt("id");
            } else {
                throw new SQLException("Não foi encontrado nenhum projeto associado ao professor.");
            }

            // Inserir a atividade com o projeto_id obtido
            stmtInserir = conn.prepareStatement(sqlInserir);
            stmtInserir.setString(1, atividade.getTitulo());
            stmtInserir.setString(2, atividade.getConteudo());
            stmtInserir.setDate(3, java.sql.Date.valueOf(atividade.getDataInicial()));
            stmtInserir.setDate(4, java.sql.Date.valueOf(atividade.getDataFinal()));
            stmtInserir.setInt(5, projetoId);

            stmtInserir.executeUpdate();  // Executa a inserção no banco de dados

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Fechar os recursos
            if (rs != null) {
                rs.close();
            }
            if (stmtProjeto != null) {
                stmtProjeto.close();
            }
            if (stmtInserir != null) {
                stmtInserir.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }



    public void atualizarAtividade(Atividade atividade, int usuarioId) throws SQLException {
        String sqlProjeto = "SELECT projeto.id FROM projeto WHERE projeto.professor_id = ?";
        String sql = "UPDATE atividade SET titulo = ?, conteudo = ?, dataInicial = ?, dataFinal = ?, projeto_id = ? " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmtProjeto = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConexao();

            // Obter projeto_id com base no professor_id (usuarioId)
            stmtProjeto = conn.prepareStatement(sqlProjeto);
            stmtProjeto.setInt(1, usuarioId);
            ResultSet rs = stmtProjeto.executeQuery();

            int projetoId = 0; // Inicializa projetoId

            if (rs.next()) {
                projetoId = rs.getInt("id");
            } else {
                throw new SQLException("Não foi encontrado nenhum projeto associado ao professor.");
            }

            // Atualizar a atividade com o projeto_id obtido
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, atividade.getTitulo());
            stmt.setString(2, atividade.getConteudo());
            stmt.setDate(3, java.sql.Date.valueOf(atividade.getDataInicial()));
            stmt.setDate(4, java.sql.Date.valueOf(atividade.getDataFinal()));
            stmt.setInt(5, projetoId);
            stmt.setInt(6, atividade.getId());

            stmt.executeUpdate();  // Executa a atualização no banco de dados

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Fechar os recursos
            if (stmtProjeto != null) {
                stmtProjeto.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }



    public void excluirAtividade(int id) {
        String sql = "DELETE FROM atividade WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Atividade getAtividadePorId(int id) {
        String sql = "SELECT * FROM atividade WHERE id = ?";
        Atividade atividade = null;
        try (Connection conn = DatabaseUtil.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    atividade = new Atividade(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("conteudo"),
                            rs.getDate("dataInicial").toLocalDate(),
                            rs.getDate("dataFinal").toLocalDate(),
                            rs.getInt("projeto_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atividade;
    }
}
