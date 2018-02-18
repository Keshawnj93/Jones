package Tests;

import Objects.*;
import Beans.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestClass1 {

    public static void main(String[] args) {
        
        DBManager db = new DBManager();
        Question q = db.retrieve(8, 30);
        q.print();
//        DBManager db = new DBManager();
//        
//        
//
//        List<String> results = new ArrayList<String>();
//
//        File[] files = new File("c:\\selftest\\selftest11e\\").listFiles();
//        //If this pathname does not denote a directory, then listFiles() returns null. 
//
//        for (File file : files) {
//            if (file.isFile()) {
//                results.add(file.getName());
//                
//                QuestionParser q = new QuestionParser(file.getName());
//                ArrayList<Question> e = q.allQuestions();
//                for(Question r : e){
//                    db.insert(r);
//                }
//            }
//        }

//
//        DBManager db = new DBManager();
//
//        db.insert(e.get(0));
    }
}
