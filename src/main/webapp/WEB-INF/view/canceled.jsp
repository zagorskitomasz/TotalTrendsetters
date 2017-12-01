<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page="/WEB-INF/view/templates/header.jsp"/>
	<body>
		<jsp:include page="/WEB-INF/view/templates/navbar.jsp"></jsp:include>
    	<div class="container">
    	<div class="page-header">
    		<h1>Canceled</h1>
    		</div>
    	</div>
    	<div class="container theme-showcase" role="main">
    		<div class="alert alert-warning" role="alert">
         	Your computation was canceled.
      		</div>
      
    		<c:url var="homeLink" value="/">
			</c:url>

    		<div class="btn-group" role="group" aria-label="...">
				<button type="button" onclick="location.href='${homeLink}'" class="btn btn-default ">Home</button>
			</div>

		</div>
		<jsp:include page="/WEB-INF/view/templates/footer.jsp"></jsp:include>
	</body>
</html>