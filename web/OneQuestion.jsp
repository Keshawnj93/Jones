<%-- 
    Document   : OneQuestion
    Created on : Feb 14, 2018, 10:08:32 PM
    Author     : Keshawn
--%>

<!-- Use the following URL to access on local machine: 
http://localhost:8080/Jones/OneQuestion.jsp?chapterNo=1&questionNo=1&&title=project1 
-->

<%@page import= "Beans.QuestionBean" contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "QuestionBeanID" class= "Beans.QuestionBean" scope= "session">
</jsp:useBean>

<% QuestionBeanID.setChapterNo(Integer.parseInt(request.getParameter("chapterNo")));%>
<% QuestionBeanID.setQuestionNo(Integer.parseInt(request.getParameter("questionNo")));%>
<% QuestionBeanID.setTitle(request.getParameter("title")); %>
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
                <p style="background-color: slategray; color: white; text-align: center; padding: 0px"> Multiple-Choice Question <jsp:getProperty name="QuestionBeanID" property="title" /> </p>                
            </div>
            
            <div style="border: 1px solid orange; padding-top: 0px">
                <p><jsp:getProperty name="QuestionBeanID" property="body" /></p>
            </div>
        </form>
    </body>
</html>
