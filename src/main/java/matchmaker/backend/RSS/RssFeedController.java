package matchmaker.backend.RSS;

import matchmaker.backend.constants.ChallengeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Date;

@RestController
public class RssFeedController {

  @Autowired private RssFeedView rssFeedView;

  @GetMapping("/rss")
  public View getRssFeed() {
      //Filter the rss feed by author
      return rssFeedView;
  }

  @GetMapping("/rss/filter")
    public View getRssFeedFiltered(
            @RequestParam(value = "status", required = false) ChallengeStatus status,
            @RequestParam(value = "created", required = false) @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate created
  ) {
        rssFeedView.setStatus(status);
        rssFeedView.setCreated(created);
        return rssFeedView;
    }
}
