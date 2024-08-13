package btl.g4.mock.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    private String position;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @ManyToOne
    @JoinColumn(name = "interviewSchedule_id")
    private InterviewSchedule interviewInfo;

    private Timestamp contractPeriodStart;
    private Timestamp contractPeriodEnd;
    private String interviewNote;
    private String contractType;
    private String level;
    private String department;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiterOwner;

    private Timestamp dueDate;
    private String status;
    private float basicSalary;
    private String note;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;
}
