package com.example.pruebaappredsocial;

public class FamilyRequest {
    private int userId;
    private int familyMemberId;
    private String relation;

    // Constructor, getters y setters
    public FamilyRequest(int userId, int familyMemberId, String relation) {
        this.userId = userId;
        this.familyMemberId = familyMemberId;
        this.relation = relation;
    }

    public int getUserId() {
        return userId;
    }

    public int getFamilyMemberId() {
        return familyMemberId;
    }

    public String getRelation() {
        return relation;
    }
}

