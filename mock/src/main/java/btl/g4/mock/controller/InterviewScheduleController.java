package btl.g4.mock.controller;

import btl.g4.mock.dto.InterviewScheduleDto;
import btl.g4.mock.entity.Candidate;
import btl.g4.mock.entity.InterviewSchedule;
import btl.g4.mock.entity.Job;
import btl.g4.mock.entity.User;
import btl.g4.mock.service.impl.*;
import btl.g4.mock.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/interview-schedule")
public class InterviewScheduleController {

    @Autowired
    private InterviewScheduleServiceImpl interviewScheduleService;

    @Autowired
    private JobServiceImpl jobService;

    @Autowired
    private CandidateServiceImpl candidateService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JobCandidateServiceImpl jobCandidateService;

    @GetMapping("")
    public String getInterviewSchedule(Model model,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       RedirectAttributes redirectAttributes) {

        try {
            String username = SecurityUtil.getUsername().orElse(null);
            String userRole = userService.getRole(username);

            Pageable pageable = PageRequest.of(page, size);
            Page<InterviewSchedule> interviewSchedulePage = interviewScheduleService.getAllInterviewSchedule(pageable);

            Set<User> interviewers = interviewSchedulePage.stream()
                    .flatMap(schedule -> schedule.getInterviewers().stream())
                    .collect(Collectors.toSet());

            Set<String> statuses = interviewSchedulePage.stream()
                    .map(InterviewSchedule::getStatus)
                    .collect(Collectors.toSet());

            model.addAttribute("userRole", userRole);
            model.addAttribute("interviewSchedules", interviewSchedulePage.getContent());
            model.addAttribute("interviewers", interviewers);
            model.addAttribute("statuses", statuses);
            model.addAttribute("username", username);
            model.addAttribute("totalPages", interviewSchedulePage.getTotalPages());
            model.addAttribute("currentPage", page);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed!");
        }

        return "interviewManagement/interviewSchedule";
    }


    @PreAuthorize("hasRole('MANAGE') or hasRole('RECRUITER') or hasRole('ADMIN')")
    @GetMapping("/add-interview-schedule")
    public String addInterviewSchedule(Model model, RedirectAttributes redirectAttributes) {

        try {
            String username = SecurityUtil.getUsername().orElse(null);
            List<User> interviewers = userService.getInterviewer();
            List<Candidate> candidates = candidateService.getAllCandidates();
            List<User> recruitersOwners = userService.getRecruiter();

            String fullName = userService.getUserByUsername(username).getFullName();
            Integer userId = userService.getUserByUsername(username).getId();

            model.addAttribute("candidates", candidates );
            model.addAttribute("interviewers", interviewers );
            model.addAttribute("recruiterOwners", recruitersOwners );
            model.addAttribute("interviewScheduleDto", new InterviewScheduleDto());
            model.addAttribute("username", username);
            model.addAttribute("fullName", fullName);
            model.addAttribute("userId", userId);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Failed!");
            return "redirect:/interview-schedule";
        }

        return "interviewManagement/addInterviewSchedule";
    }

    @PreAuthorize("hasRole('MANAGE') or hasRole('RECRUITER') or hasRole('ADMIN')")
    @GetMapping("/jobs-by-candidate")
    @ResponseBody
    public List<Job> getJobsByCandidate(@RequestParam Integer candidateId) {
        return jobCandidateService.getActiveJobsByCandidateId(candidateId);
    }


    @PreAuthorize("hasRole('MANAGE') or hasRole('RECRUITER') or hasRole('ADMIN')")
    @PostMapping("/save-interview-schedule")
    public String saveInterviewSchedule(@ModelAttribute InterviewScheduleDto interviewScheduleDto,
                                        RedirectAttributes redirectAttributes) {

        try {
            interviewScheduleService.saveInterviewSchedule(interviewScheduleDto);
            redirectAttributes.addFlashAttribute("message", "Interview schedule added successfully!");
        } catch (Exception e){
            System.out.println("Failed to save interview schedule");
            redirectAttributes.addFlashAttribute("message", "Fail to add interview schedule!");
        }

        return "redirect:/interview-schedule";
    }

    @GetMapping("/interview-schedule-detail/{id}")
    public String viewInterviewScheduleDetail(@PathVariable("id") Integer id, Model model,
                                              RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getUsername().orElse(null);
        String userRole = userService.getRole(username);
        model.addAttribute("username", username);
        model.addAttribute("userRole", userRole);

        try {
            InterviewSchedule interviewSchedule = interviewScheduleService.getInterviewSchedule(id);
            model.addAttribute("schedule", interviewSchedule);
            return "interviewManagement/interviewScheduleDetail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fail to load interview schedule detail!");
            return "redirect:/interview-schedule";
        }
    }

    @PreAuthorize("hasRole('INTERVIEWER')")
    @GetMapping("/submit-result/{id}")
    public String submitResult(@PathVariable Integer id, Model model,
                               RedirectAttributes redirectAttributes) {

        try {
            String username = SecurityUtil.getUsername().orElse(null);
            InterviewSchedule schedule = interviewScheduleService.getInterviewSchedule(id);

            model.addAttribute("username", username);
            model.addAttribute("interviewSchedule", schedule);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Fail to load data!");
            return "redirect:/interview-schedule";
        }

        return "interviewManagement/interviewScheduleSubmitResult";
    }

    @PreAuthorize("hasRole('INTERVIEWER')")
    @PostMapping("/submit-result")
    public String submitResult(@RequestParam("id") Integer id,
                               @RequestParam("note") String note,
                               @RequestParam("resultNote") String resultNote,
                               @RequestParam("result") String result,
                               Model model, RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getUsername().orElse(null);
        model.addAttribute("username", username);
        try {
            InterviewSchedule existingSchedule = interviewScheduleService.getInterviewSchedule(id);
            if (existingSchedule != null) {
                if( note.strip().equals("") ) note = "N/A";
                existingSchedule.setNote(note);
                existingSchedule.setResult(result);
                existingSchedule.setResultNote(resultNote);
                existingSchedule.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                Candidate candidate = existingSchedule.getCandidate();
                candidate.setStatus(result);
                candidate.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                candidateService.saveCandidate(candidate);
                User updateBy = userService.getUserByUsername(username);
                existingSchedule.setUpdateBy(updateBy);
                existingSchedule.setStatus("Closed");
                interviewScheduleService.updateInterviewSchedule(existingSchedule);
                redirectAttributes.addFlashAttribute("message", "Result submitted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Fail to found interview schedule!");

            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fail to update interview result!");
            e.printStackTrace();
        }
        return "redirect:/interview-schedule";
    }


    @PreAuthorize("hasRole('MANAGE') or hasRole('RECRUITER') or hasRole('ADMIN')")
    @GetMapping("/edit-interview-schedule/{id}")
    public String editInterviewSchedule(@PathVariable Integer id, Model model,
                                        RedirectAttributes redirectAttributes) {

        try {
            InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();
            String username = SecurityUtil.getUsername().orElse(null);
            String userRole = userService.getRole(username);
            InterviewSchedule interviewSchedule = interviewScheduleService.getInterviewSchedule(id);

            List<Job> jobs = jobService.getAllJobs();
            List<Candidate> candidates = candidateService.getAllCandidates();
            List<User> interviewers = userService.getInterviewer();
            List<User> recruiterOwners = userService.getRecruiter();
            String fullName = userService.getUserByUsername(username).getFullName();
            User user = userService.getUserByUsername(username);
            Integer userId = userService.getUserByUsername(username).getId();

            model.addAttribute("username", username);
            model.addAttribute("userRole", userRole);
            model.addAttribute("candidates", candidates);
            model.addAttribute("jobs", jobs);
            model.addAttribute("interviewers", interviewers);
            model.addAttribute("recruiterOwners", recruiterOwners);
            model.addAttribute("interviewSchedule", interviewSchedule);
            model.addAttribute("interviewScheduleDto", new InterviewScheduleDto());
            model.addAttribute("fullName", fullName);
            model.addAttribute("user", user);
            model.addAttribute("userId", userId);

            model.addAttribute("interviewScheduleDto", interviewScheduleDto);
            return "interviewManagement/editInterviewSchedule";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Fail to load data!");
            return "redirect:/interview-schedule";
        }
    }

    @PreAuthorize("hasRole('MANAGE') or hasRole('RECRUITER') or hasRole('ADMIN')")
    @PostMapping("/update-interview-schedule")
    public String updateInterviewSchedule(@RequestParam("id") Integer id,
                                          @RequestParam("date") String date,
                                          @RequestParam("timeStart") String timeStart,
                                          @RequestParam("timeEnd") String timeEnd,
                                          @ModelAttribute InterviewSchedule interviewSchedule,
                                          RedirectAttributes redirectAttributes,
                                          Model model) {

        try {
            String username = SecurityUtil.getUsername().orElse(null);
            interviewSchedule.setId(id);
            String scheduleTimeStartStr = date + " " + timeStart + ":00";
            String scheduleTimeEndStr = date + " " + timeEnd + ":00";
            String note = interviewSchedule.getNote().strip();
            if( note.equals("") ) note = "N/A";
            interviewSchedule.setNote(note);
            interviewSchedule.setScheduleTimeStart(Timestamp.valueOf(scheduleTimeStartStr));
            interviewSchedule.setScheduleTimeEnd(Timestamp.valueOf(scheduleTimeEndStr));
            interviewSchedule.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            User updateBy = userService.getUserByUsername(username);
            interviewSchedule.setUpdateBy(updateBy);
            interviewScheduleService.updateInterviewSchedule(interviewSchedule);
            redirectAttributes.addFlashAttribute("message", "Interview schedule updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fail to update interview schedule!");
        }
        return "redirect:/interview-schedule";
    }

    @PreAuthorize("hasRole('MANAGE') or hasRole('RECRUITER') or hasRole('ADMIN')")
    @PostMapping("/cancel-schedule")
    public String cancelSchedule(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            InterviewSchedule interviewSchedule = interviewScheduleService.getInterviewSchedule(id);
            interviewSchedule.setStatus("Cancelled");
//            interviewScheduleService.saveInterviewSchedule(interviewSchedule);
            Candidate candidate = interviewSchedule.getCandidate();
            candidate.setStatus("Cancelled Interview");
            String username = SecurityUtil.getUsername().orElse(null);
            User updateBy = userService.getUserByUsername(username);
            interviewSchedule.setUpdateBy(updateBy);
            candidate.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            candidateService.saveCandidate(candidate);
            interviewScheduleService.sendCancelInterviewEmail(interviewSchedule);
            interviewScheduleService.updateInterviewSchedule(interviewSchedule);
            redirectAttributes.addFlashAttribute("message", "Interview schedule cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fail to cancel interview schedule!");
        }
        return "redirect:/interview-schedule";
    }

    @PostMapping("/send-reminder")
    public String sendReminder(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        InterviewSchedule schedule = interviewScheduleService.getInterviewSchedule(id);
        try {
            schedule.setStatus("Invited");
            schedule.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            String username = SecurityUtil.getUsername().orElse(null);
            User updateBy = userService.getUserByUsername(username);
            schedule.setUpdateBy(updateBy);
            interviewScheduleService.updateInterviewSchedule(schedule);
            interviewScheduleService.sendReminderEmail(schedule);
            redirectAttributes.addFlashAttribute("message", "Reminder sent successfully!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Fail to send reminder!");

        }
        return "redirect:/interview-schedule/interview-schedule-detail/" + id ;
    }
}
