package btl.g4.mock.dto;

//import btl.g4.mock.entity.Level;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.sql.Date;

@Getter
@Setter
public class JobEdit {
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
    //    private String salaryRange;
    private float salaryRangeFrom;
    private float salaryRangeTo;
    private String workingAddress;
    private String benefits;
}

