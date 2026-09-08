package org.spring.core.learn.spring14fitclubmvc.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
 * ================= RELATIONSHIP 2 (continued) & 6: @ManyToOne, TWO DIFFERENT STYLES =================
 * A Booking links ONE Member to ONE FitnessClass. Both fields below are
 * @ManyToOne - but they demonstrate the two different styles on purpose:
 *
 *   - `member`: the OWNING side of a BIDIRECTIONAL relationship (RELATIONSHIP 2).
 *     Member.java has a matching @OneToMany(mappedBy = "member")
 *     List<Booking> bookings, so you can navigate BOTH ways:
 *     booking.getMember() AND member.getBookings().
 *
 *   - `fitnessClass`: a UNIDIRECTIONAL @ManyToOne (RELATIONSHIP 6).
 *     FitnessClass has NO matching @OneToMany back-reference at all -
 *     there is no fitnessClass.getBookings(). If you need "all bookings
 *     for this class", you go through a repository QUERY instead
 *     (BookingRepository.countByFitnessClass_IdAndStatus) rather than
 *     walking an in-memory collection - this avoids FitnessClass ever
 *     accidentally loading a huge list of bookings just because someone
 *     fetched a class.
 *
 * @JoinColumn(name = "member_id"/"class_id") -> the actual FOREIGN KEY
 * columns in the "bookings" table. Forgetting to SET these fields before
 * saving produces a NULL foreign key.
 */


@Entity
@Table(name= "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;



    @ManyToOne    //FetchType is not set to Lazy means When this Entirty data is Fetched along with it FitnessClass also is Fetched. EAGER
    @JoinColumn(name = "class_id", nullable= false)
    private FitnessClass fitnessClass;


    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING) // stores "CONFIRMED"/"CANCELLED" as text, not 0/1 ordinals
    @Column(nullable = false)
    private BookingStatus status;

    public Booking() {}

    public Booking(Long id, Member member, FitnessClass fitnessClass, LocalDateTime bookingDate, BookingStatus status) {
        this.id = id;
        this.member = member;
        this.fitnessClass = fitnessClass;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public FitnessClass getFitnessClass() { return fitnessClass; }
    public void setFitnessClass(FitnessClass fitnessClass) { this.fitnessClass = fitnessClass; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }



}
