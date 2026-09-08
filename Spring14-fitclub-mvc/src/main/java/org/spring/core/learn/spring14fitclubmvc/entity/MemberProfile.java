package org.spring.core.learn.spring14fitclubmvc.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;



/*
 * ================= RELATIONSHIP 1 (continued): @OneToOne OWNING SIDE, BIDIRECTIONAL =================
 * @JoinColumn(name = "member_id", unique = true) is what makes THIS side
 * the owner - the member_profiles table gets the actual foreign key
 * column. "unique = true" is the critical part: it's what enforces a true
 * ONE-to-one (at most one profile per member) instead of secretly
 * allowing many profiles to point at the same member, which would make
 * this behave like a many-to-one despite the annotation name.
 */


@Entity
@Table(name = "members_profile")
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String bio;


    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 200)
    private String address;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique= true)
    @JsonIgnore  // prevents profile -> member -> profile ... infinite JSON loop
    private Member member;


    public MemberProfile(Long id, String bio, String phoneNumber, String address, Member member) {
        this.id = id;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.member = member;
    }


    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getAddress() {
        return address;
    }

    public Member getMember() {
        return member;
    }
}
