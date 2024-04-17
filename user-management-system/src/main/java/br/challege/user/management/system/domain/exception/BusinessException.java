package br.challege.user.management.system.domain.exception;

public class BusinessException extends  RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}