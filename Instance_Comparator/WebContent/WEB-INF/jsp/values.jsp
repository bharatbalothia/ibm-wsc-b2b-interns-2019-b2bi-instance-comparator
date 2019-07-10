<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.instance.comparator.model.RESTCalls,java.util.*,java.util.concurrent.*, java.util.Set, java.util.HashSet" %>
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
public static String WFCodediff[];
public static String List[] = new String[1000];
int len1=0;
int len2=0;
int greater,lesser,k=0;
public static Map<Integer,String> Program1 = new ConcurrentHashMap<Integer, String>();
public static Map<Integer,String> Program2 = new ConcurrentHashMap<Integer, String>();
public static Map<Integer,String> Time1 = new ConcurrentHashMap<Integer, String>();
public static Map<Integer,String> Time2 = new ConcurrentHashMap<Integer, String>();
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
len1 =  RESTCalls.len1;
len2 =  RESTCalls.len2;
List1 = new String[len1];
List2 = new String[len2];
List1 = RESTCalls.hostList1;
List2 = RESTCalls.hostList2;
Program1.putAll(RESTCalls.Program1);
Program2.putAll(RESTCalls.Program2);
WFCodediff = new String[RESTCalls.diffLen];
WFCodediff = RESTCalls.codeDiff;
k=0;
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

<!--  Creating Table to display -->

<br><br><br><br>
<% Set<String> diff1 = new HashSet<String>(Arrays.asList(List1));
	Set<String> diff2 = new HashSet<String>(Arrays.asList(List2));  
    diff1.removeAll(Arrays.asList(List2));
    diff2.removeAll(Arrays.asList(List1));
    String[] missing1 = {};
    String[] missing2 = {};
    missing1 = diff1.toArray(missing1);
    missing2 = diff2.toArray(missing2);
%>
<button onclick="myFunction('FirstTab')" class="w3-btn w3-block w3-red">
Missing Workflows(<%=(missing1.length) + (missing2.length) %>)</button>

<div id="FirstTab" class="w3-hide w3-container w3-white">
  <table id="datatables1" class="display">
  <thead>
  <tr>
  <th><%=RESTCalls.urlName1 %></th>
  <th><%=RESTCalls.urlName2 %></th>
  </tr>
  </thead>
  <tbody>
<%//Missing Workflows%>
    <% for(int i=0,j=0; i<missing1.length || j<missing2.length;i++,j++){
    %>
		  <tr>
    <% if(i<missing1.length) {%>
    <td><%= missing1[i]%></td>
    <%}
    else{%>
    	<td><%= " "%></td>	
    <%}
    if(j<missing2.length) {%>
    <td><%= missing2[j]%></td>
    <%}
    else {%>
    <td><%= " "%></td>
    <%}
    }
    Arrays.fill(missing1,null);
    %>
    </tr>
  </tbody>
  </table>
</div>
	    	     
<button onclick="myFunction('SecondTab')" class="w3-btn w3-block w3-orange" >
Mismatched Workflows (<%= Program1.size()%>)</button>
<% 
Iterator<Integer> cd1 = Program1.keySet().iterator();
Iterator<Integer> cd2 = Program1.keySet().iterator();
%>
<script>

var myData = [];
for(var i = 0; i < <%= Program1.size()%>; i++)
	{
	<%Integer key = cd1.next(); %>
	var data = {"id": i,"code1": <%= Program1.get(key).toString()%> ,"code2": <%= Program2.get(key).toString()%>}
	myData.push(data)
	}
	
var myJSON = {"data":myData};


</script>
<div id="SecondTab" class="w3-hide w3-container w3-white">
  <table id="datatables2" class="display">
  <thead>
  <tr>
   <th>Workflow 1</th>
    <th>Workflow 2</th>
  </tr>
  </thead>
  <tbody>
<%//Mismatched Workflows%>

 <% int j=0;
 while (j<RESTCalls.diffLen){%>
	 <tr>
    <td ><%= WFCodediff[j]%></td>
    <td><%= WFCodediff[j]%></td>
     </tr>
    <% j++;
    }
 Program1.clear();
 Program2.clear();
 Time1.clear();
 Time2.clear();
 Arrays.fill(WFCodediff,null);
    %>
   
  </tbody>
  </table>
</div>

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
	
$(document).ready(function() {
    $('#datatables2').DataTable();
} );



function format ( d ) {
    // `d` is the original data object for the row
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<tr>'+
            '<td>Code related to first workflow:</td>'+
            '<td>'+d.code1+'</td>'+
        '</tr>'+
        '<tr>'+
            '<td>Code related to second workflow:</td>'+
            '<td>'+d.code2+'</td>'+
        '</tr>'+
    '</table>';
}

$('#datatables2 tbody').on('click', 'td.details-control', function () {
    var tr = $(this).closest('tr');
    var row = table.row( tr );

    if ( row.child.isShown() ) {
        // This row is already open - close it
        row.child.hide();
        tr.removeClass('shown');
    }
    else {
        // Open this row
        row.child( format(myJSON) ).show();
        tr.addClass('shown');
    }
} );



$(document).ready(function() {
    $('#datatables3').DataTable();
} );
</script>

</body>
</html>