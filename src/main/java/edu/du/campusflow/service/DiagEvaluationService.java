package edu.du.campusflow.service;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.entity.DiagItem;
import edu.du.campusflow.entity.DiagQuestion;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiagEvaluationService {
    private final DiagEvaluationRepository diagEvaluationRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final DiagQuestionRepository diagQuestionRepository;
    private final DiagItemRepository diagItemRepository;
    private final DeptRepository deptRepository;
    private final AuthService authService;

    @Transactional
    public List<DiagEvaluationDetailDTO> searchEvaluationsByDeptAndGrade(Long deptId, String grade) {
        log.info("학과/학년별 진단평가 조회 시작 - deptId: {}, grade: {}", deptId, grade);

        // 학년에 따른 코드ID 매핑
        Long gradeCodeId;
        if ("1".equals(grade)) {
            gradeCodeId = 97L;  // 1학년
        } else if ("2".equals(grade)) {
            gradeCodeId = 98L;  // 2학년
        } else if ("3".equals(grade)) {
            gradeCodeId = 99L;  // 3학년
        } else if ("4".equals(grade)) {
            gradeCodeId = 100L; // 4학년
        } else {
            throw new IllegalArgumentException("Invalid grade: " + grade);
        }

        // findEvaluationsByDeptAndGrade를 findEvaluations로 변경
        List<DiagEvaluationDetailDTO> results = diagEvaluationRepository
                .findEvaluations(deptId, gradeCodeId, null, null);  // lectureName과 studentName은 null

        log.info("조회된 결과 수: {}", results.size());
        return results;
    }


    // 교수의 강의 목록 조회 메서드 추가
    @Transactional
    public List<Map<String, Object>> getProfessorLectures() {
        Member currentMember = authService.getCurrentMember();
        List<Ofregistration> registrations = ofregistrationRepository
                .findByLectureId_Member_MemberId(currentMember.getMemberId());

        return registrations.stream()
                .map(reg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ofregistrationId", reg.getId());
                    map.put("lectureName", reg.getLectureId().getLectureName());
                    map.put("semester", reg.getLectureId().getSemester().getCodeName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Map<String, Object>> getAllLectures() {
        List<Ofregistration> registrations = ofregistrationRepository.findAll();

        return registrations.stream()
                .map(reg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ofregistrationId", reg.getId());
                    map.put("lectureName", reg.getLectureId().getLectureName());
                    map.put("professorName", reg.getLectureId().getMember().getName());
                    map.put("semester", reg.getLectureId().getSemester().getCodeName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DiagQuestionDTO> getDiagnosticResults(Long ofregistrationId) {
        Ofregistration ofregistration = ofregistrationRepository.findById(ofregistrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다."));

        List<DiagQuestion> questions = diagQuestionRepository.findAll();
        List<DiagItem> diagItems = diagItemRepository.findByOfRegistration_Id(ofregistrationId);  // diagItemRepository 사용

        return questions.stream()
                .map(question -> {
                    DiagQuestionDTO dto = new DiagQuestionDTO();
                    dto.setQuestionId(question.getQuestionId());
                    dto.setQuestionName(question.getQuestionName());
                    dto.setLectureName(ofregistration.getLectureId().getLectureName());
                    dto.setName(ofregistration.getLectureId().getMember().getName());
                    dto.setSemester(ofregistration.getLectureId().getSemester().getCodeName());
                    dto.setSubjectId(ofregistration.getLectureId().getCurriculumSubject().getSubject().getSubjectId());

                    // 점수 초기화
                    dto.initializeScoreCounts();  // 추가

                    // 해당 문항에 대한 답변들 처리
                    diagItems.stream()
                            .filter(item -> item.getDiagQuestion().getQuestionId().equals(question.getQuestionId()))
                            .forEach(item -> dto.incrementScoreCount(item.getScore()));

                    dto.calculateAverageScore();  // DiagQuestionDTO에 이 메서드가 있어야 함
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DiagEvaluationDetailDTO> searchEvaluations(
            Long deptId, String grade, String lectureName, String studentName) {

        Long gradeCodeId = getGradeCodeId(grade);

        List<DiagEvaluationDetailDTO> results = diagEvaluationRepository
                .findEvaluations(deptId, gradeCodeId, lectureName, studentName);

        return results;
    }

    private Long getGradeCodeId(String grade) {
        Long gradeCodeId;
        switch (grade) {
            case "1":
                gradeCodeId = 97L;
                break;
            case "2":
                gradeCodeId = 98L;
                break;
            case "3":
                gradeCodeId = 99L;
                break;
            case "4":
                gradeCodeId = 100L;
                break;
            default:
                throw new IllegalArgumentException("Invalid grade: " + grade);
        }
        return gradeCodeId;
    }

    // getAllDepartments는 한 번만 정의
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