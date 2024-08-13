package btl.g4.mock.service.impl;

import btl.g4.mock.dto.JobDetails;
import btl.g4.mock.dto.JobEdit;
import btl.g4.mock.dto.JobForm;
import btl.g4.mock.entity.Job;
import btl.g4.mock.repository.JobRepository;
import btl.g4.mock.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job createJob(JobForm jobForm) {
        Job job = new Job();
        job.setTitle(jobForm.getTitle());
        job.setRequiredSkills(jobForm.getRequiredSkills());
        job.setStartDate(jobForm.getStartDate());
        job.setEndDate(jobForm.getEndDate());
        job.setLevel(jobForm.getLevel());
        job.setStatus("Draft");
        job.setSalaryRangeFrom(job.getSalaryRangeFrom());
        job.setSalaryRangeTo(job.getSalaryRangeTo());
//        job.setSalaryRange(jobForm.getSalaryRange());
        job.setWorkingAddress(jobForm.getWorkingAddress());
        job.setBenefits(jobForm.getBenefits());
        job.setDescription(jobForm.getDescription());
        return jobRepository.save(job);
    }

    @Override
    public JobDetails getJobDetails(Integer id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            JobDetails jobDetails = new JobDetails();
            jobDetails.setId(job.getId());
            jobDetails.setTitle(job.getTitle());
            jobDetails.setRequiredSkills(job.getRequiredSkills());
            jobDetails.setStartDate(job.getStartDate());
            jobDetails.setEndDate(job.getEndDate());
            jobDetails.setLevel(job.getLevel());
            jobDetails.setStatus(job.getStatus());
            jobDetails.setSalaryRangeFrom(job.getSalaryRangeFrom());
            jobDetails.setSalaryRangeTo(job.getSalaryRangeTo());
//            jobDetails.setSalaryRange(job.getSalaryRange());
            jobDetails.setWorkingAddress(job.getWorkingAddress());
            jobDetails.setBenefits(job.getBenefits());
            jobDetails.setDescription(job.getDescription());
            return jobDetails;
        }
        return null;
    }

    @Override
    public JobEdit getJobEdit(Integer id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            JobEdit jobEdit = new JobEdit();
            jobEdit.setId(job.getId());
            jobEdit.setTitle(job.getTitle());
            jobEdit.setRequiredSkills(job.getRequiredSkills());
            jobEdit.setStartDate(job.getStartDate());
            jobEdit.setEndDate(job.getEndDate());
            jobEdit.setLevel(job.getLevel());
            jobEdit.setStatus("Draft");
            jobEdit.setSalaryRangeFrom(job.getSalaryRangeFrom());
            jobEdit.setSalaryRangeTo(job.getSalaryRangeTo());
//            jobEdit.setSalaryRange(job.getSalaryRange());
            jobEdit.setWorkingAddress(job.getWorkingAddress());
            jobEdit.setBenefits(job.getBenefits());
            jobEdit.setDescription(job.getDescription());
            return jobEdit;
        }
        return null;
    }

    @Override
    public void saveJobEdit(JobEdit jobEdit) {
        Optional<Job> jobOptional = jobRepository.findById(jobEdit.getId());
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(jobEdit.getTitle());
            job.setRequiredSkills(jobEdit.getRequiredSkills());
            job.setStartDate(jobEdit.getStartDate());
            job.setEndDate(jobEdit.getEndDate());
            job.setLevel(jobEdit.getLevel());
            job.setStatus("Draft");
            job.setSalaryRangeFrom(jobEdit.getSalaryRangeFrom());
            job.setSalaryRangeTo(jobEdit.getSalaryRangeTo());
//            job.setSalaryRange(jobEdit.getSalaryRange());
            job.setWorkingAddress(jobEdit.getWorkingAddress());
            job.setBenefits(jobEdit.getBenefits());
            job.setDescription(jobEdit.getDescription());
            jobRepository.save(job);
        }
    }

    @Override
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job getJob(Integer id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Integer id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> search(String keyword) {
        if (keyword == null) {
            return getAllJobs(); // Return all jobs if no keyword or status is provided
        }
        return jobRepository.search(keyword);
    }

    @Override
    public Page<Job> getJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }
}

