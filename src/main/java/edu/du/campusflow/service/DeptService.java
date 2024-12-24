package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.enums.DeptStatus;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeptService {
    private final DeptRepository deptRepository;
    private final CommonCodeRepository commonCodeRepository;

    @Transactional
    public void createDummyDept() {
        // 각 상태에 대한 CommonCode 객체를 가져옴
        CommonCode active = commonCodeRepository.findByCodeValue(DeptStatus.ACTIVE.toString());
        CommonCode inactive = commonCodeRepository.findByCodeValue(DeptStatus.INACTIVE.toString());
        CommonCode pending = commonCodeRepository.findByCodeValue(DeptStatus.PENDING.toString());

        // 10개의 학과 데이터 생성 및 저장
        deptRepository.save(Dept.builder()
                .deptName("컴퓨터공학과")
                .maxStudents(100)
                .deptStatus(active)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("기계공학과")
                .maxStudents(150)
                .deptStatus(inactive)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("전기전자공학과")
                .maxStudents(120)
                .deptStatus(pending)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("토목공학과")
                .maxStudents(80)
                .deptStatus(active)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("화학공학과")
                .maxStudents(60)
                .deptStatus(inactive)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("경영학과")
                .maxStudents(200)
                .deptStatus(pending)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("경제학과")
                .maxStudents(180)
                .deptStatus(active)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("심리학과")
                .maxStudents(90)
                .deptStatus(inactive)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("영어영문학과")
                .maxStudents(70)
                .deptStatus(pending)
                .build());

        deptRepository.save(Dept.builder()
                .deptName("사회학과")
                .maxStudents(50)
                .deptStatus(active)
                .build());
    }

}
