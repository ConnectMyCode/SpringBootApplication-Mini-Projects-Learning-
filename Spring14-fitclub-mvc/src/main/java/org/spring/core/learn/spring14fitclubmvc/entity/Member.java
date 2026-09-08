package org.spring.core.learn.spring14fitclubmvc.entity;


/*
 * ================= SPRING DATA JPA REVISION =================
 * @Entity        -> tells Hibernate/JPA "this class maps to a database table."
 * @Table(name)    -> explicitly names the table. Worth doing even when it
 *                    would match the class name by default, because some
 *                    class names (Order, User, Group...) collide with SQL
 *                    reserved keywords and silently break schema creation.
 * @Id             -> marks the primary key field.
 * @GeneratedValue -> tells the DATABASE to generate the id (auto-increment
 *                    in MySQL). This is why every "new Member(...)" in our
 *                    code and tests passes null for the id.
 * @Column         -> fine-tunes a column (uniqueness, nullability, length).
 */


import jakarta.persistence.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name= "members")
public class Member {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // NOTE FOR YOU: in a real production app this would be hashed with
    // BCryptPasswordEncoder (via Spring Security), never stored as plain
    // text. We're storing it plainly here ONLY to keep this project's
    // scope focused on Spring MVC / JPA - security is intentionally out
    // of scope for this practice project. This field must NEVER be
    // serialized to JSON directly - see MemberResponse DTO, which is what
    // controllers actually return instead of this entity.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime registeredOn;


    /*
     * ================= RELATIONSHIP 1: @OneToOne (BIDIRECTIONAL, inverse side) =================
     * `mappedBy = "member"` means THIS side does NOT own the relationship
     * and has NO foreign key column of its own. MemberProfile (the OTHER
     * side) has the actual `member_id` foreign key column, via
     * @JoinColumn. "member" here must exactly match the field name on
     * MemberProfile that points back to Member.
     * cascade = ALL -> saving/deleting a Member automatically
     * saves/deletes its MemberProfile too - you never call
     * memberProfileRepository.save() separately.
     */
    @OneToOne(mappedBy= "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberProfile profile;



    /*
     * ================= RELATIONSHIP 2: @OneToMany (BIDIRECTIONAL, inverse side) =================
     * One Member has many Bookings. `mappedBy = "member"` again means
     * THIS side has no foreign key column - Booking.member (annotated
     * @ManyToOne @JoinColumn(name="member_id")) is the owning side that
     * actually stores the FK. Now you can navigate member.getBookings()
     * as well as booking.getMember().
     * fetch = LAZY: without it, loading ONE member would eagerly load
     * ALL their bookings too, every single time.
     */
    @OneToMany(mappedBy= "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();




    /*
     * ================= RELATIONSHIP 3: @ManyToMany (UNIDIRECTIONAL) =================
     * A member can favorite many classes, and a class can be favorited by
     * many members - needs a JOIN TABLE (member_favorite_classes), not a
     * foreign key on either side.
     * UNIDIRECTIONAL: Member owns the relationship (@JoinTable declared
     * here), and FitnessClass has NO idea which members favorited it -
     * there's no mappedBy on FitnessClass for this. Compare this to
     * RELATIONSHIP 5 (Trainer <-> FitnessClass) which IS bidirectional,
     * to see both styles side by side.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_favorite_classes",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    @JsonIgnore
    private Set<FitnessClass> favoriteClasses = new HashSet<>();


    public Member() {
        // No-arg constructor required by JPA (Hibernate builds entities via
        // reflection, calling this constructor then setting fields).
    }

    public Member(Long id, String username, String email, String password, LocalDateTime registeredOn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registeredOn = registeredOn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getRegisteredOn() { return registeredOn; }
    public void setRegisteredOn(LocalDateTime registeredOn) { this.registeredOn = registeredOn; }

    public MemberProfile getProfile() { return profile; }
    public void setProfile(MemberProfile profile) { this.profile = profile; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public Set<FitnessClass> getFavoriteClasses() { return favoriteClasses; }
    public void setFavoriteClasses(Set<FitnessClass> favoriteClasses) { this.favoriteClasses = favoriteClasses; }








}
