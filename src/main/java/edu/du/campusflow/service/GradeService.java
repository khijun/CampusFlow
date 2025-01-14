package edu.du.campusflow.service;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Completion;
import edu.du.campusflow.entity.Grade;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;


    public List<GradeDTO> getGroupedGradesByRole(Long memberId, List<Long> gradeTypeList) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        var memberType = member.getMemberType().getCodeId();
        List<Grade> grades;

        if (memberType == 101L) { // 학생
            grades = gradeRepository.findByMemberIdAndGradeTypes(memberId, gradeTypeList);
        } else if (memberType == 102L || memberType == 103L) { // 교수 또는 교직원
            var lectureIds = lectureRepository.findLectureIdsByMemberId(memberId);
            grades = gradeRepository.findByLectureIdsAndGradeTypes(lectureIds, gradeTypeList);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 데이터 그룹화
        Map<String, List<Grade>> groupedGrades = grades.stream()
                .collect(Collectors.groupingBy(grade ->
                        grade.getCompletion().getOfRegistration().getLectureId().getMember().getName()
                                + ":" + grade.getCompletion().getOfRegistration().getLectureId().getLectureName()
                ));

        // 그룹화된 데이터를 DTO로 변환
        List<GradeDTO> result = groupedGrades.entrySet().stream()
                .map(entry -> {
                    String[] keys = entry.getKey().split(":");
                    String professorName = keys[0];
                    String lectureName = keys[1];

                    Map<String, Integer> scores = new HashMap<>();
                    int totalScore = 0;

                    // 성적 정보를 scores에 추가 및 가중치 적용
                    for (Grade grade : entry.getValue()) {
                        scores.put(grade.getGradeType().getCodeName(), grade.getScore());
                    }

                    // 총점 계산
                    int finalTotalScore = calculateTotalScore(entry.getValue());

                    // Completion 테이블에서 finalGrade를 바로 가져오기
                    String finalGrade = entry.getValue().get(0).getCompletion().getFinalGrade().getCodeName();

                    return new GradeDTO(professorName, lectureName, scores, finalGrade,finalTotalScore);
                })
                .collect(Collectors.toList());

        return result;
    }









    private int calculateTotalScore(List<Grade> grades) {
        int totalScore = 0;

        // 각 성적 항목(중간, 기말, 과제, 출석)을 더함
        for (Grade grade : grades) {
            Long codeId = grade.getGradeType().getCodeId();  // 성적 항목의 codeId

            switch (codeId.intValue()) {
                case 67: // 중간
                    totalScore += grade.getScore();
                    break;
                case 68: // 기말
                    totalScore += grade.getScore();
                    break;
                case 69: // 과제
                    totalScore += grade.getScore();
                    break;
                case 70: // 출석
                    totalScore += grade.getScore();
                    break;
                default:
                    throw new IllegalArgumentException("알 수 없는 성적 항목 코드입니다: " + codeId);
            }
        }

        return totalScore;
    }

}