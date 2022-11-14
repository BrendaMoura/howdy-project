package com.example.projetohowdy.model;

public class Participants {
    public String idParticipant;
    public String seenAt;
    public Long unSeenQuant;
    public boolean deleted;

    public String getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(String idParticipant) {
        this.idParticipant = idParticipant;
    }

    public String getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(String seenAt) {
        this.seenAt = seenAt;
    }

    public Long getUnSeenQuant() {
        return unSeenQuant;
    }

    public void setUnSeenQuant(Long unSeenQuant) {
        this.unSeenQuant = unSeenQuant;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Participants(String idParticipant, Long unSeenQuant, boolean deleted) {
        this.idParticipant = idParticipant;
        this.unSeenQuant = unSeenQuant;
        this.deleted = deleted;
    }

    public Participants() {
    }
}
