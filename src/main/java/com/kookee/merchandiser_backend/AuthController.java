package com.kookee.merchandiser_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
    "https://kookee-merchandiser-app.netlify.app", // production frontend
    "http://localhost:3000"                         // local dev frontend
})
public class AuthController {

    @Autowired
    private MerchandiserRepository merchRepo;

    @PostMapping("/login")
    public boolean login(@RequestBody Merchandiser request) {
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password: " + request.getPassword());

        Merchandiser merch = merchRepo.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (merch != null) {
            System.out.println("✅ Found match in DB");
            return true;
        } else {
            System.out.println("❌ No match in DB");
            return false;
        }
    }
}
