
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<body>
	<h2><%= "Hello World!" %></h2>
	
	<form action="SvUsuarios" method="POST">
		<p><label>N. reloj:</label> <input type="text" name="nreloj"></p>
		<p><label>Nombre(s):</label> <input type="text" name="nombre"></p>
		<p><label>Apellido Paterno:</label> <input type="text" name="apellidoPaterno"></p>
		<p><label>Apellido Materno:</label> <input type="text" name="apellidoMaterno"></p>
		<button type="submit">Enviar</button>
	</form>
	</body>
</html>
