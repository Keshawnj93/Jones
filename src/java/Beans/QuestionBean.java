package Beans;

import Objects.*;

public class QuestionBean {
    private int chapterNo, questionNo;
    private String title, body, mode, selected;
    private String[] checked;
    DBManager db;
    
    public QuestionBean(){
        chapterNo = 1; questionNo = 1;
        title = "Chapter 1 Question 1";
        body = "";
        mode = "Display";
        db = new DBManager();
        
    }
    
    public void printPage(){
        body = "";
        Question q = db.retrieve(chapterNo, questionNo);
        //invalid questionNo or chapterNo.
        if (chapterNo == -1 || questionNo == -1){
            body = "<font color=\"red\"> Please only enter integers into the chapterNo and questionNo field. <font/>";
        }
        
        //chapter/question combination not found
        else if (q.getAnswerKey() == null){
            body = "<font color=\"red\"> The requested question could not be found <font/>";
        } else {
            //question found. print.
            printQuestion(q);
            printChoices(q);
            
            //Display mode. Print the Check My Answer button
            if (mode.equals("Display")){
                body = body + "<br /><input type=\"submit\" name=\"mode\" value=\"Check My Answer\" style=\"background-color : green\">";
            }
            
            //Check Answer mode. Print answer correctness, hint, and Get Statistics button
            else if (mode.equals("Check My Answer")){                              
                try{
                    if (checked.length > 0){
                        Answer a = updateAnswer(q);
                        String answers = "";
                            for (String s : checked){
                                switch (s){
                                    case "0" : answers += "A"; break;
                                    case "1" : answers += "B"; break;
                                    case "2" : answers += "C"; break;
                                    case "3" : answers += "D"; break;
                                    case "4" : answers += "E"; break;    
                                }
                            }
                    
                        //Correct answer. Inform user
                        if (a.getIsCorrect() == 1){
                            body = body + "<br /><font color=\"green\"><p>Your answer " + answers + " is correct <img src=\"img/project1Check.jpg\"></p></font>";
                            
                            if (q.getHint() == null || q.getHint().equals("null")){
                               // no Hint
                            }
                            
                            //Hint can be displayed
                            else {
                                body = body + "<font color=\"green\"><p onclick=\'displayHint()\'>Click here to show an explaination</p></font>"
                                        + "<font color=\"darkviolet\"><p id=\"hint\"></p>";
                            }
                        }
                    
                        //Incorrect answer. Inform user
                        if (a.getIsCorrect() == 0){
                        
                            body = body + "<br /><font color=\"red\"><p>Your answer " + answers + " is incorrect <img src=\"img/project1X.jpg\"></p></font>";
                            //No hint
                            if (q.getHint() == null || q.getHint().equals("null")){
                                body = body + "<font color=\"green\"><p id=\"answer\" onClick=\"displayAnswer()\">Click here to show the correct answer</p></font>";
                            }
                    
                            //Hint
                            else {
                                body = body + "<font color=\"green\"><p id=\"answer\" onClick=\"displayAnswer()\">Click here to show the correct answer and an explaination</p></font>"
                                        + "<font color=\"darkviolet\"><p id=\"hint\"></p>";
                            }
                        }
                    }
                } catch (Exception e){
                    //No answer selected. Notify user
                    body = body + "<br /><p>You didn't answer this question <img src=\"img/project1Question.jpg\"></p>";
                    //No hint
                    if (q.getHint() == null || q.getHint().equals("null")){
                        body = body + "<font color=\"green\"><p id=\"answer\" onClick=\"displayAnswer()\">Click here to show the correct answer</p></font>";
                    }
                    
                    //Hint
                    else {
                        body = body + "<font color=\"green\"><p id=\"answer\" onClick=\"displayAnswer()\">Click here to show the correct answer and an explaination</p></font>"
                                + "<font color=\"darkviolet\"><p id=\"hint\"></p>";
                    }
                }
                
                //Print Get Statistics button. Not yet functional.
                body = body + "<input type=\"button\" value=\"Get Statistics\" style=\"background-color : green\">";
                
                //Print onClick function to display answer and hint
                printJS(q);
            }
            
        }
    }
    
    private void printQuestion(Question q){
        //The question contains only one line of text. No code formatting needed
        if (!q.getText().contains("\n")){
            body = body + replaceTags(q.getText()) + "<br /><br />";
        }
        
        //The question contains multiple lines. Code must be formatted
        else {
            formatCode(q);
        }
    }
    
    private void formatCode(Question q){
        //Print fist line, containing no code
        String s = replaceTags(q.getText());
        String st = s.substring(0, s.indexOf("\n"));
        body = body + st + "<br /><font size=\"3\" face=\"courier new\">";
        
        //While there are still /n in the string, print the lines of code
       s = s.substring(s.indexOf("\n") + 1);
       while (s.contains("\n")){
           s = replaceTags(s);
           body = body + "&emsp;"; 
           //If starts with a space, print a \t
           if (s.startsWith(" ")){
               while (s.startsWith(" ")){
                   body = body + "&emsp;";
                   s = s.substring(1);
               }
           }
           st = s.substring(0, s.indexOf("\n"));
           st = colorCode(st);
           body = body + st + "<br />";
           s = s.substring(s.indexOf("\n") + 1);
       }
       
       //Print last line of code, if exists
       if (!s.isEmpty()){
           replaceTags(s);
           s = colorCode(s);
           body = "&emsp;" + body + s + "<br />";
       }
       
       body = body + "<br /></font>";
    }
    
    private String colorCode(String s){
        String ret = colorKeyword(s);
        ret = colorNum(ret);
        ret = colorComment(ret);
        return ret;
    }
    
    private String colorKeyword(String s){
        String[] keywords = {"abstract", "assert", "boolean", "break", "byte",
            "switch", "case", "try", "catch", "finally", "char", "class", "continue",
            "default", "do", "double", "if", "else", "enum", "extends", "final",
            "float", "for", "implements", "import", "instanceOf", "int", "interface",
            "long", "native", "new", "package", "private", "protected", "public", "return", 
            "short", "static", "strictfp", "super", "synchronized", "this", "throw",
            "throws", "transient", "void", "volatile", "while", "goto", "const"};
        
        String ret = "";
        //First check. Iterate through keywords in order
        for (String k : keywords){
            try{
                if (s.contains(k) && s.charAt(s.indexOf(k) + k.length()) == ' '){
                    while (s.contains(k)){
                        if (s.charAt(s.indexOf(k) + k.length()) == ' '){
                            ret += s.substring(0, s.indexOf(k)) + "<font color=\"green\">" + k + "</font>";
                            s = s.substring(s.indexOf(k) + k.length());
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e){ // Keyword is at the end of the line
                while (s.contains(k)){
                    ret += s.substring(0, s.indexOf(k)) + "<font color=\"green\">" + k + "</font>";
                    s = s.substring(s.indexOf(k) + k.length());
                }
            }
        }
        
        //Second check. Iterate through keywords in reverse order
        for (int i = keywords.length; i > 0; i--){
            String k = keywords[i-1];
            if (ret.contains(k) && ret.charAt(ret.indexOf(k) + k.length()) == ' '){
                String temp = ret;
                while (temp.contains(k)){
                    if (temp.charAt(temp.indexOf(k) + k.length()) == ' '){
                        ret = "<font color=\"green\">" + k + "</font>" + temp.substring(temp.indexOf(k) + k.length());
                        temp = temp.substring(temp.indexOf(k) + k.length());
                    }
                }
            }
        } 
        
        ret += s;
        if (!ret.isEmpty()) return ret;
        return s;
    }
    
    private String colorNum(String s){
        String ret = "";
        char last = 'a';
        while (!s.isEmpty()){
            if (!ret.isEmpty()){
                switch(s.charAt(0)){
                    //Check to see if first char in number.
                    case '0' : 
                    case '1' : 
                    case '2' : 
                    case '3' : 
                    case '4' : 
                    case '5' :
                    case '6' :     
                    case '7' :     
                    case '8' :     
                    case '9' : {
                        //char before number must be a number, ' ', '{', '[', '(' or '.' to exclude variable names
                        switch (last){
                            case ' ' :
                            case '{' :
                            case '[' :    
                            case '(' :    
                            case '.' : 
                            case '0' : 
                            case '1' : 
                            case '2' : 
                            case '3' : 
                            case '4' : 
                            case '5' :
                            case '6' :     
                            case '7' :     
                            case '8' :     
                            case '9' : {
                                //Set font of numbers to blue
                                ret += "<font color=\"blue\">" + s.charAt(0) + "</font>";
                                last = s.charAt(0);
                            } break;
                            default : {
                                ret += s.charAt(0);
                                last = s.charAt(0);
                            }
                        }
                    } break;
                    default : {
                        ret += s.charAt(0);
                        last = s.charAt(0);
                    }
                }
            }
            
            //If first char in string in a number, it will be colored
            else{
                switch(s.charAt(0)){
                    //Check to see if first char in number.
                    case '0' : 
                    case '1' : 
                    case '2' : 
                    case '3' : 
                    case '4' : 
                    case '5' :
                    case '6' :     
                    case '7' :     
                    case '8' :     
                    case '9' : {
                        ret += "<font color=\"blue\">" + s.charAt(0) + "</font>";
                        last = s.charAt(0);
                    } break;
                    default : {
                        ret += s.charAt(0);
                        last = s.charAt(0);
                    }
                 }
            }
            
            s = s.substring(1);
        }
        
        return ret;
    }
    
    private String colorComment(String s){
        String ret = "";
        
        //Single line comment
        if (s.contains("//")){
            ret = s.substring(0, s.indexOf("//")) + "<font color=\"darkgray\">" + 
                    s.substring(s.indexOf("//")) + "</font>";
        }
        
        //No comments
        else ret = s;
        return ret;
    }
    
    private void printChoices(Question q){
        //Multiple answers. Checkbox display
        if (q.getAnswerKey().length() > 1){
            String[] s = {q.getChoiceA(), q.getChoiceB(), q.getChoiceC(), q.getChoiceD(), q.getChoiceE()};
            char c = 'A';
            for (int i = 0; i < s.length; i++){
                //If choice is not null, print the choice
                if (!s[i].equals("null") && !s[i].equals(null)){
                    s[i] = replaceTags(s[i]);
                    body = body + "<input type=\"checkbox\" name=\"choices\" value=\"" + i +"\">"
                            + "<font face= \"courier new\" color=\"darkred\">" + c + ". </font>&nbsp;" + s[i] + "<br />";
                    c++;
                }
            }
        }
        
        //Single answer. Radio display
        if (q.getAnswerKey().length() == 1){
            String[] s = {q.getChoiceA(), q.getChoiceB(), q.getChoiceC(), q.getChoiceD(), q.getChoiceE()};
            char c = 'A';
            for (int i = 0; i < s.length; i++){
                //If choice is not null, print the choice
                if (!s[i].equals("null") && !s[i].equals(null)){
                    s[i] = replaceTags(s[i]);
                    
                    body = body + "<input type=\"radio\" name=\"choices\" value=\"" + i +"\">"
                        + "<font face =\"courier new\" color=\"darkred\">" + c + ". </font>&nbsp;" + s[i] + "<br />";
                    c++;
                }
            }
        }
        
    }
    
    private Answer updateAnswer(Question q){
        Answer a = db.retrieveAnswer(chapterNo, questionNo);
            a.setAnswerA(0);
            a.setAnswerB(0);
            a.setAnswerC(0);
            a.setAnswerD(0);
            a.setAnswerE(0);
                
            //Set user answers
            for (String s : checked){
                switch (s){
                    case "0" : a.setAnswerA(1); break;
                    case "1" : a.setAnswerB(1); break;
                    case "2" : a.setAnswerC(1); break;
                    case "3" : a.setAnswerD(1); break;
                    case "4" : a.setAnswerE(1); break;
                }
            }                   
                    
            //Check correctness
            String key = q.getAnswerKey();
            String answers = "";
            boolean correct = true;
            for (String s : checked){
                answers += s;
            }
               
            //Different lengths means user answer is incorrect
            if (key.length() != answers.length()) correct = false;
            else {
                if (key.contains("a") && !answers.contains("0")) correct = false;
                if (key.contains("b") && !answers.contains("1")) correct = false;
                if (key.contains("c") && !answers.contains("2")) correct = false;
                if (key.contains("d") && !answers.contains("3")) correct = false;
                if (key.contains("e") && !answers.contains("4")) correct = false;
            }
                
            if (correct) a.setIsCorrect(1);
            else a.setIsCorrect(0);
                  
            db.updateAnswer(a);
            return a;
    }
    
    private void printJS(Question q){
        body = body + "<br /><script>"
                + "function displayAnswer(){ "
                + "document.getElementById('answer').innerHTML = 'The correct answer is " + q.getAnswerKey().toUpperCase() + "'; "
                + "document.getElementById('hint').innerHTML = 'Explaination: " + q.getHint() + "'; "
                + "}</script>";
        
        body = body + "<br /><script>"
                + "function displayHint(){ "
                + "document.getElementById('hint').innerHTML = 'Explaination: " + q.getHint() + "'; "
                + "}</script>";
    }
    
    private String replaceTags(String s){
        if (s.contains("<") || s.contains(">")){
            String temp = "";
            String t = s;
            for (int j = 0; j < t.length(); j++){
                
                if (t.charAt(j) == '<'){
                    temp = t.substring(0, j) + "&lt";
                    try {
                        temp += t.substring(j + 1);
                    } catch (IndexOutOfBoundsException e){
                        // < is at the end of the string.
                    }
                    
                    if (!t.isEmpty()) t = temp;
                }
                            
                if (t.charAt(j) == '>'){
                    temp = t.substring(0, j) + "&gt";
                    try {
                        temp += t.substring(j + 1);
                    } catch (IndexOutOfBoundsException e){
                        // > is at the end of the string.
                    }
                }
                
                if (!temp.isEmpty()) t = temp;
            }
            s = temp;
        }
        
        return s;
    }
    
    /* Getters and Setters */
    public int getChapterNo(){
        return chapterNo;
    }
    
    public void setChapterNo(int i){
        chapterNo = i;
    }
    
    public int getQuestionNo(){
        return questionNo;
    }
    
    public void setQuestionNo(int i){
        questionNo = i;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String i){
        title = i;
    }
    
    public String getBody(){
        return body;
    }
    
    public void setBody(String i){
        body = i;
    }
    
    public String getMode(){
        return mode;
    }
    
    public void setMode(String i){
        mode = i;
    }
    
    public String getSelected(){
        return selected;
    }
    
    public void setSelected(String i){
        selected = i;
    }
    
    public String[] getChecked(){
        return checked;
    }
    
    public void setChecked(String[] i){
        checked = i;
    }
}
