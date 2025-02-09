package com.bitespeed.IR.Service;

import com.bitespeed.IR.model.Contact;
import com.bitespeed.IR.ContactRepository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public Map<String, Object> identifyCustomer(String email, String phoneNumber) {
        // to find existing contacts by email or phone number
        List<Contact> existingContacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        Contact primaryContact = existingContacts.stream()
                .filter(c -> "primary".equals(c.getLinkPrecedence()))
                .findFirst()
                .orElse(null);

        // if no contact exists, create a new primary contact
        if (primaryContact == null) {
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");
            newContact.setCreatedAt(LocalDateTime.now());
            newContact.setUpdatedAt(LocalDateTime.now());

            newContact = contactRepository.save(newContact);

            return buildResponse(newContact,
                    new ArrayList<>(List.of(email)),
                    new ArrayList<>(List.of(phoneNumber)),
                    new ArrayList<>());
        }

        // to retrieve all contacts linked to this primary contact
        List<Contact> allContacts = contactRepository.findByLinkedIdOrId(primaryContact.getId(), primaryContact.getId());

        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<Long> secondaryIds = new ArrayList<>();

        for (Contact c : allContacts) {
            if (c.getEmail() != null && !emails.contains(c.getEmail())) emails.add(c.getEmail());
            if (c.getPhoneNumber() != null && !phoneNumbers.contains(c.getPhoneNumber())) phoneNumbers.add(c.getPhoneNumber());
            if ("secondary".equals(c.getLinkPrecedence())) secondaryIds.add(c.getId());
        }

        // Ensure the new request data is linked if needed
        boolean isNewInfo = allContacts.stream()
                .noneMatch(c -> Objects.equals(c.getEmail(), email) && Objects.equals(c.getPhoneNumber(), phoneNumber));

        if (isNewInfo) {
            Contact newSecondary = new Contact();
            newSecondary.setEmail(email);
            newSecondary.setPhoneNumber(phoneNumber);
            newSecondary.setLinkedId(primaryContact.getId());
            newSecondary.setLinkPrecedence("secondary");
            newSecondary.setCreatedAt(LocalDateTime.now());
            newSecondary.setUpdatedAt(LocalDateTime.now());

            newSecondary = contactRepository.save(newSecondary);
            secondaryIds.add(newSecondary.getId());
        }

        return buildResponse(primaryContact, emails, phoneNumbers, secondaryIds);
    }

    private Map<String, Object> buildResponse(Contact primary, List<String> emails, List<String> phones, List<Long> secondaryIds) {
        Map<String, Object> response = new HashMap<>();
        response.put("primaryContactId", primary.getId());

        // to ensure the primary contact's email and phoneNumber are always included
        if (primary.getEmail() != null && !emails.contains(primary.getEmail())) {
            emails.add(0, primary.getEmail());  // to add primary email as first element
        }
        if (primary.getPhoneNumber() != null && !phones.contains(primary.getPhoneNumber())) {
            phones.add(0, primary.getPhoneNumber());  // to add primary phone number as first element
        }

        response.put("emails", emails);
        response.put("phoneNumbers", phones);
        response.put("secondaryContactIds", secondaryIds);

        return Collections.singletonMap("contact", response);
    }
}
