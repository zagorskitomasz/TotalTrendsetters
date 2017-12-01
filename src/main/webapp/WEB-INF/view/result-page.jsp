<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import = "java.io.*,java.util.*" %>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page="/WEB-INF/view/templates/header.jsp"/>
	<body>
		<jsp:include page="/WEB-INF/view/templates/navbar.jsp"></jsp:include>
    	<div class="container">
    	<div class="page-header">
    	 <%
            response.setIntHeader("Refresh", 5);
         %>
    		<h1>Computation Result</h1>
    		</div>
    		<c:choose>
    		<c:when test="${computing=='false'}">
    		<div class="col-sm-6">
    	    	<div class="table-responsive">
  				<table class="table table-bordered table-striped">
    				<tbody>
						<tr>
							<td><p class="text-right">Graph name</p></td>
							<td><Strong>${graphName}</Strong></td>
						</tr>
						<tr>
							<td><p class="text-right">Graph size</p></td>
							<td><Strong>${graphSize}</Strong></td>
						</tr>
						<tr>
							<td><p class="text-right">Converted % of users</p></td>
							<td><Strong>${convPercent}</Strong></td>
						</tr>
						<tr>
							<td><p class="text-right"><Strong>Number of trendsetters</Strong></p></td>
							<td><Strong>${convByTrues}</Strong></td>
						</tr>
						<tr>
							<td><p class="text-right">New preference attractivity</p></td>
							<td><Strong>${attTrue}</Strong></td>
						</tr>
						<tr>
							<td><p class="text-right">Old preference attractivity</p></td>
							<td><Strong>${attFalse}</Strong></td>
						</tr>
    				</tbody>
 				</table>
				</div>
			</div>
			</c:when>
			<c:otherwise>
				<div class="container theme-showcase" role="main">
    				<div class="alert alert-warning" role="alert">
         				Computation in progress, page will refresh automatically. Please wait...
      				</div>
      
    		<c:url var="cancelLink" value="/cancel">
			</c:url>
			<button type="button" onclick="location.href='${cancelLink}'" class="btn btn-danger ">Cancel</button>
			</div>
			</c:otherwise>
			</c:choose>
    	</div>
		<jsp:include page="/WEB-INF/view/templates/footer.jsp"></jsp:include>
	</body>
</html>