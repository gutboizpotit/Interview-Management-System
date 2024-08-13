package btl.g4.mock.service.impl;

import btl.g4.mock.entity.Candidate;
import btl.g4.mock.repository.CandidateRepository;
import btl.g4.mock.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate getCandidate(Integer id) {
        return candidateRepository.findById(id).orElse(null);
    }

    @Override
    public Candidate updateCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public void deleteCandidate(Integer id) {
        candidateRepository.deleteById(id);
    }

    @Override
    public List<Candidate> getAllCandidatesNotBanned() {
        return candidateRepository.findAllCandidatesNotBanned();
    }

    @Override
    public List<Candidate> getAllCandidateInInterviewSchedule() {
        return candidateRepository.findAllCandidateInInterviewSchedule();
    }

    @Override
    public List<Candidate> search(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return getAllCandidates();
        }
        return candidateRepository.search(keyword);
    }

    @Override
    public void banCandidate(Integer id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid candidate Id:" + id));
        candidate.setStatus("Banned");
        candidateRepository.save(candidate);
    }
}
