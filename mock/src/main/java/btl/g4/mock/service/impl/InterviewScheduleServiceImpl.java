package btl.g4.mock.service.impl;

import btl.g4.mock.dto.InterviewScheduleDto;
import btl.g4.mock.entity.Candidate;
import btl.g4.mock.entity.User;
import btl.g4.mock.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import btl.g4.mock.entity.InterviewSchedule;
import btl.g4.mock.repository.InterviewScheduleRepository;
import btl.g4.mock.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CandidateServiceImpl candidateService;

    @Autowired
    private JobServiceImpl jobService;

    @Override
    public List<InterviewSchedule> getAllInterviewSchedules() {
        return interviewScheduleRepository.findAll();
    }

    @Override
    public InterviewSchedule getInterviewSchedule(Integer id) {
        return interviewScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public void saveInterviewSchedule(InterviewScheduleDto interviewScheduleDto) {
        String username = SecurityUtil.getUsername().orElse(null);
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        String scheduleTimeStartStr = interviewScheduleDto.getScheduleDate() + " " + interviewScheduleDto.getScheduleTimeStart() + ":00";
        String scheduleTimeEndStr = interviewScheduleDto.getScheduleDate() + " " + interviewScheduleDto.getScheduleTimeEnd() + ":00";
        interviewSchedule.setTitle(interviewScheduleDto.getTitle());
        interviewSchedule.setScheduleTimeStart(Timestamp.valueOf(scheduleTimeStartStr));
        interviewSchedule.setScheduleTimeEnd(Timestamp.valueOf(scheduleTimeEndStr));
        interviewSchedule.setJob(jobService.getJob(interviewScheduleDto.getJobId()));
        Candidate candidate = candidateService.getCandidate(interviewScheduleDto.getCandidateId());
        candidate.setStatus("Waiting to interview");
        candidate.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        candidateService.saveCandidate(candidate);
        interviewSchedule.setCandidate(candidate);
        interviewSchedule.setInterviewers(userService.findAllById(interviewScheduleDto.getInterviewerIds()));
        interviewSchedule.setLocation(interviewScheduleDto.getLocation());
        String note = interviewScheduleDto.getNote().strip();
        if( note.equals("") ) note = "N/A";
        interviewSchedule.setNote(note);
        User recuiter = userService.getUser(interviewScheduleDto.getRecruiterOwner());
        interviewSchedule.setRecruiterOwner(recuiter);
        interviewSchedule.setMeetLink(interviewScheduleDto.getMeetLink());
        interviewSchedule.setStatus("Open");
        interviewSchedule.setResult("N/A");
        interviewSchedule.setResultNote("N/A");
        interviewSchedule.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        interviewSchedule.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        User updateBy = userService.getUserByUsername(username);
        interviewSchedule.setUpdateBy(updateBy);
        interviewScheduleRepository.save(interviewSchedule);
        sendReminderEmail(interviewSchedule);
    }

    @Override
    public void deleteInterviewSchedule(Integer id) {
        interviewScheduleRepository.deleteById(id);
    }

    @Override
    public void updateInterviewSchedule(InterviewSchedule interviewSchedule) {
        interviewScheduleRepository.save(interviewSchedule);
    }

    @Override
    public void sendReminderEmail(InterviewSchedule interviewSchedule) {
        List<String> interviewerEmails = interviewSchedule.getInterviewers().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        interviewerEmails.add(interviewSchedule.getCandidate().getEmail());
        LocalDateTime scheduleDateTimeStart = interviewSchedule.getScheduleTimeStart().toLocalDateTime();
        LocalDateTime scheduleDateTimeEnd = interviewSchedule.getScheduleTimeEnd().toLocalDateTime();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formattedDate = scheduleDateTimeStart.format(dateFormatter);
        String formattedTimeStart = scheduleDateTimeStart.format(timeFormatter);
        String formattedTimeEnd = scheduleDateTimeEnd.format(timeFormatter);
        String[] emailArray = interviewerEmails.toArray(new String[0]);
        System.out.println( emailArray );
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailArray);
        message.setSubject("no-reply-email-IMS-system Interview Schedule Reminder");
        message.setText(String.format(
                "This email is from IMS system,\n\n" +
                        "You have an interview schedule %s at %s to %s\n" +
                        "Candidate: %s, Job: %s, the CV: %s\n" +
                        "If anything is wrong, please refer to recruiter %s or visit our website\n" +
                        "%s\n\n" +
                        "Please join interview room ID: %s",
                formattedDate, formattedTimeStart, formattedTimeEnd,
                interviewSchedule.getCandidate().getName(), interviewSchedule.getJob().getTitle(),
                interviewSchedule.getCandidate().getCV() ,interviewSchedule.getRecruiterOwner().getEmail(),
                "http://localhost:8888/interview-schedule/interview-schedule-detail/" + interviewSchedule.getId().toString(),
                interviewSchedule.getMeetLink()
        ));
        emailSender.send(message);
    }

    @Override
    public void sendCancelInterviewEmail(InterviewSchedule interviewSchedule) {
        List<String> interviewerEmails = interviewSchedule.getInterviewers().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        interviewerEmails.add(interviewSchedule.getCandidate().getEmail());
        LocalDateTime scheduleDateTimeStart = interviewSchedule.getScheduleTimeStart().toLocalDateTime();
        LocalDateTime scheduleDateTimeEnd = interviewSchedule.getScheduleTimeEnd().toLocalDateTime();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formattedDate = scheduleDateTimeStart.format(dateFormatter);
        String formattedTimeStart = scheduleDateTimeStart.format(timeFormatter);
        String formattedTimeEnd = scheduleDateTimeEnd.format(timeFormatter);

        String[] emailArray = interviewerEmails.toArray(new String[0]);
        System.out.println( emailArray );
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailArray);
        message.setSubject("no-reply-email-IMS-system Cancel Interview");
        message.setText(String.format(
                "This email is from IMS system,\n\n" +
                        "The interview scheduled on %s at %s to %s has been canceled.\n" +
                        "Candidate: %s\n" +
                        "Job: %s\n" +
                        "Location: %s\n\n" +
                        "If you have any questions, please refer to the recruiter %s or visit our website %s\n\n" +
                        "Thank you.",
                formattedDate,
                formattedTimeStart,
                formattedTimeEnd,
                interviewSchedule.getCandidate().getName(),
                interviewSchedule.getJob().getTitle(),
                interviewSchedule.getLocation(),
                interviewSchedule.getRecruiterOwner().getEmail(),
                "http://localhost:8888/interview-schedule/interview-schedule-detail" + interviewSchedule.getId().toString()
        ));
        emailSender.send(message);
    }

    @Override
    public List<InterviewSchedule> getAllInterviewSchedulesByCandidateId(Integer candidateId) {
        return interviewScheduleRepository.findAllInterviewSchedulesByCandidateId(candidateId);
    }

    @Override
    public Page<InterviewSchedule> getAllInterviewSchedule(Pageable pageable) {
        return interviewScheduleRepository.findAll(pageable);
    }

    @Override
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void sendDailyReminders() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfTomorrow = today.plusDays(1).with(LocalTime.MIN);
        LocalDateTime endOfTomorrow = today.plusDays(1).with(LocalTime.MAX);
        List<InterviewSchedule> schedules = interviewScheduleRepository.findSchedulesForTomorrow(startOfTomorrow, endOfTomorrow);
        System.out.println("Run sendDailyReminders");
        System.out.println(schedules.size());
        for (InterviewSchedule schedule : schedules) {
            sendReminderEmail(schedule);
        }
    }
}
