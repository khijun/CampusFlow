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
        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);
        if (tuitionTarget == null) {
            throw new RuntimeException("해당 학생의 등록금 정보를 찾을 수 없습니다.");
        }

        return TuitionDTO.builder()
                .targetId(tuitionTarget.getTargetId())
                .memberId(tuitionTarget.getMember().getMemberId())
                .memberName(tuitionTarget.getMember().getName())
                .deptName(tuitionTarget.getMember().getDept().getDeptName())
                .amount(tuitionTarget.getTuitionId().getAmount())
                .paidAmount(tuitionTarget.getPaidAmount())
                .paidDate(tuitionTarget.getPaidDate())
                .paymentStatus(tuitionTarget.isPaymentStatus())
                .build();
    }

    @Transactional
    public void updatePaymentStatus(Member student, boolean status) {
        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);
        if (tuitionTarget == null) {
            throw new RuntimeException("해당 학생의 등록금 대상 정보를 찾을 수 없습니다.");
        }
        tuitionTarget.setPaymentStatus(status);
        if (status) {
            // 납부 상태가 true일 때 현재 시간을 paidDate에 설정
            tuitionTarget.setPaidDate(LocalDateTime.now());
        } else {
            // 납부 상태가 false일 때 paidDate를 null로 설정
            tuitionTarget.setPaidDate(null);  
        }
        tuitionTargetRepository.save(tuitionTarget);
    }

    @Transactional
    public void updatePaidAmount(Member student, Integer paidAmount) {
        if (paidAmount == null || paidAmount < 0) {
            throw new IllegalArgumentException("지불 금액은 0 이상이어야 합니다.");
        }

        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);
        if (tuitionTarget == null) {
            throw new RuntimeException("해당 학생의 등록금 정보를 찾을 수 없습니다.");
        }
        tuitionTarget.setPaidAmount(paidAmount);
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
                        .deptName(tuitionTarget.getMember().getDept().getDeptName())
                        .amount(tuitionTarget.getTuitionId().getAmount())
                        .paidAmount(tuitionTarget.getPaidAmount())
                        .paidDate(tuitionTarget.getPaidDate())
                        .paymentStatus(tuitionTarget.isPaymentStatus())
                        .newPaymentStatus(false) // 기본값 설정
                        .build())
                .collect(Collectors.toList());
    }

}
