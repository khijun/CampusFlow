package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.enums.InquiryStatus;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InquiryService {
    @Autowired
    private InquiryRepository inquiryRepository;
    @Autowired
    private CommonCodeRepository commonCodeRepository;

    // 모든 문의 조회
    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    // 특정 문의 조회
    public Inquiry getInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).orElse(null);
    }

    // 문의 생성
    public Inquiry createInquiry(Inquiry inquiry) {
        inquiry.setCreatedAt(LocalDateTime.now());

        // CommonCode를 생성하고 ENUM을 문자열로 변환하여 설정
        CommonCode commonCode = new CommonCode();
        commonCode.setCodeValue(InquiryStatus.PENDING.getValue()); // ENUM을 문자열로 변환하여 사용
        inquiry.setInquiryStatus(commonCode); // inquiryStatus에 설정

        return inquiryRepository.save(inquiry);
    }

    // 문의 업데이트
    public Inquiry updateInquiry(Long inquiryId, Inquiry inquiry) {
        Inquiry existingInquiry = getInquiryById(inquiryId);
        if (existingInquiry != null) {
            existingInquiry.setSubject(inquiry.getSubject());
            existingInquiry.setContent(inquiry.getContent());
            existingInquiry.setUpdatedAt(LocalDateTime.now());
            return inquiryRepository.save(existingInquiry);
        }
        return null;
    }

    // 문의 삭제
    public void deleteInquiry(Long inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

    // 문의에 대한 답변 추가
    public Inquiry addResponse(Long inquiryId, Inquiry response) {
        Inquiry existingInquiry = getInquiryById(inquiryId);
        if (existingInquiry != null) {
            response.setResponseTo(existingInquiry);
            response.setCreatedAt(LocalDateTime.now());
            return inquiryRepository.save(response);
        }
        return null;
    }


}