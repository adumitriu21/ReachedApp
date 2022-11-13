package com.example.reachedapp.Models;


import java.util.Date;

public class ParentTeacherMessage {
    private String MessageId;
    private String MessageText;
    private String Parent;
    private String Teacher;
    private Date MassegeDate;

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public Date getMassegeDate() {
        return MassegeDate;
    }

    public void setMassegeDate(Date massegeDate) {
        MassegeDate = massegeDate;
    }
}
