package edu.du.campusflow.service;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Tuition;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.repository.TuitionRepository;
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
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CommonCodeRepository commonCodeRepository;

    public TuitionService(TuitionRepository tuitionRepository, TuitionTargetRepository tuitionTargetRepository,
                          MemberService memberService, MemberRepository memberRepository,
                          CommonCodeRepository commonCodeRepository) {
        this.tuitionRepository = tuitionRepository;
        this.tuitionTargetRepository = tuitionTargetRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.commonCodeRepository = commonCodeRepository;
    }

    public List<Member> getMemberServiceInfo() {
        // 1. 학적 상태별로 학생 목록 가져오기
        List<Member> enrolledMembers = memberRepository.findByAcademicStatus(commonCodeRepository.findByCodeValue("ENROLLED"));
        List<Member> returnFromLeaveMembers = memberRepository.findByAcademicStatus(commonCodeRepository.findByCodeValue("RETURN_FROM_LEAVE"));
        List<Member> returnedMembers = memberRepository.findByAcademicStatus(commonCodeRepository.findByCodeValue("RETURNED"));

        // 2. 세 리스트를 합치기
        List<Member> combinedList = new ArrayList<>();
        combinedList.addAll(enrolledMembers);
        combinedList.addAll(returnFromLeaveMembers);
        combinedList.addAll(returnedMembers);

        return combinedList;
    }

    @Transactional
    public TuitionDTO getTuitionInfo(Member student) {
        Tuition tuition = tuitionRepository.findByDeptId(student.getDept());
        if (tuition == null) {
            throw new RuntimeException("등록금 정보를 찾을 수 없습니다.");
        }

        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);

        Integer paidAmount = tuitionTarget != null ? tuitionTarget.getPaidAmount() : 0;
        LocalDateTime paidDate = tuitionTarget != null ? tuitionTarget.getPaidDate() : null;
        boolean paymentStatus = tuitionTarget != null && tuitionTarget.isPaymentStatus();

        return TuitionDTO.builder()
                .targetId(tuition.getTuitionId())
                .memberId(student.getMemberId())
                .memberName(student.getName())
                .deptId(student.getDept().getDeptId())
                .amount(tuition.getAmount())
                .paidAmount(paidAmount)
//                .paidDate(paidDate != null ? paidDate.date() : null)
                .paymentStatus(paymentStatus)
                .build();
    }

    @Transactional
    public void updatePaymentStatus(Member student, boolean status) {
        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);
        if (tuitionTarget == null) {
            throw new RuntimeException("해당 학생의 등록금 대상 정보를 찾을 수 없습니다.");
        }
        tuitionTarget.setPaymentStatus(status);
        tuitionTargetRepository.save(tuitionTarget);
    }

    public List<TuitionDTO> findAllTuitionDTO() {
        return tuitionTargetRepository.findAll()
                .stream()
                .map(tuitionTarget -> TuitionDTO.builder()
                        .targetId(tuitionTarget.getTargetId())
                        .memberId(tuitionTarget.getMember().getMemberId())
                        .memberName(tuitionTarget.getMember().getName())
                        .deptId(tuitionTarget.getMember().getDept().getDeptId())
                        .amount(tuitionTarget.getPaidAmount())
                        .paidAmount(tuitionTarget.getPaidAmount())
                        .paidDate(tuitionTarget.getPaidDate())
                        .paymentStatus(tuitionTarget.isPaymentStatus())
                        .newPaymentStatus(false) // 기본값 설정
                        .build())
                .collect(Collectors.toList());
    }
}
