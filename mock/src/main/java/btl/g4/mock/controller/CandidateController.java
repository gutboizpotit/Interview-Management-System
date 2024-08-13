package btl.g4.mock.controller;

import btl.g4.mock.dto.JobEdit;
import btl.g4.mock.entity.Candidate;
import btl.g4.mock.entity.Job;
import btl.g4.mock.entity.User;
import btl.g4.mock.repository.CandidateRepository;
import btl.g4.mock.repository.JobRepository;
import btl.g4.mock.service.impl.CandidateServiceImpl;
import btl.g4.mock.service.impl.JobServiceImpl;
import btl.g4.mock.service.impl.UserServiceImpl;
import btl.g4.mock.util.DropdownValues;
import btl.g4.mock.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    private DropdownValues dropdownValues;

    @Autowired
    private CandidateServiceImpl candidateService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobServiceImpl jobService;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/candidate-management")
    public String getCandidate(Model model) {
//        List<Candidate> candidates = candidateService.getAllCandidates();
//        Set<String> statuses = candidates.stream().map(Candidate::getStatus).collect(Collectors.toSet());
        String username = SecurityUtil.getUsername().orElse(null);
        model.addAttribute("username", username);
        model.addAttribute("candidates", candidateRepository.findAllOrderByStatusAndCreatedAtDesc());
        try {
            String userRole=userService.getRole(username);
            model.addAttribute("userRole", userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        model.addAttribute("statuses", statuses);
        return "CandidateManagement/listCandidate";
    }

    @GetMapping("/candidate-management/create-candidate")
    public String createCandidateForm(Model model) {
        model.addAttribute("jobs", jobRepository.findAllOrderByCreatedAtDesc());
        model.addAttribute("candidate", new Candidate());
        model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
        model.addAttribute("recruiters", userService.getRecruiter());
        return "CandidateManagement/createCandidate";
    }

    @PostMapping("/candidate-management/create-candidate")
    public String createCandidate(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        try {
            candidateService.saveCandidate(candidate);
            redirectAttributes.addFlashAttribute("candidate_message", "Successfully created candidate");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error_candidate_message", "Failed to create candidate");
        }
//        candidateService.saveCandidate(candidate);
        return "redirect:/candidate-management";
    }

    @GetMapping("/candidate-management/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('RECRUITER') or hasRole('ADMIN') or hasRole('INTERVIEWER')")
    public String getCandidate(@PathVariable Integer id, Model model) {
        Candidate candidate = candidateService.getCandidate(id);
        String username = SecurityUtil.getUsername().orElse(null);
        try {
            String userRole=userService.getRole(username);
            model.addAttribute("userRole", userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (candidate != null) {
            model.addAttribute("candidate", candidate);
            model.addAttribute("username", username);
            model.addAttribute("positions", dropdownValues.getPositions());
            model.addAttribute("departments", dropdownValues.getDepartments());
            model.addAttribute("recruiters", userService.getRecruiter());
            return "CandidateManagement/candidateInformation";
        } else {
            return "redirect:/candidate-management";
        }
    }

    @GetMapping("/candidate-management/edit/{id}")
    public String editCandidate(@PathVariable Integer id, Model model) {
        Candidate candidate = candidateService.getCandidate(id);
        if (candidate != null) {
            model.addAttribute("candidate", candidate);
            model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
            model.addAttribute("positions", dropdownValues.getPositions());
            model.addAttribute("departments", dropdownValues.getDepartments());
            model.addAttribute("recruiters", userService.getRecruiter());
            return "CandidateManagement/editCandidate";
        } else {
            return "redirect:/candidate-management";
        }
    }

    @PostMapping("/candidate-management/edit")
    public String saveCandidateEdit(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        try {
            candidateService.saveCandidate(candidate);
            redirectAttributes.addFlashAttribute("edit_candidate_message", "Successfully edited candidate");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("edit_candidate_error", "Failed to save candidate");
        }
//        candidateService.saveCandidate(candidate);
        return "redirect:/candidate-management";
    }

    @GetMapping("/candidate-management/delete/{id}")
    public String deleteCandidate(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.deleteCandidate(id);
            redirectAttributes.addFlashAttribute("delete_candidate_message", "Successfully deleted candidate");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("delete_candidate_error", "Failed to delete candidate");
        }
//        candidateService.deleteCandidate(id);
        return "redirect:/candidate-management";
    }

    @GetMapping("/candidate-management/search")
    public String searchCandidates(@RequestParam("keyword") String keyword, Model model) {
        List<Candidate> candidates = candidateService.search(keyword);
        model.addAttribute("candidates", candidates);
        if(candidates.isEmpty()) {
            model.addAttribute("noResults", true);
        }
        return "CandidateManagement/listCandidate";
    }

    @GetMapping("/candidate-management/ban/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('MANAGER')")
    public String banCandidate(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.banCandidate(id);
            redirectAttributes.addFlashAttribute("ban_candidate_message", "Successfully banned candidate");
        } catch (Exception e) {
            logger.error("Error banning candidate", e);
            redirectAttributes.addFlashAttribute("ban_candidate_error", "Failed to ban candidate");
        }
        return "redirect:/candidate-management";
    }
}
