package Beans;

import Objects.*;

public class QuestionBean {
    private int chapterNo, questionNo;
    private String title, body;
    DBManager db;
    
    public QuestionBean(){
        chapterNo = 1; questionNo = 1;
        title = "Chapter 1 Question 1";
        body = "";
        db = new DBManager();
        
    }
    
    public void printPage(){
        body = "";
        Question q = db.retrieve(chapterNo, questionNo);
        if (q.getAnswerKey() == null){
            body = "<font color=\"red\"> The requested question could not be found <font/>";
        } else {
            printQuestion(q);
            printChoices(q);
            body = body + "<br /><input type=\"submit\" value=\"Check My Answer\" style=\"background-color : green\">";
        }
    }
    
    private void printQuestion(Question q){
        body = body + q.getText() + "<br /><br />";
    }
    
    private void printChoices(Question q){
        //Multiple answers. Checkbox display
        if (q.getAnswerKey().length() > 1){
            String[] s = {q.getChoiceA(), q.getChoiceB(), q.getChoiceC(), q.getChoiceD(), q.getChoiceE()};
            for (int i = 0; i < s.length; i++){
                //If choice is not null, print the choice
                if (!s[i].equals("null") && !s[i].equals(null)){
                    body = body + "<input type=\"checkbox\" name=\"choices\"> " + s[i] + "<br />";
                }
            }
        }
        
        //Single answer. Radio display
        if (q.getAnswerKey().length() == 1){
            String[] s = {q.getChoiceA(), q.getChoiceB(), q.getChoiceC(), q.getChoiceD(), q.getChoiceE()};
            for (int i = 0; i < s.length; i++){
                //If choice is not null, print the choice
                if (!s[i].equals("null") && !s[i].equals(null)){
                    body = body + "<input type=\"radio\" name=\"choices\"> " + s[i] + "<br />";
                }
            }
        }
        
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
}
