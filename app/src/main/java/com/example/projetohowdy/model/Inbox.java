package com.example.projetohowdy.model;

public class Inbox {
    public String idInbox;
    public String idLastMessage;

    public String idInitiator;
    public String seenAtByInitiator;
    public Long unSeenQuantByInitiator;
    public boolean deletedByInitiator;

    public String idReceiver;
    public String seenAtByReceiver;
    public Long unSeenQuantByReceiver;
    public boolean deletedByReceiver;
}
