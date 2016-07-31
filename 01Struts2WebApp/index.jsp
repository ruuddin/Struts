
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="sjt" uri="/struts-jquery-tree-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TEST SERVLET</title>
</head>
<body>

<h1>ALM SERVLET EXPERIMENT</h1>
<form action="MyFirstServlet" method="get">
Choose Course : <select name=“course”>
<option value="Java SE">Java SE </option>
<option value="Java EE">Java EE</option>
<option value="J2ME">J2ME</option>
</select>
<input type="submit" value="submit" />
</form> 

               
        <sjt:tree 
                id="treeDynamicAjax" 
                jstreetheme="apple"
                rootNode="nodes"
                childCollectionProperty="children"
                nodeTitleProperty="title"
                nodeIdProperty="id"
                nodeHref="%{echo}"
                nodeHrefParamName="echo"
                nodeTargets="result2"
                contextmenu="{
                        items: { 
                                'create' : false,
                                'rename' : false,
                                'ccp' : false,
                                'remove' : { 
                                        'label': 'Delete this Node', 
                                        'action':  function (obj) { this.remove(obj); deleteTreeNode('%{echo}', obj); }
                                } 
                        } 
                }"
        />

  <strong>Result Div :</strong>
  <div id="result2" class="result ui-widget-content ui-corner-all">Click on the AJAX Links above.</div>




</body>
</html>