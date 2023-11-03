package matchmaker.backend.controllers;

import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class ImageController {


    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
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

    public Image upload(MultipartFile file, User author){
        Image img = null;
        try {
            img = new Image(file.getBytes());
            return imageRepository.save(img);
        } catch (IOException e) {
            log.warn("Image upload failed");
        }
        return null;
    }

    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Image uploadImage(@RequestParam("image") MultipartFile multipartFile, @RequestAttribute("loggedInUser") User currentUser) {
        return upload(multipartFile, currentUser);
    }

    @GetMapping(value="/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image img = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.photoData);
    }



}
