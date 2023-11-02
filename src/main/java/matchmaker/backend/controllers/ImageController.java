package matchmaker.backend.controllers;

import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/image/user/{id}")
    public String getBase64ImageByUserId(@PathVariable("id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return null;
        }

        Optional<Image> profilePicture = imageRepository.findById(user.get().getAvatarImageId());
        if (!profilePicture.isPresent()) {
            return null;
        }

        byte[] byteaImage = profilePicture.get().photoData;
        return java.util.Base64.getEncoder().encodeToString(byteaImage);
    }
}
