package net.bifewagyu.authenticator.globalhandler;

public class EmailCadastradoExeption extends RuntimeException {
    public EmailCadastradoExeption(String email) {
        super("Email Ja cadastrado" + email);
    }
}
