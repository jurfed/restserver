package com.octavianonline.servlet;

import java.io.Serializable;

public class Parameters  implements Serializable {

    private Long credit;
    private Double sound;
    private String language;

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public double getSound() {
        return sound;
    }

    public void setSound(double sound) {
        this.sound = sound;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Parameters(Long credit, double sound, String language) {
        this.credit = credit;
        this.sound = sound;
        this.language = language;
    }
}
