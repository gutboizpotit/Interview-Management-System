package btl.g4.mock.repository;

import btl.g4.mock.entity.Job;
import btl.g4.mock.entity.JobCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobCandidateRepository extends JpaRepository<JobCandidate, Integer> {
    List<JobCandidate> findByCandidateId(Integer id);

            @Query("SELECT jc.job FROM JobCandidate jc WHERE jc.candidate.id = :candidateId AND jc.job.status = 'Open'")
    List<Job> findActiveJobsByCandidateId(@Param("candidateId") Integer candidateId);
}
