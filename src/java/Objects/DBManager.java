package Objects;

import java.io.File;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private String chapter, question, text, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint, message;
    Statement smnt;
    PreparedStatement psmt;
    Connection conn = null;

    public DBManager() {
        chapter = question = text = choiceA = choiceB = choiceC = choiceD = choiceE = answerKey = hint = message = null;
        initDB();
        //populateDB();
    }

    public void initDB() {
        //Load Driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Error loading jdbc driver");
            System.exit(0);
        }
        System.out.println("Driver Loaded Successfully");

        //Connect to Database
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/javabook", "root", "Flylow13");
        } catch (Exception e) {
            System.out.println("Error connecting to server");
            System.exit(0);
        }
        System.out.println("Database Connected Successfully");

        //Create tables if not already created
        try {
            smnt = conn.createStatement();
            smnt.execute("create table if not exists intro11equiz("
                    + "chapterNo int(11),"
                    + "questionNo int(11),"
                    + "question text,"
                    + "choiceA varchar(1000),"
                    + "choiceB varchar(1000),"
                    + "choiceC varchar(1000),"
                    + "choiceD varchar(1000),"
                    + "choiceE varchar(1000),"
                    + "answerKey varchar(5),"
                    + "hint text"
                    //+ ",CONSTRAINT ChapQues PRIMARY KEY (ChapterNo,questionNo)"
                    + ");");

            smnt.execute("create table if not exists intro11e("
                    + "chapterNo int(11),"
                    + "questionNo int(11),"
                    + "isCorrect bit(1) default 0,"
                    + "time timestamp default current_timestamp on update current_timestamp,"
                    + "hostname varchar(100),"
                    + "answerA bit(1) default 0,"
                    + "answerB bit(1) default 0,"
                    + "answerC bit(1) default 0,"
                    + "answerD bit(1) default 0,"
                    + "answerE bit(1) default 0,"
                    + "username varchar(100)"
                    + ");");
        } catch (Exception e) {
            System.out.println("Error in executing statement");
            System.exit(0);
        }

    }
    
    public void populateDB(){
        List<String> results = new ArrayList<String>();

        File[] files = new File("c:\\selftest\\selftest11e\\").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
                
                QuestionParser q = new QuestionParser(file.getName());
                ArrayList<Question> e = q.allQuestions();
                for(Question r : e){
                    insert(r);
                }
            }
        }
    }

    public void insert(Question q) {
        
        //1st insert question into intro11equiz
        //Chapter 0 or question 0 are invalid
        if (q.getChapter() == 0 || q.getQuestion() == 0) {
            System.out.println("Error: invalid question");
        } else {
            String st = "Insert into intro11equiz (chapterNo, questionNo, question, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            smnt = null;
            ResultSet res = null;

            try {
                psmt = conn.prepareCall(st);
            } catch (Exception e) {
                message = "Error in preparing statement";
            }

            try {
                //Test whether the ID entered is already in use
                smnt = conn.createStatement();
                res = smnt.executeQuery("Select * from intro11equiz where chapterNo = \"" + q.getChapter() + "\" and questionNo = \"" + q.getQuestion() + "\";");
                if (res.isBeforeFirst()) {
                    System.out.println("Chapter " + q.getChapter() + " question" + q.getQuestion() + "is already in use in intro11equiz");
                } else {
                    //Insert new row into table)
                    psmt.setString(1, "" + q.getChapter());
                    psmt.setString(2, "" + q.getQuestion());
                    psmt.setString(3, "" + q.getText());
                    psmt.setString(4, "" + q.getChoiceA());
                    psmt.setString(5, "" + q.getChoiceB());
                    psmt.setString(6, "" + q.getChoiceC());
                    psmt.setString(7, "" + q.getChoiceD());
                    psmt.setString(8, "" + q.getChoiceE());
                    psmt.setString(9, "" + q.getAnswerKey());
                    psmt.setString(10, "" + q.getHint());
                    psmt.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = "Sorry, an error has occured in execution. Please try again  ";
            }
            
            //After inserting question into intro11equiz, initialize intro11e
            String s = "";
            try {
                InetAddress ip = InetAddress.getLocalHost();
                String hostName = ip.getHostName();
                s = "Insert into intro11e (chapterNo, questionNo, isCorrect, hostname, answerA, answerB, answerC, answerD, answerE)"
                    + " values (?, ?, 0, \'" + hostName + 
                        "\', 0, 0, 0, 0, 0);";
            } catch (Exception e){
                System.out.println("Error in retrieving hostname");
                System.exit(0);
            }
                
                smnt = null;
                res = null;
                
                try {
                    psmt = conn.prepareCall(s);
                } catch (Exception e) {
                    message = "Error in preparing statement";
                }
                
                try {
                //Test whether the ID entered is already in use
                smnt = conn.createStatement();
                res = smnt.executeQuery("Select * from intro11e where chapterNo = \"" + q.getChapter() + "\" and questionNo = \"" + q.getQuestion() + "\";");
                if (res.isBeforeFirst()) {
                    System.out.println("Chapter " + q.getChapter() + " question" + q.getQuestion() + "is already in use in intro11e");
                } else {
                    //Insert new row into table)
                    psmt.setString(1, "" + q.getChapter());
                    psmt.setString(2, "" + q.getQuestion());
                    psmt.execute();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                message = "Sorry, an error has occured in execution. Please try again  ";
            }
        
        }
    }
    
    public Question retrieve(int chapterNo, int questionNo) {
        Question q = new Question();
        
        String st = "Select * from intro11equiz where chapterNo = ? and questionNo = ?";
        
        try{
            psmt = conn.prepareCall(st);
        } catch(Exception e){
            message = "Error in creating prepared statement";
            return q;
        }
        
        ResultSet res = null;
        try{
            psmt.setInt(1, chapterNo);
            psmt.setInt(2, questionNo);
            res = psmt.executeQuery();
            
            if (!res.isBeforeFirst()){
                message = "Chapter " + chapterNo + " Question " + questionNo + "cannot be found";
            } else {
                res.next();
                q.setChapter(res.getInt(1));
                q.setQuestion(res.getInt(2));
                q.setText(res.getString(3));
                q.setChoiceA(res.getString(4));
                q.setChoiceB(res.getString(5));
                q.setChoiceC(res.getString(6));
                q.setChoiceD(res.getString(7));
                q.setChoiceE(res.getString(8));;
                q.setAnswerKey(res.getString(9));
                q.setHint(res.getString(10));
                
                message = "Question found";
            }
        } catch(Exception e){
            message = "Error in retrieval";
            return q;
        }
        
        return q;
    }
    
    public Answer retrieveAnswer(int chapterNo, int questionNo){
        Answer a = new Answer();
        
        String st = "Select * from intro11e where chapterNo = ? and questionNo = ?";
        
        try{
            psmt = conn.prepareCall(st);
        } catch(Exception e){
            message = "Error in creating prepared statement";
            return a;
        }
        
        ResultSet res = null;
        try{
            psmt.setInt(1, chapterNo);
            psmt.setInt(2, questionNo);
            res = psmt.executeQuery();
            
            if (!res.isBeforeFirst()){
                message = "Chapter " + chapterNo + " Question " + questionNo + "cannot be found";
            } else {
                res.next();
                a.setChapterNo(res.getInt(1));
                a.setQuestionNo(res.getInt(2));
                a.setIsCorrect(res.getInt(3));
                a.setTimeStamp(res.getString(4));
                a.setHostName(res.getString(5));
                a.setAnswerA(res.getInt(6));
                a.setAnswerB(res.getInt(7));
                a.setAnswerC(res.getInt(8));
                a.setAnswerD(res.getInt(9));
                a.setAnswerE(res.getInt(10));;
                a.setUserName(res.getString(11));
                
                message = "Answer found";
            }
        } catch(Exception e){
            message = "Error in retrieval";
            return a;
        }
        
        return a;
    }
    
    public void updateAnswer(Answer a){
        String st = "Update intro11e "
                + "set isCorrect = ?, time = current_timestamp(), hostname = ?, answerA = ?, answerB = ?, answerC = ?, answerD = ?, answerE = ? "
                + "where chapterNo = ? and questionNo = ?;";
        Statement stmt = null;
        ResultSet res = null;
        
        try{
            psmt = conn.prepareCall(st);
        } catch (Exception e){
            message = "Error in preparing statement";
        }
        
        try {
            //Test whether the ID entered can be updated
            stmt = conn.createStatement();
            res = stmt.executeQuery("Select * from intro11e where chapterNo = " + a.getChapterNo() + " and questionNo = " + a.getQuestionNo() + ";");

            if (!res.isBeforeFirst()){
                message = "Error: The requested chapter and question could not be found";
                return;
            }
            
            //Update the table
            psmt.setInt(1, a.getIsCorrect());
            psmt.setString(2, a.getHostName());
            psmt.setInt(3, a.getAnswerA());
            psmt.setInt(4, a.getAnswerB());
            psmt.setInt(5, a.getAnswerC());
            psmt.setInt(6, a.getAnswerD());
            psmt.setInt(7, a.getAnswerE());
            psmt.setInt(8, a.getChapterNo());
            psmt.setInt(9, a.getQuestionNo());
            
            psmt.execute();
            
        } catch (Exception e){
            message = "Sorry, an error has occured in execution. Please try again";
        }
    }
}
