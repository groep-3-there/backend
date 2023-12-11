package matchmaker.backend;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    MailjetClient client;
    public EmailService(){

    }

    public void sendEmail(String targetEmail, String targetName, String title, String subtitle, String buttonText, String buttonClickUrl) throws MailjetException {
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
