package btl.g4.mock.dto;

import btl.g4.mock.entity.Candidate;
import btl.g4.mock.entity.Job;
import btl.g4.mock.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InterviewScheduleDto {
    private int id;
    private String title;
    private Integer candidateId;
    private String scheduleDate;
    private String scheduleTimeStart;
    private String scheduleTimeEnd;
    private String note;
    private Integer jobId;
    private List<Integer> interviewerIds;
    private String location;
    private Integer recruiterOwner;
    private String meetLink;
}
