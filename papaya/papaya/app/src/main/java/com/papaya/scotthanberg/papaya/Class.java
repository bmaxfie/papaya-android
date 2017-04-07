package com.papaya.scotthanberg.papaya;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class Class implements Serializable {

    //private List<Student> listOfStudents;
    //private Professor professor;
    //private List<StudySession> pastStudySessions;


    private List<StudySession> activeStudySessions;
    private String classID;
    private String className;
    private String description;
    private int role;

    public Class(String classID, String className, String description, List<StudySession> sessions, int role) {
        this.classID = classID;
        this.className = className;
        this.description = description;
        this.role = role;
        this.activeStudySessions = sessions;
    }


    /*
        GETTERS:
     */
    public String getClassID() {
        return classID;
    }

    public String getClassName() { return className; }

    public String getDescription() { return description; }

    public int getRole() {return role; }

    public List<StudySession> getActiveStudySessions() {
        return activeStudySessions;
    }

    /*
        SETTERS:
     */
    public void setActiveStudySessions(List<StudySession> newSessions) {
        activeStudySessions.clear();
        // Set references to this Class so that
        for (StudySession session : newSessions)
            session.setClassObject(this);
        activeStudySessions = newSessions;
    }


    /*public List<Student> getListOfStudents() {
        return listOfStudents;
    }

    public Professor getProf() {
        return professor;
    }

    public List<StudySession> getPastStudySessions() {
        return pastStudySessions;
    }
    */


}
