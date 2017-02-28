package com.papaya.scotthanberg.papaya;

import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class Class {
    private List<Student> listOfStudents;
    private Professor Prof;
    private List<StudySession> pastStudySessions;
    private List<StudySession> activeStudySessions;
    private String classID;

    public List<Student> getListOfStudents() {
        return listOfStudents;
    }

    public Professor getProf() {
        return Prof;
    }

    public List<StudySession> getPastStudySessions() {
        return pastStudySessions;
    }

    public List<StudySession> getActiveStudySessions() {
        return activeStudySessions;
    }

    public String getClassID() {
        return classID;
    }


}
