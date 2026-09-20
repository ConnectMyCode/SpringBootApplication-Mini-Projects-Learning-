package org.spring.core.learn.spring14fitclubmvc.dto.response;


import org.spring.core.learn.spring14fitclubmvc.entity.Member;

import java.time.LocalDateTime;


/*
 * ================= WHY A RESPONSE DTO INSTEAD OF RETURNING THE ENTITY =================
 * Returning the Member entity directly from a @RestController is a
 * common REST API mistake: it leaks internal structure (password field,
 * JPA lazy-loading proxies) into the public API, couples your DB schema
 * to your API contract, and lazy relationships can throw
 * LazyInitializationException when Jackson tries to serialize them
 * outside a transaction. A dedicated response DTO with a `from(entity)`
 * factory method keeps the API contract intentional.
 */

import java.time.LocalDateTime;

public class MemberResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final LocalDateTime registeredOn;


    public MemberResponse(Long id, String username, String email, LocalDateTime registeredOn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.registeredOn = registeredOn;
    }

    public static MemberResponse from(Member member){
        return new MemberResponse(member.getId(), member.getUsername(), member.getEmail(),member.getRegisteredOn());
        // Notice: password is NEVER read here - it physically cannot end
        // up in the JSON response, since this DTO doesn't have a field for it.
    }


    public Long getId() { return id; }

    public String getUsername() {  return username;  }

    public String getEmail() { return email;    }

    public LocalDateTime getRegisteredOn() {  return registeredOn; }

}


