<%--
 * Main page representing base page for managing of data. Lists shows all orders from all users.
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
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.8/css/all.css">

<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

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
                <li class="breadcrumb-item active"><a href="#">Hlavné menu</a></li>
                <li class="ml-auto">Prihlásený užívateľ: <strong>${sessionScope.username}</strong>
                                                    &nbsp; | &nbsp;
                                                    <a href="${pageContext.request.contextPath }/logout">Odhlásiť sa</a></li>
            </ol>
        </nav>

        <div class="row">&nbsp;</div>

        <div class="card">
         <div class="card-body">
            <div class="form-group row">
                <form method="get" action="${pageContext.request.contextPath }/mainpage">
                    <button type="submit" formaction="/userpage/${-1}" class="btn btn-outline-success">Správa užívateľov</button>
                    <button type="submit" formaction="/productpage/${-1}" class="btn btn-outline-danger">Správa produktov</button>
                    <button type="submit" formaction="/orderpage/${-1}" class="btn btn-outline-warning">Správa objednávok</button>
                </form>
            </div>
          </div>
        </div>


        <div class="row">&nbsp;</div>

        <div class="card">
         <h5 class="card-header">Zoznam všetkých objednávok od všetkých užívateľov</h5>
         <div class="card-body">
            <div class="form-group row">
                <div class="col-sm-12">
                <table class="table table-hover">
                                  <thead>
                                    <tr>
                                      <th scope="col">#</th>
                                      <th scope="col">Užívateľ</th>
                                      <th scope="col">Číslo OBJ</th>
                                      <th scope="col">Názov</th>
                                      <th scope="col">Zľava</th>
                                      <th scope="col">Priradené produkty</th>
                                      <th scope="col">Celková cena</th>
                                      <th scope="col">Zľava z ceny</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                     <c:forEach items="${orders}" var="order" varStatus="loop">
                                        <tr name="order_id" value="${order.order_id}">
                                          <td>${loop.index+1}</td>
                                          <td>${order.userOrder.user_login}</td>
                                          <td>${order.order_no}</td>
                                          <td>${order.order_name}</td>
                                          <td>${order.order_discount} %</td>
                                          <td>
                                              <c:forEach items="${order.orderProducts}" var="product">
                                                 ${product.product_code},
                                              </c:forEach>
                                          </td>
                                          <td><c:out value="${order.getTotalOrderPrice()}" /> &euro;</td>
                                          <td><c:out value="${order.getTotalOrderPriceDiscount()}" /> &euro;</td>

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
          </div>
        </div>

        <div class="row">&nbsp;</div>


    </div>
    <!--container end.//-->

</body>
</html>

