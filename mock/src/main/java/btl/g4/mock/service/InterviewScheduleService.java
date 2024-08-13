package btl.g4.mock.service;

import btl.g4.mock.dto.InterviewScheduleDto;
import btl.g4.mock.entity.InterviewSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterviewScheduleService {
    List<InterviewSchedule> getAllInterviewSchedules();
    InterviewSchedule getInterviewSchedule(Integer id);
    void saveInterviewSchedule(InterviewScheduleDto interviewScheduleDto);
    void deleteInterviewSchedule(Integer id);
    void updateInterviewSchedule(InterviewSchedule interviewSchedule);
    void sendReminderEmail(InterviewSchedule interviewSchedule);
    void sendCancelInterviewEmail(InterviewSchedule interviewSchedule);
    List<InterviewSchedule> getAllInterviewSchedulesByCandidateId(Integer candidateId);
    Page<InterviewSchedule> getAllInterviewSchedule(Pageable pageable);
    void sendDailyReminders();
}
