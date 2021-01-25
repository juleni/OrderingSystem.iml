<%--
 * Page representing managing of Order data (create/update/delete)
 * Lists shows all orders related to current logged in user.
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
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath }/mainpage">Hlavné menu</a></li>
            <li class="breadcrumb-item active"><a href="#">Správa Objednávok</a></li>
            <li class="ml-auto">Prihlásený užívateľ: <strong>${sessionScope.username}</strong>
                                                &nbsp; | &nbsp;
                                                <a href="${pageContext.request.contextPath }/logout">Odhlásiť sa</a></li>
        </ol>
    </nav>

    <div class="row">&nbsp;</div>


<div class="card">
  <h5 class="card-header">Nová objednávka</h5>
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
  <p class="card-text">Pridaná bude len objednávka s minimálne jedným priradeným produktom.</p>
    <form method="post" action="${pageContext.request.contextPath }/addOrder">
      <div class="form-group row">
        <label for="inputNo" class="col-sm-2 col-form-label">Číslo</label>
        <div class="col-sm-10">
          <input type="text" name="order_no" class="form-control" id="inputNo"
                 value="<c:out value="${editOrder.order_no}" />"
                 placeholder="<c:out value="${editOrder.order_no}" />" required="true">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputName" class="col-sm-2 col-form-label">Názov</label>
        <div class="col-sm-10">
          <input type="text" name="order_name" value="${editOrder.order_name}"
                 class="form-control" id="inputName" placeholder="" required="true">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputDiscount" class="col-sm-2 col-form-label">Zľava</label>
        <div class="col-sm-10">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text">&euro;</span>
              </div>
              <input type="text" name="order_discount" value="${editOrder.order_discount}"
                     class="form-control" id="inputDiscount" placeholder="" required="true">
            </div>
        </div>
      </div>

      <div class="form-group row">
        <label for="productMultiSelect" class="col-sm-2 col-form-label">Produkty</label>
        <div class="col-sm-10">
            <select multiple class="form-control" id="productsMultiSelect" name="productIDs" value="${productIDs}" >
                 <c:forEach items="${products}" var="product">
                    <option value="${product.product_id}"
                      <c:if test="${productIDs.contains(product.product_id)}">
                         selected="selected"
                      </c:if>
                    >${product.product_name}</option>
                 </c:forEach>
            </select>
        </div>
      </div>

      <div class="form-group row">
        <label for="orderTextarea" class="col-sm-2 col-form-label">Popis</label>
        <div class="col-sm-10">
            <textarea name="order_desc" class="form-control" id="orderTextarea" rows="3">${editOrder.order_desc}</textarea>
        </div>
      </div>


      <div class="form-group row">
        <div class="col-sm-12">
          <c:if test="${editOrder.order_id == null || editOrder.order_id < 0}">
            <input type="hidden" name="order_id" value="-1" />
            <button type="submit" class="btn btn-primary">Pridať</button>
          </c:if>
          <c:if test="${editOrder.order_id >= 0}">
              <input type="hidden" name="order_id" value="${editOrder.order_id}" />
              <button type="submit" class="btn btn-warning">Uložiť zmeny</button>
          </c:if>
        </div>
      </div>

    </form>
  </div>
</div>

    <div class="row">&nbsp;</div>

    <div class="card">
        <h5 class="card-header">Zoznam objednávok aktuálne prihláseného užívateľa</h5>
        <div class="card-body">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">Číslo</th>
                  <th scope="col">Názov</th>
                  <th scope="col">Zľava</th>
                  <th scope="col">Priradené produkty</th>
                  <th scope="col">Celková cena</th>
                  <th scope="col">Zľava z ceny</th>
                  <th scope="col">Akcia</th>
                </tr>
              </thead>
              <tbody>
                 <c:forEach items="${orders}" var="order" varStatus="loop">
                    <tr name="order_id" value="${order.order_id}">
                      <td>${loop.index+1}</td>
                      <td><a href="<c:url value='${pageContext.request.contextPath }/orderpage/${order.order_id}' />">${order.order_no}</a>
                      </td>
                      <td>${order.order_name}</td>
                      <td>${order.order_discount} %</td>
                      <td>
                          <c:forEach items="${order.orderProducts}" var="product">
                             ${product.product_code},
                          </c:forEach>
                      </td>
                      <td><c:out value="${order.getTotalOrderPrice()}" /> &euro;</td>
                      <td><c:out value="${order.getTotalOrderPriceDiscount()}" /> &euro;</td>
                      <td>
                         <form method="post" action="${pageContext.request.contextPath }/deleteOrder">
                            <input type="hidden" name="order_id" value="${order.order_id}" />
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

