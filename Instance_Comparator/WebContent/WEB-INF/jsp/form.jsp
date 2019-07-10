<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>IBM Instance Comparator</title>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" type ="text/css" href="css/login.css">
<link rel="shortcut icon" href="//www.ibm.com/favicon.ico" type=image/vnd.microsoft.icon>
<link href='https://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
<style>
.container-fluid {
	margin-top: 68px;
}
p.indent{ padding-left: 1.8em }
</style>
</head>

<body>
<div class="topnav">
</div>
<div id='stars'></div>
<div id='stars2'> </div>
<div id='stars3'></div>



<div class="wrapper fadeInDown">

  <div id="formContent">
    <!-- Tabs Titles  -->
	<f:form class="login100-form validate-form flex-sb flex-w" method="POST" action="/Instance_Comparator/form"
		modelAttribute="formsubmit" style="display: flex; flex-flow: row wrap;  align-items: center;" id="login_form">
				<h2> Instance Comparator</h2> 
				<b>_________________________________</b><br><br><br>
			<h3>
			First Instance</h3>
			<br/><br/>
				<f:input path="HostName" required="required" placeholder="URL" style="width: 445px;"/>
				<br>
				<f:input path="UserName" required="required" placeholder="UserName" />
				<f:input path="Password" required="required" placeholder="Password" type="password"/>
				<br>
				
				 <br><br>
				<h3>
			Second Instance</h3>
			   <br>
			   <f:input path="HostName2" required="required" placeholder="URL" style="width: 445px;"/>
				<br>
				<f:input path="UserName2" required="required" placeholder="UserName"/>
				
				<f:input path="Password2" required="required" placeholder="Password" type="password"/>
				
				<br><br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="fadeIn fourth" type="submit" value="Submit"  />
			
	</f:form>

  </div>
</div>
<script type="text/javascript">
$('#login_form').submit(function() {
    $('#gif').css('visibility', 'visible');
});
</script>
</body>
</html>