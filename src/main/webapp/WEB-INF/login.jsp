<%--
 * Page representing login page of the application
 *
 * @author Julian Legeny
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home page</title>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.8/css/all.css">

<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

</head>

<div class="container-fluid">
<div class="row">&nbsp;</div>
<div class="row justify-content-center">
<h1>Demo aplikácia</h1>
</div>
<div class="row">&nbsp;</div>
<div class="row justify-content-center">
<div class="card">
<article class="card-body">
	<h4 class="card-title text-center mb-4 mt-1">Prihlásenie</h4>
	<hr>
	<p class="text-danger text-center">${error}</p>
	<form method="post" action="${pageContext.request.contextPath }/login">
	<div class="form-group">
	<div class="input-group">
		<div class="input-group-prepend">
		    <span class="input-group-text"> <i class="fa fa-user"></i> </span>
		 </div>
		<input type="text" name="username" value="test" class="form-control" placeholder="Login">
	</div> <!-- input-group.// -->
	</div> <!-- form-group// -->
	<div class="form-group">
	<div class="input-group">
		<div class="input-group-prepend">
		    <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
		 </div>
	    <input type="password" name="password" value="testpass" class="form-control" placeholder="******">
	</div> <!-- input-group.// -->
	</div> <!-- form-group// -->
	<div class="form-group">
	<button type="submit" value="login" class="btn btn-primary btn-block"> Prihlásiť  </button>
	</div> <!-- form-group// -->
	<p class="text-center">Default login/heslo: <strong>test / testpass</strong></p>
	</form>
</article>
</div> <!-- card.// -->

</div> <!-- row.// -->

</div>
<!--container end.//-->
</body>
</html>



