package matchmaker.backend.RSS;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matchmaker.backend.constants.ChallengeVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import java.util.*;

import matchmaker.backend.repositories.ChallengeRepository;

@Component
public class RssFeedView extends AbstractRssFeedView {

    @Autowired
    private ChallengeRepository challengeRepository;
   @Override
   protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
       feed.setTitle("MatchMaker RSS Feed");
       feed.setDescription("MatchMakers public challenges");
       feed.setLink("http://www.matchmakergroep3.nl");
   }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Item> items = new ArrayList<>();
        challengeRepository.findChallengesByVisibilityIs(ChallengeVisibility.PUBLIC).forEach(challenge -> {
            Item item = new Item();
            item.setTitle(challenge.getTitle());
            item.setLink("http://matchmakergroep3.nl/challenge/" + challenge.getId());
            item.setPubDate(Date.from(challenge.getCreatedAt().toInstant()));
            item.setAuthor(challenge.getAuthor().getName());
            Description description = new Description();
            description.setValue(challenge.getSummary());
            item.setDescription(description);
            items.add(item);
        });
        return items;
    }
}
