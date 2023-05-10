package it.cgmconsulting.myblog.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Mail {

    private String mailFrom;
    private String mail;
    private String mailTo;
    private String mailSubject;
    private String mailContent;

    public Mail(String mailFrom, String email, String s, String s1) {
    }
}
