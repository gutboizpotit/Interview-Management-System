package btl.g4.mock.service;

import btl.g4.mock.entity.Job;
import btl.g4.mock.entity.JobCandidate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobCandidateService {
    List<Job> getJobByCandidateId(Integer id);
    List<Job> getActiveJobsByCandidateId(@Param("candidateId") Integer candidateId);
//    public JobCandidate applyToJob(Integer jobId, Integer candidateId)
}
