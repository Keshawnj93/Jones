<%-- 
    Document   : GradeOneQuestion
    Created on : Feb 26, 2018, 1:59:37 AM
    Author     : Keshawn
--%>

<%@page import= "Beans.QuestionBean" contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "QuestionBeanID" class= "Beans.QuestionBean" scope= "session">
</jsp:useBean>
<% QuestionBeanID.setMode(request.getParameter("mode")); %>
<% QuestionBeanID.setChecked(request.getParameterValues("choices")); %>
<% QuestionBeanID.setSelected(request.getParameter("choices")); %>
<% QuestionBeanID.printPage(); %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Project 1 - Keshawn Jones</title>
    </head>
    <body style="margin-left: auto; margin-right: auto; width: 35%">
        <form>
            <div>
                <p style="background-color: midnightblue; color: white; text-align: center; font-size : 18px"> 
                    Multiple-Choice Question <jsp:getProperty name="QuestionBeanID" property="title" /> </p>
            </div>
            
            <div style="border: 1px solid orange; padding-top: 0px">
                <p><jsp:getProperty name="QuestionBeanID" property="body" /></p>
            </div>
        </form>
    </body>
</html>
