package edu.du.campusflow.service;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.repository.DeptRepository;
import edu.du.campusflow.repository.LecQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LecQuestionService {

    private final DeptRepository deptRepository;
    private final LecQuestionRepository lecQuestionRepository;

    @Transactional
    public List<LecQuestionDTO> searchEvaluations(
            Long deptId, String grade, String lectureName, String name) {

        log.info("Search params - deptId: {}, grade: {}, lectureName: {}, name: {}",
                deptId, grade, lectureName, name);

        Long gradeCodeId = getGradeCodeId(grade);  // 학년을 코드 ID로 변환

        return lecQuestionRepository.findEvaluations(
                deptId,
                gradeCodeId,
                lectureName,
                name
        );
    }

    private Long getGradeCodeId(String grade) {
        switch (grade) {
            case "1": return 97L;  // 1학년
            case "2": return 98L;  // 2학년
            case "3": return 99L;  // 3학년
            case "4": return 100L; // 4학년
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }

    public List<Map<String, Object>> getAllDepartments() {
        return deptRepository.findAll().stream()
                .map(dept -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("deptId", dept.getDeptId());
                    map.put("deptName", dept.getDeptName());
                    return map;
                })
                .collect(Collectors.toList());
    }
}