package it.cgmconsulting.myblog.mail;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {



    public MessageResponse sendMail(Mail mail) {
        //MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(mailgunApiyKey).createApi(MailgunMessagesApi.class);
        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config("ab0f3a30afa807fb1217c416460e58c7-2cc48b29-a282cf57").createApi(MailgunMessagesApi.class);

        Message message = Message.builder()
                .from(mail.getMailFrom())
                .to(mail.getMailTo())
                .subject(mail.getMailSubject())
                .text(mail.getMailContent())
                .build();

        //return mailgunMessagesApi.sendMessage(mailgunDomain, message);
        return mailgunMessagesApi.sendMessage("sandbox11474c3719f2483e8edc29a6f598754a.mailgun.org", message);
    }


}
