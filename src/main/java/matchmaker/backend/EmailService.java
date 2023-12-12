package matchmaker.backend;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import matchmaker.backend.models.Challenge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Service
public class EmailService {

    MailjetClient client;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private Environment environment;

    public EmailService(){

    }

    public void sendEmail(String targetEmail, String targetName, String title, String subtitle, String buttonText, String buttonClickUrl) throws MailjetException {
        if (Objects.equals(environment.getProperty("spring.profiles.active"), "test")){
            log.warn("EmailService.sendEmail() called in test environment, not sending email");
            return;
        }
        ClientOptions options = ClientOptions.builder()
                .apiKey("7dcdd090e3db1ec214d8e7c71d82006e")
                .apiSecretKey("f66b5514baaf89d83588fde64717d4e4")
                .build();

        this.client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(targetEmail, targetName))
                .from(new SendContact("matchmakergroep3@gmail.com", "Matchmaker"))
                .templateID(5427584L)
                .templateLanguage(true)
                .variables(Map.of("title", title, "subtitle", subtitle, "ctoText", buttonText, "ctoUrl", buttonClickUrl))
                .trackOpens(TrackOpens.ENABLED)
//                .attachment(Attachment.fromFile(attachmentPath))
                .header("test-header-key", "test-value")
                .customID("custom-id-value")
                .build();
        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1) // you can add up to 50 messages per request
                .build();

        // act
        SendEmailsResponse response = request.sendWith(this.client);
        System.out.println(response.toString());
    }
}
