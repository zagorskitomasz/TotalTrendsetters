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
    		<h1>Home Page</h1>
    		</div>
    		<Strong>Welcome to Total Trendsetters Web Application.</Strong><br><br>
    		Total Trendsetters is smallest group of Facebook users, whose preference change would spread in entire network (due to data flow algorithm).<br>
    		Application uses complex heuristic algorithm to approximate NP-hard problem solution.<br>
    		Firstly, algorithm converts major part of users connected to those with greatest number of friends.<br>
    		It leaves group of stubborn users, which have to be converted separately - it's task for second part of algorithm.<br><br> 
    		<Strong>Computation is rather heavy (but precise) so be patient if you play with UCSD data set.<br>
    		Because of that there is only one computation slot.</Strong>
    	</div>
		<jsp:include page="/WEB-INF/view/templates/footer.jsp"></jsp:include>
	</body>
</html>