package btl.g4.mock.dto;

//import btl.g4.mock.entity.Level;
import jakarta.persistence.Entity;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobForm {
    private Integer id;
    private String title;
    private String requiredSkills;
//    private Timestamp startDate;
//    private Timestamp endDate;
    private Date startDate;
    private Date endDate;
    private String level;
    private String status;
    private String description;
    private float salaryRangeFrom;
    private float salaryRangeTo;
    private String workingAddress;
    private String benefits;
}

