package com.bitespeed.IR.ContactRepository;
import com.bitespeed.IR.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
    List<Contact> findByLinkedIdOrId(Long linkedId, Long id);
}
