package btl.g4.mock.service;

import btl.g4.mock.dto.JobDetails;
import btl.g4.mock.dto.JobEdit;
import btl.g4.mock.dto.JobForm;
import btl.g4.mock.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface JobService {
    public List<Job> getAllJobs();
    public Job createJob(JobForm jobForm);
    public Job saveJob(Job job);
    public Job getJob(Integer id);
    public Job updateJob(Job job);
    public void deleteJob(Integer id);
    public JobDetails getJobDetails(Integer id);
    public JobEdit getJobEdit(Integer id);
    public void saveJobEdit(JobEdit jobEdit);
    public List<Job> search(String keyword);
    public Page<Job> getJobs(Pageable pageable);
}

