package com.example.projetohowdy.model;

import java.util.Map;

public class Inbox {
    public String idInbox;
    public String idLastMessage;
    public Map<String, Participants> participants;

    public String getIdInbox() {
        return idInbox;
    }

    public void setIdInbox(String idInbox) {
        this.idInbox = idInbox;
    }

    public String getIdLastMessage() {
        return idLastMessage;
    }

    public void setIdLastMessage(String idLastMessage) {
        this.idLastMessage = idLastMessage;
    }

    public Map<String, Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Participants> participants) {
        this.participants = participants;
    }

    public Inbox(String idInbox, String idLastMessage, Map<String, Participants> participants) {
        this.idInbox = idInbox;
        this.idLastMessage = idLastMessage;
        this.participants = participants;
    }

    public Inbox() {
    }
}
