package btl.g4.mock.controller;

import btl.g4.mock.dto.JobDetails;
import btl.g4.mock.dto.JobEdit;
import btl.g4.mock.dto.JobForm;
import btl.g4.mock.entity.Job;
import btl.g4.mock.repository.JobRepository;
import btl.g4.mock.service.impl.JobServiceImpl;
import btl.g4.mock.service.impl.UserServiceImpl;
import btl.g4.mock.util.DropdownValues;
import btl.g4.mock.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class JobController {

    @Autowired
    private DropdownValues dropdownValues;

    @Autowired
    private JobServiceImpl jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/job-management")
    public String getJob(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        model.addAttribute("jobs", jobRepository.findAllOrderByCreatedAtDesc());
//        List<Job> jobs = jobService.getAllJobs();
        String username = SecurityUtil.getUsername().orElse(null);
//        String userRole = userService.getRole(username);
//        model.addAttribute("userRole", userRole);
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobPage = jobService.getJobs(pageable);
        model.addAttribute("jobPage", jobPage);
        model.addAttribute("username", username);
        try {
            String userRole=userService.getRole(username);
            model.addAttribute("userRole", userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "JobManagement/listJob";
    }

    @GetMapping("/job-management/create-job")
    @PreAuthorize("hasRole('MANAGER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public String createJobForm(Model model) {
        model.addAttribute("jobForm", new JobForm());
        model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
        return "JobManagement/createJob";
//        return "redirect:/job-management";
    }

    @PostMapping("/job-management/create-job")
    @PreAuthorize("hasRole('MANAGER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public String createJob(@ModelAttribute JobForm jobForm, RedirectAttributes redirectAttributes){
//        jobService.createJob(jobForm);
//        return "redirect:/job-management";
        try {
            jobService.createJob(jobForm);
            redirectAttributes.addFlashAttribute("message", "Successfully created job");
            return "redirect:/job-management";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to create job");
            return "redirect:/job-management/create-job";
        }
    }

    @GetMapping("/job-management/view/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('RECRUITER') or hasRole('ADMIN') or hasRole('INTERVIEWER')")
    public String jobDetails(@PathVariable Integer id, Model model) {
        JobDetails jobDetails = jobService.getJobDetails(id);
        String username = SecurityUtil.getUsername().orElse(null);
        try {
            String userRole=userService.getRole(username);
            model.addAttribute("userRole", userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jobDetails != null) {
            model.addAttribute("jobDetails", jobDetails);
            model.addAttribute("username", username);
            model.addAttribute("id", jobDetails.getId());
            return "JobManagement/jobDetails";
        } else {
            return "redirect:/job-management";
        }
    }

    @GetMapping("/job-management/edit/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String editJob(@PathVariable Integer id, Model model) {
        JobEdit jobEdit = jobService.getJobEdit(id);
        if (jobEdit != null) {
            model.addAttribute("jobEdit", jobEdit);
            model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
            return "jobManagement/editJob";
        } else {
            return "redirect:/job-management";
        }
    }

    @PostMapping("/job-management/edit")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String saveJobEdit(@ModelAttribute JobEdit jobEdit, RedirectAttributes redirectAttributes) {
        try {
            jobService.saveJobEdit(jobEdit);
            redirectAttributes.addFlashAttribute("edit_message", "Successfully edited job");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit_error", "Failed to edit job");
            e.printStackTrace();
        }

        return "redirect:/job-management";
    }

    @GetMapping("/job-management/delete/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String deleteJob(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            jobService.deleteJob(id);
            redirectAttributes.addFlashAttribute("delete_message", "Successfully deleted job");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("delete_error", "Failed to delete job");
        }
//        jobService.deleteJob(id);
        return "redirect:/job-management";
    }

    @GetMapping("/job-management/search")
    public String searchJobs(@RequestParam("keyword") String keyword, Model model) {
        List<Job> jobs = jobService.search(keyword);
        model.addAttribute("jobs", jobs);
        if(jobs.isEmpty()) {
            model.addAttribute("noResults", true);
        }
        return "JobManagement/listJob";
    }
}

