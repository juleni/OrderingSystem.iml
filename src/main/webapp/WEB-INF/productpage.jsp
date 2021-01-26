<%--
 * Page representing managing of Product data (create/update/delete)
 * Lists shows all products related to current logged in user.
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

<script>
    // TODO: Move this js code to the extra .js file
    // this function is called on submit to check numeric field product_price
    function numberValidation() {
        var n = document.getElementById("inputPrice").value;
        // whether passed variable is number or not
        if (isNaN(n)) {
            document.getElementById("validationPriceText").style.color = "red";
            document.getElementById("validationPriceText").innerHTML =
              "Cena nebola zadaná správne.";
            return false;
        } else {
            return true;
        }
    }
</script>
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
            <li class="breadcrumb-item active"><a href="#">Správa Produktov</a></li>
            <li class="ml-auto">Prihlásený užívateľ: <strong>${sessionScope.username}</strong>
                                                &nbsp; | &nbsp;
                                                <a href="${pageContext.request.contextPath }/logout">Odhlásiť sa</a></li>
        </ol>
    </nav>

    <div class="row">&nbsp;</div>


<div class="card">
  <h5 class="card-header">Nový produkt</h5>
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
  <p class="card-text">Pridaný bude len produkt s kódom, ktorý sa ešte v systéme nenachádza pre aktuálneho užívateľa.</p>
    <form method="post" action="${pageContext.request.contextPath }/addProduct" onsubmit="return numberValidation();">
      <div class="form-group row">
        <label for="inputCode" class="col-sm-2 col-form-label">Kód</label>
        <div class="col-sm-10">
          <input type="text" name="product_code" value="${editProduct.product_code}" class="form-control" id="inputCode" placeholder="" required="true">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputName" class="col-sm-2 col-form-label">Názov</label>
        <div class="col-sm-10">
          <input type="text" name="product_name" value="${editProduct.product_name}" class="form-control" id="inputName" placeholder="" required="true">
        </div>
      </div>

      <div class="form-group row">
        <label for="inputPrice" class="col-sm-2 col-form-label">Cena</label>
        <div class="col-sm-10">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text">&euro;</span>
                <span class="input-group-text">0.00</span>
              </div>
              <input type="text" name="product_price" value="${editProduct.product_price}" class="form-control" id="inputPrice" placeholder="" required="true">
            </div>
           <div>
             <span id="validationPriceText"></span>
           </div>
        </div>
      </div>
      <div class="form-group row">
        <label for="productTextarea" class="col-sm-2 col-form-label">Popis</label>
        <div class="col-sm-10">
            <textarea name="product_desc" class="form-control" id="productTextarea" rows="3">${editProduct.product_desc}</textarea>
        </div>
      </div>
        <div class="form-group row">
        <div class="col-sm-12">
          <c:if test="${editProduct.product_id == null || editProduct.product_id < 0}">
             <input type="hidden" name="product_id" value="-1" />
             <button type="submit" class="btn btn-primary">Pridať</button>
          </c:if>
          <c:if test="${editProduct.product_id >= 0}">
              <input type="hidden" name="product_id" value="${editProduct.product_id}" />
              <button type="submit" class="btn btn-warning">Uložiť zmeny</button>
          </c:if>
        </div>
      </div>
    </form>
  </div>
</div>

    <div class="row">&nbsp;</div>

    <div class="card">
        <h5 class="card-header">Zoznam produktov aktuálneho užívateľa</h5>
        <div class="card-body">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">Kód</th>
                  <th scope="col">Názov</th>
                  <th scope="col">Cena</th>
                  <th scope="col">Popis</th>
                  <th scope="col">Akcia</th>
                </tr>
              </thead>
              <tbody>

                 <c:forEach items="${products}" var="product" varStatus="loop">
                    <tr name>
                      <td>${loop.index+1}</td>
                      <td><a href="<c:url value='${pageContext.request.contextPath }/productpage/${product.product_id}' />">${product.product_code}</a>
                      </td>
                      <td>${product.product_name}</td>
                      <td>${product.product_price}</td>
                      <td>${product.product_desc}</td>
                      <td>
                         <form method="post" action="${pageContext.request.contextPath }/deleteProduct">
                            <input type="hidden" name="product_id" value="${product.product_id}" />
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

