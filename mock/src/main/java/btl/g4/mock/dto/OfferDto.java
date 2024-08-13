package btl.g4.mock.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private Integer candidateId;
    private String contractType;
    private String position;
    private String level;
    private Integer approverId;
    private String department;
    private Long interviewInfo;
    private Integer recruiterOwnerId;
    private String contractPeriodStart;
    private String contractPeriodEnd;
    private String dueDate;
    private String interviewNote;
    private float basicSalary;
    private String note;
}
