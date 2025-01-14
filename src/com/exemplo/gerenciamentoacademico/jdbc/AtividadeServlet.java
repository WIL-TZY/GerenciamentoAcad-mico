package com.exemplo.gerenciamentoacademico.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.exemplo.gerenciamentoacademico.jdbc.dao.AtividadeDAO;
import com.exemplo.gerenciamentoacademico.jdbc.dao.ProjetoDAO;
import com.exemplo.gerenciamentoacademico.jdbc.model.Atividade;
import com.exemplo.gerenciamentoacademico.jdbc.model.Projeto;

@WebServlet("/AtividadeServlet")
public class AtividadeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AtividadeDAO atividadeDAO;
    private ProjetoDAO projetoDAO;

    public void init() {
        atividadeDAO = new AtividadeDAO();
        projetoDAO = new ProjetoDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
                case "listar":
                    listarAtividades(request, response);
                    break;
                case "listarPorAluno":
                    listarAtividadesPorAluno(request, response);
                    break;
                case "listarProjeto":
                	listarAtividadesProjeto(request, response);
                    break;
                case "mostrarFormInsercao":
                    mostrarFormInsercao(request, response);
                    break;
                case "inserir":
                    inserirAtividade(request, response);
                    break;
                case "editar":
                    editarAtividade(request, response);
                    break;
                case "atualizar":
                    atualizarAtividade(request, response);
                    break;
                case "excluir":
                    excluirAtividade(request, response);
                    break;
                default:
                    listarAtividades(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void listarAtividadesPorAluno(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int usuarioId = (Integer) session.getAttribute("usuarioId");
        List<Atividade> listaAtividades = atividadeDAO.getAtividadesPorAluno(usuarioId);
        request.setAttribute("listaAtividades", listaAtividades);
        request.getRequestDispatcher("lista-entrega-atividade.jsp").forward(request, response);
    }


    private void mostrarFormInsercao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        int usuarioId = (Integer) session.getAttribute("usuarioId");
        List<Projeto> listaProjetos = projetoDAO.getProjetosPorProfessor(usuarioId);
        request.setAttribute("listaProjetos", listaProjetos);

        request.getRequestDispatcher("listar-atividades.jsp").forward(request, response);
    }

    private void listarAtividades(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int usuarioId = (Integer) session.getAttribute("usuarioId");
        List<Atividade> listaAtividades = atividadeDAO.getAtividadesPorProfessor(usuarioId);
        request.setAttribute("listaAtividades", listaAtividades);
        request.getRequestDispatcher("listar-atividades.jsp").forward(request, response);
    }

    private void inserirAtividade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int usuarioId = (Integer) session.getAttribute("usuarioId");

        String titulo = request.getParameter("titulo");
        String conteudo = request.getParameter("conteudo");
        LocalDate dataInicial = LocalDate.parse(request.getParameter("dataInicial"));
        LocalDate dataFinal = LocalDate.parse(request.getParameter("dataFinal"));

        Atividade atividade = new Atividade(titulo, conteudo, dataInicial, dataFinal);

        AtividadeDAO atividadeDAO = new AtividadeDAO();
        try {
            atividadeDAO.inserirAtividade(atividade, usuarioId);
            response.sendRedirect("AtividadeServlet?action=listar");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("AtividadeServlet?action=listar&error=InsertFailed");
        }
    }

    private void editarAtividade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Atividade atividade = atividadeDAO.getAtividadePorId(id);
            List<Projeto> listaProjetos = projetoDAO.getTodosProjetos();

            request.setAttribute("atividade", atividade);
            request.setAttribute("listaProjetos", listaProjetos);

            request.getRequestDispatcher("update-atividade-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AtividadeServlet?action=listar&error=InvalidActivity");
        }
    }

    private void atualizarAtividade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int usuarioId = (Integer) session.getAttribute("usuarioId");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String titulo = request.getParameter("titulo");
            String conteudo = request.getParameter("conteudo");
            LocalDate dataInicial = LocalDate.parse(request.getParameter("dataInicial"));
            LocalDate dataFinal = LocalDate.parse(request.getParameter("dataFinal"));

            Atividade atividadeAtualizada = new Atividade(id, titulo, conteudo, dataInicial, dataFinal);
            atividadeDAO.atualizarAtividade(atividadeAtualizada, usuarioId);

            response.sendRedirect("AtividadeServlet?action=listar");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AtividadeServlet?action=editar&id=" + request.getParameter("id") + "&error=UpdateFailed");
        }
    }


    private void excluirAtividade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            atividadeDAO.excluirAtividade(id);
            response.sendRedirect("AtividadeServlet?action=listar");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AtividadeServlet?action=listar&error=DeleteFailed");
        }
    }
    
    private void listarAtividadesProjeto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

       
        try {
            int projetoId = Integer.parseInt(request.getParameter("projeto_id"));
            List<Atividade> listaAtividades = atividadeDAO.getAtividadesPorProjetoId(projetoId);
            request.setAttribute("listaAtividades", listaAtividades);
            request.getRequestDispatcher("listar-atividades-projeto.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AtividadeServlet?action=listar&error=InvalidProjectId");
        }
    }
}

