package edu.du.campusflow.service;


import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Tuition;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.repository.TuitionRepository;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.TuitionTargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TuitionService {

    private final TuitionRepository tuitionRepository;
    private final TuitionTargetRepository tuitionTargetRepository;
    private final MemberService memberService; // MemberService 주입
    private final MemberRepository memberRepository;
    private final CommonCodeRepository commonCodeRepository;


    public TuitionService(TuitionRepository tuitionRepository, TuitionTargetRepository tuitionTargetRepository, MemberService memberService, MemberRepository memberRepository, CommonCodeRepository commonCodeRepository) {
        this.tuitionRepository = tuitionRepository;
        this.tuitionTargetRepository = tuitionTargetRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.commonCodeRepository = commonCodeRepository;
    }

    public List<Member> getMemberServiceInfo(CommonCode academicStatus) {
        // 1. CommonCode에서 학적 상태를 가져옵니다.
        List<Member> enrolledMembers = memberRepository.findByAcademicStatus(commonCodeRepository.findByCodeValue("ENROLLED"));
        List<Member> returnFromLeaveMembers = memberRepository.findByAcademicStatus(commonCodeRepository.findByCodeValue("RETURN_FROM_LEAVE"));
        List<Member> returnedMembers = memberRepository.findByAcademicStatus(commonCodeRepository.findByCodeValue("RETURNED"));

// 세 리스트를 합치기
        List<Member> combinedList = new ArrayList<>();
        combinedList.addAll(enrolledMembers);
        combinedList.addAll(returnFromLeaveMembers);
        combinedList.addAll(returnedMembers);

        List<String> validStatuses = List.of("ENROLLED", "RETURN_FROM_LEAVE", "RETURNED");

        // 2. 해당 상태를 가진 학생들을 조회합니다.
        List<Member> students = memberService.findAll(); // 모든 학생을 가져오는 메서드 호출
        return students.stream()
                .filter(student -> validStatuses.contains(student.getAcademicStatus())) // 필터링
                .collect(Collectors.toList()); // 리스트로 수집
    }

    @Transactional
    public TuitionDTO getTuitionInfo(Member student) {
        Tuition tuition = tuitionRepository.findByDeptId(student.getDept());
        if (tuition == null) {
            throw new RuntimeException("등록금 정보를 찾을 수 없습니다.");
        }


        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);

        Integer paidAmount = null;
        LocalDateTime paidDate = null;

        if (tuitionTarget== null) {

            // paidAmount와 paidDate 가져오기
           paidAmount = tuitionTarget.getPaidAmount();
           paidDate = tuitionTarget.getPaidDate();
            // 원하는 로직 수행
        } else {
            // targetId에 해당하는 TuitionTarget을 찾지 못한 경우 처리
        }







        return TuitionDTO.builder()
                .targetId(tuition.getTuitionId())
                .memberId(student.getMemberId())
                .memberName(student.getName())
                .department(student.getDept().getDeptName())
                .amount(tuition.getAmount())
                .paidAmount(tuitionTarget.getPaidAmount())
                .paidDate(tuitionTarget.getPaidDate() != null ? tuitionTarget.getPaidDate().toString() : null)
                .paymentStatus(tuitionTarget.isPaymentStatus())
                .build();
    }

}
