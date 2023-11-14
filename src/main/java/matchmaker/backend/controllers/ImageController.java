package matchmaker.backend.controllers;

import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {


    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ChallengeRepository challengeRepository;



    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Image uploadImage(@RequestParam("image") MultipartFile multipartFile, @RequestAttribute(name="loggedInUser", required = false) User currentUser, @RequestParam(name = "imgData", required = false, defaultValue = "0") Boolean withImageData) {
        if(currentUser == null){
            log.warn("Uploading image failed, no user logged in");
            return null;
        }

        Image img = null;
        try {
            img = new Image(multipartFile.getBytes());
            img.author = currentUser;
        } catch (IOException e) {
            log.warn("Uploading image failed"  +e.toString());
            return null;
        }
        Image m = imageRepository.save(img);
        if(withImageData){
            return m;
        }
        return m.withoutData();
    }


    @GetMapping(value="/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image img = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.photoData);
    }







}
