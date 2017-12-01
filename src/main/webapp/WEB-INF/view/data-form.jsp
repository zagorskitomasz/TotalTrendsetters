<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<% response.setCharacterEncoding("UTF-8"); request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page="/WEB-INF/view/templates/header.jsp"/>
	<body>
		<jsp:include page="/WEB-INF/view/templates/navbar.jsp"></jsp:include>
    	<div class="container">
    	<div class="page-header">
    		<h1>Computation Form</h1>
    	</div>
    	<form:form class="form-horizontal" action="/compute" modelAttribute="compDataSet" method="POST">
			<div class="form-group">
   				<label class="col-sm-2 control-label">Choose data set:</label>
    			<div class="col-sm-4">
      						<form:select path="graphName">
      							<form:option value="">--Select--</form:option>
      							<form:options items="${graphNameOptions}"/>
							</form:select>
							<strong><form:errors path="graphName" cssClass="error"/></strong>
    			</div>
  			</div>
			<div class="form-group">
   				<label class="col-sm-2 control-label">Convert % of users:</label>
    			<div class="col-sm-4">
      				<form:input class="form-control" path="threshold"/>
      				<strong><form:errors path="threshold" cssClass="error"/></strong>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-2 control-label">New preference attractivity</label>
    			<div class="col-sm-4">
      				<form:input class="form-control" path="attractivityTrue"/>
      				<strong><form:errors path="attractivityTrue" cssClass="error"/></strong>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-2 control-label">Old preference attractivity</label>
    			<div class="col-sm-4">
      				<form:input class="form-control" path="attractivityFalse"/>
      				<strong><form:errors path="attractivityFalse" cssClass="error"/></strong>
    			</div>
  			</div>
   			<div class="form-group">
    			<div class="col-sm-offset-2 col-sm-4">
      				<input type="submit" class="btn btn-primary " value="Compute"/>
      			</div>
  			</div>
		</form:form>
		</div>
		<jsp:include page="/WEB-INF/view/templates/footer.jsp"></jsp:include>
	</body>
</html>