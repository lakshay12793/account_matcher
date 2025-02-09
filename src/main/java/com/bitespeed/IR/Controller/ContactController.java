package com.bitespeed.IR.Controller;
import com.bitespeed.IR.Service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/identify")
    public ResponseEntity<Map<String, Object>> identify(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String phoneNumber = request.get("phoneNumber");

        if (email == null && phoneNumber == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email or phoneNumber is required"));
        }

        return ResponseEntity.ok(contactService.identifyCustomer(email, phoneNumber));
    }
}
