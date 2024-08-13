package btl.g4.mock.service.impl;

import btl.g4.mock.entity.Candidate;
import btl.g4.mock.entity.Job;
import btl.g4.mock.entity.JobCandidate;
import btl.g4.mock.repository.JobCandidateRepository;
import btl.g4.mock.service.JobCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobCandidateServiceImpl implements JobCandidateService {

    @Autowired
    private JobCandidateRepository jobCandidateRepository;

    @Override
    public List<Job> getJobByCandidateId(Integer id) {
        List<JobCandidate> jobCandidates = jobCandidateRepository.findByCandidateId(id);
        return jobCandidates.stream().map(JobCandidate::getJob).collect(Collectors.toList());
    }

    @Override
    public List<Job> getActiveJobsByCandidateId(Integer candidateId) {
        return jobCandidateRepository.findActiveJobsByCandidateId(candidateId);
    }

//    @Override
//    public JobCandidate applyToJob(Integer jobId, Integer candidateId) {
//        JobCandidate jobCandidate = new JobCandidate();
//        jobCandidate.setJob(Job(jobId));
//        jobCandidate.setCandidate(Candidate(candidateId));
//        return jobCandidateRepository.save(jobCandidate);
//    }
}
