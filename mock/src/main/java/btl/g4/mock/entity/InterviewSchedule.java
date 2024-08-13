package btl.g4.mock.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private Timestamp scheduleTimeStart;
    private Timestamp scheduleTimeEnd;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;


    @ManyToMany
    @JoinTable(
            name = "interview_schedule_user",
            joinColumns = @JoinColumn(name = "interview_schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> interviewers;

    private String location;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recruiterOwner;

    private String meetLink;
    private String status;
    private String result;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "update_by_id")
    private User updateBy;

    String resultNote;
}
