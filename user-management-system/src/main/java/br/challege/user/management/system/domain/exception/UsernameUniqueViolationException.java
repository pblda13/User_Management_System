package br.challege.user.management.system.domain.exception;

import lombok.Getter;

@Getter
public class UsernameUniqueViolationException extends RuntimeException {
    private String username;

    public UsernameUniqueViolationException(String username) {
        this.username = username;
    }
}
