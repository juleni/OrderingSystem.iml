<%--
 * Page representing managing of User data (create/update/delete)
 * Lists shows all users created in the application database.
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
<title>Main page</title>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.8/css/all.css">
</head>
<body>
	<%
		
		 response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		
		if(session.getAttribute("currentUser")==null)
		{
			response.sendRedirect("/login");
		}
	
	%>

    <div class="container-fluid">
    <div class="row">&nbsp;</div>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath }/mainpage">Hlavné menu</a></li>
            <li class="breadcrumb-item active"><a href="#">Správa užívateľov</a></li>
            <li class="ml-auto">Prihlásený užívateľ: <strong>${sessionScope.username}</strong>
                                                &nbsp; | &nbsp;
                                                <a href="${pageContext.request.contextPath }/logout">Odhlásiť sa</a></li>
        </ol>
    </nav>

    <div class="row">&nbsp;</div>


<div class="card">
  <h5 class="card-header">Nový užívateľ</h5>
  <div class="card-body">
  <p class="card-text">
  <c:if test="${message_ok != null}">
           <div class="alert alert-success" role="alert">
             ${message_ok}
           </div>
  </c:if>
  <c:if test="${message_err != null}">
        <div class="alert alert-danger" role="alert">
          ${message_err}
        </div>
  </c:if>

  </p>
  <p class="card-text">Pridaný bude len užívateľ s loginom, ktorý sa ešte v systéme nenachádza.</p>
    <form method="post" action="${pageContext.request.contextPath }/addUser">
      <div class="form-group row">
        <label for="inputLogin" class="col-sm-2 col-form-label">Login</label>
        <div class="col-sm-10">
          <input type="login" name="user_login" value="${editUser.user_login}" class="form-control" id="inputLogin"
            <c:if test="${editUser.user_id > -1}">readonly</c:if> placeholder="" required="true">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputEmail" class="col-sm-2 col-form-label">Email</label>
        <div class="col-sm-10">
          <input type="email" name="user_email" value="${editUser.user_email}"  class="form-control" id="inputEmail"
            placeholder="" required="true">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
        <div class="col-sm-10">
          <input type="password" name="user_password" class="form-control" id="inputPassword" placeholder="" required="true">
        </div>
      </div>
        <div class="form-group row">
        <div class="col-sm-12">
          <c:if test="${editUser.user_id == null || editUser.user_id < 0}">
             <input type="hidden" name="user_id" value="-1" />
             <button type="submit" class="btn btn-primary">Pridať</button>
          </c:if>
          <c:if test="${editUser.user_id >= 0}">
              <input type="hidden" name="user_id" value="${editUser.user_id}" />
              <button type="submit" class="btn btn-warning">Uložiť zmeny</button>
          </c:if>
        </div>
      </div>
    </form>
  </div>
</div>

    <div class="row">&nbsp;</div>

    <div class="card">
        <h5 class="card-header">Zoznam užívateľov</h5>
        <div class="card-body">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">Login</th>
                  <th scope="col">e-Email</th>
                  <th scope="col">Akcia</th>
                </tr>
              </thead>
              <tbody>
                 <c:forEach items="${users}" var="user" varStatus="loop">
                    <tr>
                      <td>${loop.index+1}</td>
                      <td><a href="<c:url value='${pageContext.request.contextPath }/userpage/${user.user_id}' />">${user.user_login}</a>
                      </td>
                      <td>${user.user_email}</td>
                      <td>
                         <form method="post" action="${pageContext.request.contextPath }/deleteUser">
                            <input type="hidden" name="user_id" value="${user.user_id}" />
                            <button type="submit" class="btn btn-outline-danger">Zmaž</button>
                         </form>
                      </td>
                    </tr>
                 </c:forEach>
              </tbody>
            </table>
            <ul class="list-group">
                <c:if test="${message != null}">
                   <li class="list-group-item">${message}</li>
                </c:if>
            </ul>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    </div>
    <!--container end.//-->

</body>
</html>

