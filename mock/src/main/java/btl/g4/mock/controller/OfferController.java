package btl.g4.mock.controller;

import btl.g4.mock.dto.OfferDto;
import btl.g4.mock.entity.Candidate;
import btl.g4.mock.entity.InterviewSchedule;
import btl.g4.mock.entity.Offer;
import btl.g4.mock.entity.User;
import btl.g4.mock.repository.OfferRepository;
import btl.g4.mock.security.CustomUserDetail;
import btl.g4.mock.service.CandidateService;
import btl.g4.mock.service.InterviewScheduleService;
import btl.g4.mock.service.OfferService;
import btl.g4.mock.service.UserService;
import btl.g4.mock.util.DropdownValues;
import btl.g4.mock.util.SecurityUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class OfferController {

    @Autowired
    private DropdownValues dropdownValues;

    @Autowired
    private OfferService offerService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @Autowired
    private JavaMailSender emailSender;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer-management")
    public String getListOffer(@RequestParam(defaultValue = "") String keyword,
                               @RequestParam(defaultValue = "") String department,
                               @RequestParam(defaultValue = "") String status,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        CustomUserDetail userDetails = SecurityUtil.getUserDetails().orElse(null);
        assert userDetails != null;
        System.out.println("userDetail: " + userDetails.getAuthorities());
        Page<Offer> offerPage = offerService.searchOffers(keyword, department, status, PageRequest.of(page, size));
        model.addAttribute("offers", offerPage.getContent());
        model.addAttribute("totalPages", offerPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("department", department);
        model.addAttribute("status", status);
        model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
        model.addAttribute("departments", dropdownValues.getDepartments());
        model.addAttribute("statuses", dropdownValues.getOfferStatus()); // Assuming you have a method to get status values
        return "OfferManagement/offer-list";
    }

    @PostMapping("/offer/export")
    public void exportData(@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo, HttpServletResponse response) throws ParseException {
        System.out.println(dateFrom + " " + dateTo);
        String fileName = "Offerlist-" + dateFrom + "_" + dateTo + ".xls";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        offerService.exportOffer(dateFrom, dateTo, response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/create-offer")
    public String getNewOfferForm(Model model) {
        model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
        model.addAttribute("offer", new OfferDto());
        model.addAttribute("contractTypes", dropdownValues.getContractTypes());
        model.addAttribute("positions", dropdownValues.getPositions());
        model.addAttribute("levels", dropdownValues.getLevels());
        model.addAttribute("departments", dropdownValues.getDepartments());
        model.addAttribute("interviewSchedules", interviewScheduleService.getAllInterviewSchedules());
        model.addAttribute("recruiters", userService.getRecruiter());
        model.addAttribute("candidates", candidateService.getAllCandidateInInterviewSchedule());
        model.addAttribute("approvers", userService.getManagers());
        List<InterviewSchedule> interviewSchedules = interviewScheduleService.getAllInterviewSchedules();
        Map<Integer, List<InterviewSchedule>> interviewSchedulesByCandidateId = new HashMap<>();
        for (InterviewSchedule schedule : interviewSchedules) {
            Integer candidateId = schedule.getCandidate().getId();
            if (!interviewSchedulesByCandidateId.containsKey(candidateId)) {
                interviewSchedulesByCandidateId.put(candidateId, new ArrayList<>());
            }
            interviewSchedulesByCandidateId.get(candidateId).add(schedule);
        }
        System.out.println(interviewSchedules);
        model.addAttribute("interviewSchedulesByCandidateId", interviewSchedulesByCandidateId);
        return "OfferManagement/offer-form";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/view/{id}")
    public String viewOffer(@PathVariable("id") Integer id, Model model) {

        Offer offer = offerService.getOfferById(id);
        List<User> interviewers = offer.getInterviewInfo().getInterviewers();
        List<String> interviewerNames = interviewers.stream().map(User::getUsername).toList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String contractPeriodStart = sdf.format(offer.getContractPeriodStart());
        String contractPeriodEnd = sdf.format(offer.getContractPeriodEnd());
        String dueDate = sdf.format(offer.getDueDate());
        String createdAt = sdf.format(offer.getCreatedAt());
        LocalDate currentDate = LocalDate.now();
        LocalDate sqlDate = offer.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        if (currentDate.isEqual(sqlDate)) createdAt = "today";
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        String basicSalary = numberFormat.format(offer.getBasicSalary());
        model.addAttribute("offer", offer);
        model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
        model.addAttribute("contractPeriodStart", contractPeriodStart);
        model.addAttribute("contractPeriodEnd", contractPeriodEnd);
        model.addAttribute("dueDate", dueDate);
        model.addAttribute("createdAt", createdAt);
        model.addAttribute("basicSalary", basicSalary);
        model.addAttribute("interviewerNames", String.join(", ", interviewerNames));
        return "OfferManagement/offer-view";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/edit/{id}")
    public String editOffer(@PathVariable("id") Integer id, Model model) {
        Offer offer = offerService.getOfferById(id);
        model.addAttribute("offer", offer);
        model.addAttribute("contractTypes", dropdownValues.getContractTypes());
        model.addAttribute("positions", dropdownValues.getPositions());
        model.addAttribute("levels", dropdownValues.getLevels());
        model.addAttribute("departments", dropdownValues.getDepartments());
        model.addAttribute("recruiters", userService.getRecruiter());
        model.addAttribute("candidates", candidateService.getAllCandidateInInterviewSchedule());
        model.addAttribute("approvers", userService.getManagers());
        model.addAttribute("username", SecurityUtil.getUsername().orElse(""));
        List<InterviewSchedule> interviewSchedules = interviewScheduleService.getAllInterviewSchedules();
        Map<Integer, List<InterviewSchedule>> interviewSchedulesByCandidateId = new HashMap<>();
        for (InterviewSchedule schedule : interviewSchedules) {
            Integer candidateId = schedule.getCandidate().getId();
            if (!interviewSchedulesByCandidateId.containsKey(candidateId)) {
                interviewSchedulesByCandidateId.put(candidateId, new ArrayList<>());
            }
            interviewSchedulesByCandidateId.get(candidateId).add(schedule);
        }
        System.out.println(interviewSchedules);
        model.addAttribute("interviewSchedulesByCandidateId", interviewSchedulesByCandidateId);
        return "OfferManagement/offer-edit";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @PostMapping("/offer/save-offer")
    public String saveOffer(@ModelAttribute("offer") OfferDto offerDto, RedirectAttributes redirectAttributes) throws ParseException {
        System.out.println(offerDto);
        offerService.saveOffer(offerDto);
        redirectAttributes.addFlashAttribute("successMessage", "Offer created successfully!");
        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @PostMapping("/offer/update-offer/{id}")
    public String updateOffer(@PathVariable("id") int id, @ModelAttribute("offer") OfferDto offerDto, RedirectAttributes redirectAttributes) throws ParseException {
        System.out.println("Id: " + id + " OfferDto: " + offerDto);
        offerService.updateOffer(id, offerDto);
        redirectAttributes.addFlashAttribute("successMessage", "Offer updated successfully!");
        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/offer/approve/{id}")
    public String approveOffer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Offer offer = offerService.getOfferById(id);
        offer.setStatus("Approved");
        offerService.saveOffer(offer);
        Candidate candidate = offer.getCandidate();
        candidate.setStatus("Approved Offer");
        candidateService.saveCandidate(candidate);
        redirectAttributes.addFlashAttribute("successMessage", "Offer approved successfully!");
        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/offer/reject/{id}")
    public String rejectOffer(@PathVariable Integer id, @RequestParam(value = "note", defaultValue = "") String note, RedirectAttributes redirectAttributes) {
        Offer offer = offerService.getOfferById(id);
        offer.setStatus("Rejected");
        offerService.saveOffer(offer);
        Candidate candidate = offer.getCandidate();
        candidate.setStatus("Rejected Offer");
        if (!note.isEmpty()) {
            offer.setNote(note);
        }
        candidateService.saveCandidate(candidate);
        System.out.println("note " + note);
        redirectAttributes.addFlashAttribute("successMessage", "Offer rejected successfully!");
        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/mark-send/{id}")
    public String markSentOffer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Offer offer = offerService.getOfferById(id);
        offer.setStatus("Waiting for Response");
        offerService.saveOffer(offer);
        Candidate candidate = offer.getCandidate();
        candidate.setStatus("Waiting for Response");
        candidateService.saveCandidate(candidate);
        redirectAttributes.addFlashAttribute("successMessage", "Offer sent to candidate successfully!");

        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/accept/{id}")
    public String acceptOffer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Offer offer = offerService.getOfferById(id);
        offer.setStatus("Accepted");
        offerService.saveOffer(offer);
        Candidate candidate = offer.getCandidate();
        candidate.setStatus("Accepted Offer");
        candidateService.saveCandidate(candidate);
        redirectAttributes.addFlashAttribute("successMessage", "Offer accepted successfully!");
        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/decline/{id}")
    public String declineOffer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Offer offer = offerService.getOfferById(id);
        offer.setStatus("Declined");
        offerService.saveOffer(offer);
        Candidate candidate = offer.getCandidate();
        candidate.setStatus("Declined Offer");
        candidateService.saveCandidate(candidate);
        redirectAttributes.addFlashAttribute("successMessage", "Offer declined successfully!");

        return "redirect:/offer-management";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    @GetMapping("/offer/cancel-offer/{id}")
    public String cancelOffer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Offer offer = offerService.getOfferById(id);
        offer.setStatus("Cancelled");
        offerService.saveOffer(offer);
        Candidate candidate = offer.getCandidate();
        candidate.setStatus("Cancelled Offer");
        candidateService.saveCandidate(candidate);
        redirectAttributes.addFlashAttribute("successMessage", "Offer canceled successfully!");
        return "redirect:/offer-management";
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendEmail() {
        List<Offer> waitingOffer = offerService.getOffersWaiting();
        if (waitingOffer.isEmpty()) {
            return;
        }
        List<User> managers = userService.getManagers();
        for (User manager : managers) {
            try {
                sendEmail(manager.getEmail(), waitingOffer);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendEmail(String to, List<Offer> offers) throws MessagingException {
        String subject = "Reminder: Offers Awaiting Approval";
        StringBuilder text = new StringBuilder("Please review and approve the following offers:\n\n");

        for (Offer offer : offers) {
            String linkOfferDetail = "http://localhost:8888/offer/view/" + offer.getId();
            text.append("Offer ID: ").append(offer.getId()).append("\nClick here to review: ").append(linkOfferDetail).append("\n\n");
        }

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text.toString());

        emailSender.send(mimeMessage);
    }
}
