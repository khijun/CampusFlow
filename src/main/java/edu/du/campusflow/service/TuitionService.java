package edu.du.campusflow.service;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.repository.TuitionRepository;
import edu.du.campusflow.repository.TuitionTargetRepository;
import edu.du.campusflow.dto.TuitionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.hibernate.Hibernate;

@Service
@RequiredArgsConstructor
public class TuitionService {
    private final TuitionRepository tuitionRepository;
    private final TuitionTargetRepository tuitionTargetRepository;
    private final MemberService memberService;

    /**
     * 회원 ID로 등록금 정보를 조회하여 DTO로 변환
     * @param memberId 조회할 회원의 ID
     * @return 등록금 정보가 담긴 DTO 객체
     * @throws RuntimeException 등록금 대상자 정보가 없는 경우
     */
    public TuitionDTO getTuitionDTOByMemberId(Long memberId) {
        Member member = memberService.findByMemberId(memberId);
        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(member)
            .orElseThrow(() -> new RuntimeException("등록금 대상자 정보를 찾을 수 없습니다."));

        // Hibernate 프록시 객체 초기화
        Hibernate.initialize(tuitionTarget.getTuitionId());
        Hibernate.initialize(tuitionTarget.getMember());
        Hibernate.initialize(tuitionTarget.getMember().getDept());

        return TuitionDTO.builder()
            .targetId(tuitionTarget.getTargetId())
            .memberId(member.getMemberId())
            .memberName(member.getName())
            .department(member.getDept().getDeptName())
            .amount(tuitionTarget.getTuitionId().getAmount())
            .paidAmount(tuitionTarget.getPaidAmount().intValue())
            .paidDate(tuitionTarget.getPaidDate())
            .paymentStatus(tuitionTarget.isPaymentStatus())
            .build();
    }

    /**
     * 페이징 처리된 등록금 대상자 검색 메서드
     * @param studentId 학번
     * @param studentName 학생 이름
     * @param department 학과명
     * @param pageable 페이징 정보
     * @return 페이징된 등록금 대상자 DTO 목록
     */
    public Page<TuitionDTO> searchTuitionTargets(
            String studentId, 
            String studentName, 
            String department, 
            Pageable pageable) {
            
        Page<TuitionTarget> targetPage;
        
        // 검색 조건이 있는 경우와 없는 경우를 구분하여 처리
        if (StringUtils.hasText(studentId) || StringUtils.hasText(studentName) || StringUtils.hasText(department)) {
            targetPage = tuitionTargetRepository.findBySearchCriteria(
                studentId, 
                studentName, 
                department, 
                pageable
            );
        } else {
            targetPage = tuitionTargetRepository.findAll(pageable);
        }
        
        // Entity를 DTO로 변환하여 반환
        return targetPage.map(this::convertToDTO);
    }

    /**
     * Entity를 DTO로 변환하는 private 메서드
     */
    private TuitionDTO convertToDTO(TuitionTarget target) {
        return TuitionDTO.builder()
                .targetId(target.getTargetId())
                .memberId(target.getMember().getMemberId())
                .memberName(target.getMember().getName())
                .department(target.getMember().getDept().getDeptName())
                .amount(target.getTuitionId().getAmount())
                .paidAmount(target.getPaidAmount())
                .paidDate(target.getPaidDate())
                .paymentStatus(target.isPaymentStatus())
                .build();
    }
}