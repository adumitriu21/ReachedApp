package com.example.reachedapp.Models;


import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentTeacherMessage that = (ParentTeacherMessage) o;
        return Objects.equals(MessageId, that.MessageId) && Objects.equals(MessageText, that.MessageText) && Objects.equals(Parent, that.Parent) && Objects.equals(Teacher, that.Teacher) && Objects.equals(MassegeDate, that.MassegeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MessageId, MessageText, Parent, Teacher, MassegeDate);
    }

    @Override
    public String toString() {
        return "ParentTeacherMessage{" +
                "MessageId='" + MessageId + '\'' +
                ", MessageText='" + MessageText + '\'' +
                ", Parent='" + Parent + '\'' +
                ", Teacher='" + Teacher + '\'' +
                ", MassegeDate=" + MassegeDate +
                '}';
    }
}
