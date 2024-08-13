package btl.g4.mock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "candidate")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String gender;
    private String phone;
//    private Timestamp dob;
    private Date dob;
    private String position;
    private String address;
    private String skills;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private User ownerHR;
    private String status;
    private String yearOfExperience;
    private String CV;
    private String highestLevel;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;
    private String note;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;
}
