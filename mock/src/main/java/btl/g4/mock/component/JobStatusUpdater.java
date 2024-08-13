package btl.g4.mock.component;

import btl.g4.mock.entity.Job;
import btl.g4.mock.repository.JobRepository;
import btl.g4.mock.service.impl.JobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JobStatusUpdater {
    @Autowired
    private JobServiceImpl jobService;

    @Scheduled(cron = "0 0 0 * * ?") // everyday at 00:00
    public void updateJobStatuses() {
        List<Job> jobs = jobService.getAllJobs();
        Date now = new Date();
        for (Job job : jobs) {
            if(now.after(job.getEndDate())) {
                job.setStatus("Close");
            } else if(now.after(job.getStartDate())) {
                job.setStatus("Open");
            } else if(now.before(job.getStartDate())) {
                job.setStatus("Draft");
            }
            jobService.updateJob(job);
        }
    }
}
