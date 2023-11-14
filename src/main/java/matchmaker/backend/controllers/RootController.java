package matchmaker.backend.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import matchmaker.backend.FirebaseService;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.Permission;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.PermissionRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;

@RestController
public class RootController {

    @Autowired
    private FirebaseAuth firebaseAuth;

    @GetMapping("/ping")
    public HashMap<String, String> ping() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ping", "pong");
        return map;
    }

    @GetMapping(path = "/whoami")
    public HashMap<String, String> test(
            @RequestAttribute(name = "loggedInUser", required = false) User currentUser) throws FirebaseAuthException {
        HashMap<String,String> map = new HashMap<>();

        if(currentUser == null){
            map.put("account", "no");
            return map;
        }
        map.put("firebase uid", firebaseAuth.getUser(currentUser.firebaseId).getUid());
        map.put("firebase email", firebaseAuth.getUser(currentUser.firebaseId).getEmail());
        return map;

    }


}