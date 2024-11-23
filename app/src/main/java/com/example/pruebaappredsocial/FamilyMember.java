package com.example.pruebaappredsocial;

public class FamilyMember {
    private String emailUser;
    private String emailFamily;
    private String relation;

    public FamilyMember(String emailUser, String emailFamily, String relation) {
        this.emailUser = emailUser;
        this.emailFamily = emailFamily;
        this.relation = relation;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailFamily() {
        return emailFamily;
    }

    public void setEmailFamily(String emailFamily) {
        this.emailFamily = emailFamily;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}

