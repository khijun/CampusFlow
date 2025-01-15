package edu.du.campusflow.service;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.dto.GradeForm;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final CompletionRepository completionRepository;



    public List<GradeDTO> getGroupedGradesByRole(Long memberId, List<Long> gradeTypeList) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Long memberType = member.getMemberType().getCodeId();
        List<Grade> grades;

        if (memberType == 101L) { // 학생
            grades = gradeRepository.findByMemberIdAndGradeTypes(memberId, gradeTypeList);
        } else if (memberType == 102L || memberType == 103L) { // 교수 또는 교직원
            List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(memberId);
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

                    // finalGrade를 calculateFinalGrade 메서드로 계산
                    Long finalGradeCode = calculateFinalGrade(finalTotalScore);

                    // CommonCode에서 finalGradeCode로 성적 이름 가져오기
                    CommonCode finalGrade = commonCodeRepository.findById(finalGradeCode)
                            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 성적 코드입니다."));

                    // Completion 엔티티의 finalGrade 설정
                    Completion completion = entry.getValue().get(0).getCompletion();
                    completion.setFinalGrade(finalGrade);  // 성적 코드 저장

                    // Completion 엔티티 저장 (필요시 DB에 반영)
                    completionRepository.save(completion);  // DB에 반영 // finalGrade 코드로 성적 이름을 얻어옴

                    return new GradeDTO(professorName, lectureName, scores, finalGrade.getCodeName(), finalTotalScore);
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

    // 총점에 따라 finalGrade를 계산
    private Long calculateFinalGrade(int totalScore) {
        if (totalScore >= 95) {
            return 43L; // A+
        } else if (totalScore >= 90) {
            return 44L; // A
        } else if (totalScore >= 85) {
            return 45L; // B+
        } else if (totalScore >= 80) {
            return 46L; // B
        } else if (totalScore >= 75) {
            return 47L; // C+
        } else if (totalScore >= 70) {
            return 48L; // C
        } else if (totalScore >= 65) {
            return 125L; // D+
        } else if (totalScore >= 60) {
            return 49L; // D
        } else {
            return 50L; // F (혹은 50L로 처리될 수 있음)
        }
    }

    @Transactional
    public void assignGrade(GradeForm gradeForm) {
        Long lectureId = gradeForm.getLectureId(); // 강의 ID

        for (GradeForm.StudentGrade studentGrade : gradeForm.getStudentGrades()) {
            Long memberId = studentGrade.getMemberId(); // 학생 ID
            String gradeType = studentGrade.getGradeType(); // 성적 유형
            int score = studentGrade.getScore(); // 점수

            // 회원 정보 조회하여 member_type이 101인 학생만 필터링
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("학생이 존재하지 않습니다."));

            if (member.getMemberType().getCodeId() != 101L) {
                throw new IllegalArgumentException("학생이 아닙니다.");
            }

            // Ofregistration 조회 - 선택한 강의에만 성적을 부여
            List<Ofregistration> ofregistrations = ofregistrationRepository.findByMember_MemberIdAndLectureId(memberId, lectureId);
            if (ofregistrations.isEmpty()) {
                throw new IllegalStateException("선택한 강의에 등록되지 않은 학생입니다.");
            }

            // 강의에 대해 성적 부여
            for (Ofregistration ofregistration : ofregistrations) {
                List<Completion> completions = completionRepository.findAllByOfRegistration(ofregistration);
                Completion completion = completions.isEmpty()
                        ? createCompletion(ofregistration)
                        : completions.get(0);

                String gradeTypeName = getGradeTypeName(Long.parseLong(gradeType));
                CommonCode gradeTypeCode = commonCodeRepository.findById(Long.parseLong(gradeType))
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 성적 유형입니다."));

                Grade grade = Grade.builder()
                        .completion(completion)
                        .gradeType(gradeTypeCode)
                        .score(score)
                        .build();

                gradeRepository.save(grade);

                List<Grade> grades = gradeRepository.findByCompletion(completion);
                int totalScore = calculateTotalScore(grades);

                Long finalGradeCodeId = calculateFinalGrade(totalScore);
                CommonCode finalGrade = commonCodeRepository.findById(finalGradeCodeId)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 최종 등급입니다."));

                completion.setFinalGrade(finalGrade);

                Long completionStateCode = (finalGradeCodeId == 50L) ? 30L : 29L;
                CommonCode completionState = commonCodeRepository.findById(completionStateCode)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이수 상태 코드입니다."));

                completion.setCompletionState(completionState);
                completionRepository.save(completion);
            }
        }
    }


    // gradeType 값에 따른 성적 유형 이름 반환
    private String getGradeTypeName(Long gradeType) {
        switch (gradeType.intValue()) {
            case 67:
                return "중간";
            case 68:
                return "기말";
            case 69:
                return "과제";
            case 70:
                return "출석";
            default:
                return "기타";
        }
    }






    // Completion 생성 메서드
    private Completion createCompletion(Ofregistration ofregistration) {
        // Completion 생성 및 초기화
        CommonCode initialCompletionState = commonCodeRepository.findById(29L)
                .orElseThrow(() -> new IllegalArgumentException("유효한 이수 상태 코드가 없습니다."));

        Completion newCompletion = Completion.builder()
                .ofRegistration(ofregistration)
                .completionState(initialCompletionState) // 초기 상태 설정
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        completionRepository.save(newCompletion);
        return newCompletion;
    }







}
