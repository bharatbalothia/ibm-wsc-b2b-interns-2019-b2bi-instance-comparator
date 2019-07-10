<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.instance.comparator.model.TPRESTCalls,java.util.*,java.util.concurrent.*, java.util.Set, java.util.HashSet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>WorkFlows Comparison</title>
<script type="text/javascript" src="//code.jquery.com/jquery-3.3.1.js"></script>
<script type="text/javascript" src="//cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.7.0/css/all.css' integrity='sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ' crossorigin='anonymous'>
<link rel="stylesheet" type="text/css" href="css/login.css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="shortcut icon" href="//www.ibm.com/favicon.ico" type=image/vnd.microsoft.icon>
<link rel="stylesheet" href="//cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">
</head>
<%!
public static String List1[];
public static String List2[];
public static String List[] = new String[1000];
int len1=0;
int len2=0;
int greater,lesser,k=0;
%>

<body style="overflow:scroll; max-height: 100%;">
	<div class="topnav">
		<img src="images/ibm-logo.svg">
<a class="hover" href="/Instance_Comparator/businessprocess">Business Process</a> 
		<a class="hover" href="/Instance_Comparator/tradingpartners">SFG Trading Partners</a>
		<a class="hover" href="#">Adapter</a>
	</div>
<div id='stars'></div>
<div id='stars2'> </div>
<div id='stars3'></div>
<div class="w3-container">
<div class="nav">


<%
len1 =  TPRESTCalls.len1;
len2 =  TPRESTCalls.len2;
List1 = new String[len1];
List2 = new String[len2];
List1 = TPRESTCalls.hostList1;
List2 = TPRESTCalls.hostList2;

%>
<%//Matching Workflows%>
<%if(len1 > len2){ %>
<%for(int i=0;i<len1;i++){%>
<%for(int j=0;j<len2;j++){
if(List1[i].equals(List2[j]))
		{
			List[k]=List1[i];
			k++;
			break;
		}
		}
		}
%>
<%}else	{%>
<%for(int i=0;i<len2;i++){%>
<%for(int j=0;j<len1;j++){
if(List2[i].equals(List1[j]))
		{
			List[k]=List2[i];
			k++;
			break;
		}
		}
		}
}%>

<button onclick="myFunction('ThirdTab')" class="w3-btn w3-block w3-green" >
Matched Workflows(<%= k %>)</button>

<div id="ThirdTab" class="w3-hide w3-container w3-white">
  <table id="datatables3" class="display">
  <thead>
  <tr>
  <th>Workflows</th>
  </tr>
  </thead>
  <tbody>
  <%for(int i=0; i<k;i++) {%>
  <tr>
    <td><%= List[i]%></td>
    </tr>
 <%}
  Arrays.fill(List1,null);
  Arrays.fill(List2,null);
  Arrays.fill(List,null);
  k=0;
  %>
  
  </tbody>
  </table>
</div>
</div>
</div>
</body>


<script>
function myFunction(id) {
  var x = document.getElementById(id);
  if (x.className.indexOf("w3-show") == -1) {
    x.className += " w3-show";
  } else { 
    x.className = x.className.replace(" w3-show", "");
  }
}
</script>



<script>
$(document).ready(function() {
    $('#datatables1').DataTable();
} );
</script>