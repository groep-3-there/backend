package matchmaker.backend;


import java.io.IOException;
import java.io.InputStream;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseService {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount = this.getClass().getResourceAsStream("/sfirebase_config.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://your-database-url.firebaseio.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseAuth firebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(firebaseApp());
    }


}
