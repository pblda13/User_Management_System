package br.challege.user.management.system.domain.exception;

public class UnauthorizedException extends  RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}