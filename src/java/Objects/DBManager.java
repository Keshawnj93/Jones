package Objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBManager {

    private String chapter, question, text, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint, message;
    Statement smnt;
    PreparedStatement psmt;
    Connection conn = null;

    public DBManager() {
        chapter = question = text = choiceA = choiceB = choiceC = choiceD = choiceE = answerKey = hint = message = null;
        initDB();
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
                    + "time timestamp default current_timestamp,"
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

    public void insert(Question q) {
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
                    System.out.println("Chapter " + q.getChapter() + " question" + q.getQuestion() + "is already in use");
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
        }
    }
}
