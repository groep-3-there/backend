package matchmaker.backend.UnitTests;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageTest {

  @Test
  void GetId() {
    Image testImage = new Image();
    testImage.id = 1L;
    assertEquals(testImage.getId(), 1L);
  }

  @Test
  void GetPhotoData() {
    Image testImage = new Image();
    byte[] testByte = new byte[1];
    testImage.photoData = testByte;
    assertEquals(testImage.getPhotoData(), testByte);
  }

  @Test
  void GetAuthor() {
    Image testImage = new Image();
    User testUser = new User("testUser");
    testImage.author = testUser;
    assertEquals(testImage.getAuthor(), testUser);
  }

  @Test
  void GetAttachmentForChallenge() {
    Image testImage = new Image();
    Challenge testChallenge = new Challenge();
    testImage.attachmentForChallenge = testChallenge;
    assertEquals(testImage.getAttachmentForChallenge(), testChallenge);
  }

  @Test
  void SetId() {
    Image testImage = new Image();
    testImage.setId(1L);
    assertEquals(testImage.id, 1L);
  }

  @Test
  void SetPhotoData() {
    Image testImage = new Image();
    byte[] testByte = new byte[1];
    testImage.setPhotoData(testByte);
    assertEquals(testImage.photoData, testByte);
  }

  @Test
  void SetAuthor() {
    Image testImage = new Image();
    User testUser = new User("testUser");
    testImage.setAuthor(testUser);
    assertEquals(testImage.author, testUser);
  }

  @Test
  void SetAttachmentForChallenge() {
    Image testImage = new Image();
    Challenge testChallenge = new Challenge();
    testImage.setAttachmentForChallenge(testChallenge);
    assertEquals(testImage.attachmentForChallenge, testChallenge);
  }
}
