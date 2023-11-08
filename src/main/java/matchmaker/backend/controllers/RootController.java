package matchmaker.backend.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
public class RootController {

    @GetMapping("/ping")
    public HashMap<String, String> ping() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ping", "pong");
        return map;
    }
}