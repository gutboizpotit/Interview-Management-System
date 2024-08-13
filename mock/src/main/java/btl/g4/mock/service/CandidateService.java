package btl.g4.mock.service;

import btl.g4.mock.entity.Candidate;

import java.util.List;

public interface CandidateService {
    public List<Candidate> getAllCandidates();
    public Candidate getCandidate(Integer id);
    public Candidate updateCandidate(Candidate candidate);
    public Candidate saveCandidate(Candidate candidate);
    public void deleteCandidate(Integer id);
    public void banCandidate(Integer id);
    List<Candidate> getAllCandidatesNotBanned();
    List<Candidate> getAllCandidateInInterviewSchedule();
    List<Candidate> search(String keyword);
}
