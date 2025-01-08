package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.entity.Member;
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
    @Autowired
    private AuthService authService;


    // 모든 문의 조회
    public List<Inquiry> getAllInquiries() {
        List<Inquiry> inquiries = inquiryRepository.findAll();

        // 기본 상태 코드 조회
        CommonCode defaultStatus = commonCodeRepository.findByCodeValue("INQUIRYSTATUS");
        if (defaultStatus == null) {
            throw new RuntimeException("기본 상태 코드를 찾을 수 없습니다.");
        }

        // 각 문의사항의 상태 확인 및 설정
        for (Inquiry inquiry : inquiries) {
            try {
                if (inquiry.getInquiryStatus() == null) {
                    inquiry.setInquiryStatus(defaultStatus);
                    inquiryRepository.save(inquiry);
                }
            } catch (EntityNotFoundException e) {
                inquiry.setInquiryStatus(defaultStatus);
                inquiryRepository.save(inquiry);
            }
        }

        return inquiries;
    }

    public Inquiry getInquiryById(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("문의사항을 찾을 수 없습니다."));

        // 문의 상태가 null인 경우 기본값 설정
        if (inquiry.getInquiryStatus() == null) {
            CommonCode defaultStatus = commonCodeRepository.findByCodeValue(InquiryStatus.AWAITING.name());
            if (defaultStatus == null){
                new RuntimeException("기본 상태 코드를 찾을 수 없습니다.");
            }
            inquiry.setInquiryStatus(defaultStatus);
            inquiryRepository.save(inquiry);
        }

        return inquiry;
    }
    //작성자 확인
    public boolean isAuthor(Inquiry inquiry) {
        var currentMember = authService.getCurrentMember();
        if (currentMember == null || inquiry.getMember() == null) {
            return false;
        }
        return inquiry.getMember().getMemberId().equals(currentMember.getMemberId());
    }

    // 교직원 여부 확인
    public boolean isStaff() {
        var currentMember = authService.getCurrentMember();
        if (currentMember == null || currentMember.getMemberType() == null) {
            return false;
        }
        return "STAFF".equals(currentMember.getMemberType().getCodeValue());
    }



   // 문의 생성
    public Inquiry createInquiry(Inquiry inquiry) {
        // 현재 로그인한 사용자 정보 가져오기
        Member currentMember = authService.getCurrentMember();
        if (currentMember == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 현재 시간 설정
        inquiry.setCreatedAt(LocalDateTime.now());

        // 작성자 설정
        inquiry.setMember(currentMember);

        // 기존의 AWAITING 상태 코드를 찾아서 사용
        CommonCode awaitingStatus = commonCodeRepository.findByCodeValue("AWAITING");
        if (awaitingStatus == null) {
            throw new RuntimeException("대기 상태 코드를 찾을 수 없습니다.");
        }
        inquiry.setInquiryStatus(awaitingStatus);

        return inquiryRepository.save(inquiry);
    }


    // 문의 완료 처리
    public void completeInquiry(Long inquiryId) {
        Inquiry inquiry = getInquiryById(inquiryId);

        if (!isAuthor(inquiry)) {
            throw new IllegalStateException("작성자만 완료 처리할 수 있습니다.");
        }

        if (!"IN_PROGRESS".equals(inquiry.getInquiryStatus().getCodeValue())) {
            throw new IllegalStateException("처리중 상태의 문의사항만 완료 처리가 가능합니다.");
        }

        CommonCode completedStatus = commonCodeRepository.findByCodeValue(InquiryStatus.PROCESSED.name());
        if (completedStatus == null) {
             throw new RuntimeException("완료 상태 코드를 찾을 수 없습니다.");
        }

        inquiry.setInquiryStatus(completedStatus);
        inquiryRepository.save(inquiry);
    }


    // 문의 삭제
    public void deleteInquiry(Long inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

//    @Override
    public Inquiry addResponse(Long inquiryId, Inquiry response) {
        if (!isStaff()) {
            throw new IllegalStateException("교직원만 답변을 등록할 수 있습니다.");
        }

        Inquiry existingInquiry = getInquiryById(inquiryId);
        if (existingInquiry.getRelatedInquiry() != null) {
            throw new IllegalStateException("이미 답변이 등록된 문의사항입니다.");
        }

        response.setRelatedInquiry(existingInquiry);
        response.setCreatedAt(LocalDateTime.now());
        return inquiryRepository.save(response);
    }
}

