package matchmaker.backend.RSS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController
public class RssFeedController {

  @Autowired private View rssFeedView;

  @GetMapping("/rss")
  public View getRssFeed() {
    return rssFeedView;
  }
}
