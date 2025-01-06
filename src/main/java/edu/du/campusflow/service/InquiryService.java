package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.enums.InquiryStatus;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
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

    @Autowired
    private CommonCodeGroupRepository codeGroupRepository;

    // 모든 문의 조회 (답변 제외)
    public List<Inquiry> getAllInquiries() {
        // responseTo가 null인 것만 가져옴 (원본 문의만)
        return inquiryRepository.findByResponseToIsNull();
    }

    // 특정 문의 조회
    public Inquiry getInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).orElse(null);
    }

    // 문의 생성
    public Inquiry createInquiry(Inquiry inquiry) {
        // CommonCode에서 AWAITING 상태 가져오기 또는 생성
        CommonCode awaitingStatus = commonCodeRepository
            .findByCodeValueAndCodeGroup_GroupCode("AWAITING", "INQUIRY_STATUS")
            .orElseGet(() -> createInquiryStatusCode());
        
        inquiry.setInquiryStatus(awaitingStatus);
        inquiry.setCreatedAt(LocalDateTime.now());
        
        return inquiryRepository.save(inquiry);
    }

    private CommonCode createInquiryStatusCode() {
        // 코드 그룹 생성
        CommonCodeGroup group = codeGroupRepository
            .findByGroupCode("INQUIRY_STATUS")
            .orElseGet(() -> {
                CommonCodeGroup newGroup = new CommonCodeGroup();
                newGroup.setGroupCode("INQUIRY_STATUS");
                newGroup.setGroupName("문의상태");
                newGroup.setGroupDescription("문의 처리 상태 코드");
                return codeGroupRepository.save(newGroup);
            });

        // AWAITING 상태 코드 생성
        createStatusIfNotExists("AWAITING", "대기", "대기상태", group);
        createStatusIfNotExists("IN_PROGRESS", "처리중", "답변 작성됨", group);
        createStatusIfNotExists("COMPLETED", "처리완료", "확인 완료됨", group);

        return commonCodeRepository.findByCodeValueAndCodeGroup_GroupCode("AWAITING", "INQUIRY_STATUS")
            .orElseThrow(() -> new RuntimeException("상태 코드 생성 실패"));
    }

    private CommonCode createStatusIfNotExists(String codeValue, String codeName, String description, CommonCodeGroup group) {
        return commonCodeRepository.findByCodeValueAndCodeGroup_GroupCode(codeValue, "INQUIRY_STATUS")
            .orElseGet(() -> {
                CommonCode status = new CommonCode();
                status.setCodeValue(codeValue);
                status.setCodeName(codeName);
                status.setCodeDescription(description);
                status.setCodeGroup(group);
                return commonCodeRepository.save(status);
            });
    }

    // 문의 삭제
    public void deleteInquiry(Long inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

    // 문의에 대한 답변 추가 (상태 변경 포함)
    public Inquiry addResponse(Long inquiryId, String content, boolean isStaff) {
        if (!isStaff) {
            throw new RuntimeException("답변 권한이 없습니다.");
        }

        Inquiry parentInquiry = getInquiryById(inquiryId);
        if (parentInquiry == null) {
            throw new RuntimeException("원본 문의를 찾을 수 없습니다.");
        }

        // 상태를 처리중으로 변경
        CommonCode inProgressStatus = commonCodeRepository
            .findByCodeValueAndCodeGroup_GroupCode("IN_PROGRESS", "INQUIRY_STATUS")
            .orElseGet(() -> createInquiryStatusCode());
        parentInquiry.setInquiryStatus(inProgressStatus);
        inquiryRepository.save(parentInquiry);

        // 답변 생성
        Inquiry response = new Inquiry();
        response.setContent(content);
        response.setCreatedAt(LocalDateTime.now());
        response.setResponseTo(parentInquiry);
        response.setInquiryStatus(inProgressStatus);
        
        return inquiryRepository.save(response);
    }

    // 문의 처리완료로 상태 변경
    public Inquiry completeInquiry(Long inquiryId, boolean isAuthor) {
        if (!isAuthor) {
            throw new RuntimeException("권한이 없습니다.");
        }

        Inquiry inquiry = getInquiryById(inquiryId);
        if (inquiry == null) {
            throw new RuntimeException("문의를 찾을 수 없습니다.");
        }

        CommonCode completedStatus = commonCodeRepository
            .findByCodeValueAndCodeGroup_GroupCode("COMPLETED", "INQUIRY_STATUS")
            .orElseGet(() -> createInquiryStatusCode());
        
        inquiry.setInquiryStatus(completedStatus);
        return inquiryRepository.save(inquiry);
    }
}