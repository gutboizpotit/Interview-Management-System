package btl.g4.mock.service.impl;

import btl.g4.mock.dto.OfferDto;
import btl.g4.mock.entity.InterviewSchedule;
import btl.g4.mock.entity.Offer;
import btl.g4.mock.repository.CandidateRepository;
import btl.g4.mock.repository.InterviewScheduleRepository;
import btl.g4.mock.repository.OfferRepository;
import btl.g4.mock.repository.UserRepository;
import btl.g4.mock.service.OfferService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Override
    public Offer getOfferById(int id) {
        return offerRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOffer(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    public void saveOffer(OfferDto offerDto) throws ParseException {
        Offer offer = convertToEntity(offerDto);
        offerRepository.save(offer);
    }

    @Override
    public void updateOffer(int id, OfferDto offerDto) throws ParseException {
        Offer foundOffer = offerRepository.findById(id).orElse(null);
        if (foundOffer != null) {
            foundOffer.setCandidate(candidateRepository.findById(offerDto.getCandidateId()).orElse(null));
            foundOffer.setContractType(offerDto.getContractType());
            foundOffer.setPosition(offerDto.getPosition());
            foundOffer.setLevel(offerDto.getLevel());
            foundOffer.setApprover(userRepository.findById(offerDto.getApproverId()).orElse(null));
            foundOffer.setDepartment(offerDto.getDepartment());
            InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(offerDto.getCandidateId()).orElse(null);
            foundOffer.setInterviewInfo(interviewSchedule);
            foundOffer.setRecruiterOwner(userRepository.findById(offerDto.getRecruiterOwnerId()).orElse(null));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            foundOffer.setContractPeriodStart(new Timestamp(dateFormat.parse(offerDto.getContractPeriodStart()).getTime()));
            foundOffer.setContractPeriodEnd(new Timestamp(dateFormat.parse(offerDto.getContractPeriodEnd()).getTime()));

            foundOffer.setDueDate(new Timestamp(dateFormat.parse(offerDto.getDueDate()).getTime()));
            foundOffer.setInterviewNote(offerDto.getInterviewNote());
            foundOffer.setBasicSalary(offerDto.getBasicSalary());
            foundOffer.setNote(offerDto.getNote());
            foundOffer.setStatus("Waiting for Approval");
            offerRepository.save(foundOffer);
        }
    }

    @Override
    public void cancelOffer(int id) {
    }

    @Override
    public Page<Offer> searchOffers(String keyword, String department, String status, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty() && department != null && !department.isEmpty() && status != null && !status.isEmpty()) {
            return offerRepository.findByKeywordAndDepartmentAndStatus(keyword, department, status, pageable);
        } else if (keyword != null && !keyword.isEmpty() && department != null && !department.isEmpty()) {
            return offerRepository.findByKeywordAndDepartment(keyword, department, pageable);
        } else if (keyword != null && !keyword.isEmpty() && status != null && !status.isEmpty()) {
            return offerRepository.findByKeywordAndStatus(keyword, status, pageable);
        } else if (department != null && !department.isEmpty() && status != null && !status.isEmpty()) {
            return offerRepository.findByDepartmentAndStatus(department, status, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            return offerRepository.findByKeyword(keyword, pageable);
        } else if (department != null && !department.isEmpty()) {
            return offerRepository.findByDepartment(department, pageable);
        } else if (status != null && !status.isEmpty()) {
            return offerRepository.findByStatus(status, pageable);
        } else {
            return offerRepository.findAllDesc(pageable);
        }
    }

    @Override
    public void exportOffer(String dateFrom, String dateTo, HttpServletResponse response) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date from = dateFormat.parse(dateFrom);
        Date to = dateFormat.parse(dateTo);
        List<Offer> offers = offerRepository.findOffersBetweenDates(from, to);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Offers");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Candidate");
        row.createCell(2).setCellValue("Position");
        row.createCell(3).setCellValue("Level");
        row.createCell(4).setCellValue("Approver");
        row.createCell(5).setCellValue("Department");
        row.createCell(6).setCellValue("Contract Type");
        row.createCell(7).setCellValue("Interview Info");
        row.createCell(8).setCellValue("Basic Salary");
        row.createCell(9).setCellValue("Note");
        row.createCell(10).setCellValue("Due Date");
        row.createCell(11).setCellValue("Contract Period Start");
        row.createCell(12).setCellValue("Contract Period End");
        row.createCell(13).setCellValue("Recruiter Owner");
        row.createCell(14).setCellValue("Status");
        row.createCell(15).setCellValue("Created Date");
        row.createCell(16).setCellValue("Updated Date");

        int rowNum = 1;
        for(Offer offer : offers) {
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(offer.getId());
            row.createCell(1).setCellValue(offer.getCandidate().getName());
            row.createCell(2).setCellValue(offer.getPosition());
            row.createCell(3).setCellValue(offer.getLevel());
            row.createCell(4).setCellValue(offer.getApprover().getUsername());
            row.createCell(5).setCellValue(offer.getDepartment());
            row.createCell(6).setCellValue(offer.getContractType());
            row.createCell(7).setCellValue(offer.getInterviewInfo().getTitle());
            row.createCell(8).setCellValue(offer.getBasicSalary());
            row.createCell(9).setCellValue(offer.getNote());
            row.createCell(10).setCellValue(dateFormat.format(offer.getDueDate()));
            row.createCell(11).setCellValue(dateFormat.format(offer.getContractPeriodEnd()));
            row.createCell(12).setCellValue(dateFormat.format(offer.getContractPeriodEnd()));
            row.createCell(13).setCellValue(offer.getRecruiterOwner().getUsername());
            row.createCell(14).setCellValue(offer.getStatus());
            row.createCell(15).setCellValue(dateFormat.format(offer.getCreatedAt()));
            row.createCell(16).setCellValue(dateFormat.format(offer.getUpdatedAt()));
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Offer> getOffersWaiting() {
        return offerRepository.findOffersWaiting();
    }

    private Offer convertToEntity(OfferDto offerDto) throws ParseException {
        Offer offer = new Offer();
        offer.setCandidate(candidateRepository.findById(offerDto.getCandidateId()).orElse(null));
        offer.setContractType(offerDto.getContractType());
        offer.setPosition(offerDto.getPosition());
        offer.setLevel(offerDto.getLevel());
        offer.setApprover(userRepository.findById(offerDto.getApproverId()).orElse(null));
        offer.setDepartment(offerDto.getDepartment());
        InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(offerDto.getCandidateId()).orElse(null);
        offer.setInterviewInfo(interviewSchedule);
        offer.setRecruiterOwner(userRepository.findById(offerDto.getRecruiterOwnerId()).orElse(null));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        offer.setContractPeriodStart(new Timestamp(dateFormat.parse(offerDto.getContractPeriodStart()).getTime()));
        offer.setContractPeriodEnd(new Timestamp(dateFormat.parse(offerDto.getContractPeriodEnd()).getTime()));

        offer.setDueDate(new Timestamp(dateFormat.parse(offerDto.getDueDate()).getTime()));
        offer.setInterviewNote(offerDto.getInterviewNote());
        offer.setBasicSalary(offerDto.getBasicSalary());
        offer.setNote(offerDto.getNote());
        offer.setStatus("Waiting for Approval");
        return offer;
    }
}
