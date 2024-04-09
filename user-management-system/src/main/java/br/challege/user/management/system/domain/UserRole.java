package br.challege.user.management.system.domain;

public enum UserRole {

    DMIN ("admin"),

    USER ("user");

    private String role;


    UserRole(String role) {
        this.role = role;
    }
}
