<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Listagem de Feedbacks de Professores</title>
</head>
<body>
    <h2>Listagem de Feedbacks de Professores</h2>
   
    
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Feedback</th>
                <th>ID do Professor</th>
             
            </tr>
        </thead>
        <tbody>
            <c:forEach var="feedback" items="${listaFeedbacksProfessor}">
                <tr>
                    <td>${feedback.id}</td>
                    <td>${feedback.titulo}</td>
                    <td>${feedback.feedback}</td>
                    <td>${feedback.professorId}</td>
                    
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <br>
    <button onclick="window.location.href='index-professor.jsp'">Voltar a Página Inicial</button>
</body>
</html>