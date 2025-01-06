package edu.du.campusflow.service;


import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.Tuition;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.repository.TuitionRepository;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.TuitionTargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TuitionService {

    private final TuitionRepository tuitionRepository;
    private final TuitionTargetRepository tuitionTargetRepository;


    public TuitionService(TuitionRepository tuitionRepository, TuitionTargetRepository tuitionTargetRepository) {
        this.tuitionRepository = tuitionRepository;
        this.tuitionTargetRepository = tuitionTargetRepository;
    }

    @Transactional
    public TuitionDTO getTuitionInfo(Member student) {
        Tuition tuition = tuitionRepository.findByDeptId(student.getDept());
        if (tuition == null) {
            throw new RuntimeException("등록금 정보를 찾을 수 없습니다.");
        }


        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(student);

        if (tuitionTarget== null) {

            // paidAmount와 paidDate 가져오기
            Integer paidAmount = tuitionTarget.getPaidAmount();
            LocalDateTime paidDate = tuitionTarget.getPaidDate();
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
