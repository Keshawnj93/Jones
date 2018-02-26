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
        if (q.getAnswerKey() == null){
            body = "<font color=\"red\"> The requested question could not be found <font/>";
        } else {
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
                    
                    //Correct answer. Inform user
                    if (a.getIsCorrect() == 1){
                        body = body + "<br /><font color=\"green\"><p>Your answer is correct</p></font>";
                    }
                    
                    //Incorrect answer. Inform user
                    if (a.getIsCorrect() == 0){
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
                        
                        body = body + "<br /><font color=\"red\"><p>Your answer " + answers + " is incorrect</p></font>";
                        body = body + "<font color=\"green\"><p>Click here to show the correct answer</p></font>";
                    }
                }
                } catch (Exception e){
                    //No answer selected. Notify user
                    body = body + "<br /><p>You didn't answer this question</p>";
                    body = body + "<font color=\"green\"><p>Click here to show the correct answer</p></font>";
                }
                
                //Print Get Statistics button. Not yet functional.
                body = body + "<input type=\"button\" value=\"Get Statistics\" style=\"background-color : green\">";
            }
            
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
                    body = body + "<input type=\"checkbox\" name=\"choices\" value=\"" + i +"\"> " + s[i] + "<br />";
                }
            }
        }
        
        //Single answer. Radio display
        if (q.getAnswerKey().length() == 1){
            String[] s = {q.getChoiceA(), q.getChoiceB(), q.getChoiceC(), q.getChoiceD(), q.getChoiceE()};
            for (int i = 0; i < s.length; i++){
                //If choice is not null, print the choice
                if (!s[i].equals("null") && !s[i].equals(null)){
                    body = body + "<input type=\"radio\" name=\"choices\" value=\"" + i +"\"> " + s[i] + "<br />";
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
