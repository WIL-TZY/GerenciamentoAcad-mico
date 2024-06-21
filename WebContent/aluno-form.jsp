<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulário do Aluno</title>
</head>
<body>
    <h1>Formulário do Aluno</h1>
    <form action="processAlunoForm.jsp" method="post">
        <label for="matricula">Matrícula:</label><br>
        <input type="text" id="matricula" name="matricula"><br>
        <label for="nome">Nome:</label><br>
        <input type="text" id="nome" name="nome"><br>
        <label for="email">Email:</label><br>
        <input type="email" id="email" name="email"><br>
        <label for="lattes">Lattes:</label><br>
        <input type="text" id="lattes" name="lattes"><br>
        <label for="login">Login:</label><br>
        <input type="text" id="login" name="login"><br>
        <label for="senha">Senha:</label><br>
        <input type="password" id="senha" name="senha"><br><br>
        <input type="submit" value="Enviar">
    </form>
</body>
</html>
