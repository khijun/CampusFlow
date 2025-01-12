package edu.du.campusflow;

import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import edu.du.campusflow.service.ChangeHistoryService;
import edu.du.campusflow.service.ChangeRequestService;
import edu.du.campusflow.service.InfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class HjinTest {


    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @Autowired
    private ChangeHistoryRepository changeHistoryRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository;


    private Member member;
    private CommonCode beforeCode;
    private CommonCode afterCode;
    private CommonCode academicStatus;
    private CommonCode applicationStatus;
    @Autowired
    private ChangeRequestService changeRequestService;
    @Autowired
    private EducationInfoRepository educationInfoRepository;

    @Autowired
    private InfoService infoService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChangeHistoryService changeHistoryService;

//    @Test
//    @Transactional
//    @Commit
//    void testProcessChangeRequest() {
//        // 1. 테스트 데이터 준비
//        Long memberId = 2405002L; // 테스트할 멤버 ID
//        Long beforeCodeId = 2L;  // 변경 전 코드
//        Long afterCodeId = 1L;   // 변경 후 코드
//        Long academicStatusCodeId = 1L;
//        Long applicationStatusCodeId = 12L;
//        Long gradeCodeId = 97L;   // 학년 코드 ID
//
//        // CommonCode 엔티티 생성
//        CommonCode beforeCode = commonCodeRepository.findById(beforeCodeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid before code ID: " + beforeCodeId));
//        CommonCode afterCode = commonCodeRepository.findById(afterCodeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid after code ID: " + afterCodeId));
//        CommonCode academicStatusId = commonCodeRepository.findById(academicStatusCodeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid after code ID: " + academicStatusCodeId));
//        CommonCode applicationStatusId = commonCodeRepository.findById(applicationStatusCodeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid after code ID: " + applicationStatusCodeId));
//        // Member 객체 생성 후 세터로 memberId 설정
//        Member member = new Member();
//        member.setMemberId(memberId); // 세터 사용
//
//        // ChangeRequest 엔티티 생성
//        ChangeRequest changeRequest = ChangeRequest.builder()
//                .member(member)  // Member 객체 설정
//                .beforeCode(beforeCode)
//                .afterCode(afterCode)
//                .academicStatus(academicStatusId)
//                .applicationStatus(applicationStatusId)
//                .build();
//
//        // 2. 로직 실행
//        changeRequestService.processChangeRequest(changeRequest, gradeCodeId);
//
//        // 3. 저장된 ChangeRequest 조회 후 출력
//        List<ChangeRequest> savedRequests = changeRequestRepository.findByMember_MemberId(memberId);
//        System.out.println("Saved ChangeRequest: " + savedRequests);
//
//        // 4. 저장된 ChangeHistory 조회 후 출력
//        List<ChangeHistory> savedHistories = changeHistoryRepository.findAll();
//        System.out.println("Saved ChangeHistory: " + savedHistories);
//    }




}
