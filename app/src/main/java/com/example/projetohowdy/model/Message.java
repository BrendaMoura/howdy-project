package com.example.projetohowdy.model;

import java.util.Date;

public class Message {
    public String idMessage;
    public String idInbox;
    public String idSender;
    public String content;
    public Date createdAt;
    public Date updatedAt;
    public boolean deletedBySender;
    public boolean deletedByReceiver;

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getIdInbox() {
        return idInbox;
    }

    public void setIdInbox(String idInbox) {
        this.idInbox = idInbox;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeletedBySender() {
        return deletedBySender;
    }

    public void setDeletedBySender(boolean deletedBySender) {
        this.deletedBySender = deletedBySender;
    }

    public boolean isDeletedByReceiver() {
        return deletedByReceiver;
    }

    public void setDeletedByReceiver(boolean deletedByReceiver) {
        this.deletedByReceiver = deletedByReceiver;
    }

    public Message(String idMessage, String idInbox, String idSender, String content, Date createdAt, Date updatedAt, boolean deletedBySender, boolean deletedByReceiver) {
        this.idMessage = idMessage;
        this.idInbox = idInbox;
        this.idSender = idSender;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedBySender = deletedBySender;
        this.deletedByReceiver = deletedByReceiver;
    }

    public Message() {
    }
}
