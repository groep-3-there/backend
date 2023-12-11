package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.List;

@Table(name = "notifications")
@EnableAutoConfiguration
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "notification_id")
    @TableGenerator(name = "notification_id", initialValue = 1000)
    public Long id;

    public String title;
    public String description;
    public String link;
    public boolean read;

    public Notification() {}

    public Notification(Notification notification) {
        this.title = notification.title;
        this.description = notification.description;
        this.link = notification.link;
        this.read = notification.read;
    }
}
