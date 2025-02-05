package edu.du.campusflow.service;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.dto.GradeForm;
import edu.du.campusflow.dto.GradeFormProfessor;
import edu.du.campusflow.dto.GradeProfessorDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
                    int subjectCredits = 0;  // 학점 초기화

                    // 성적 정보를 scores에 추가 및 가중치 적용
                    for (Grade grade : entry.getValue()) {
                        Integer score = grade.getScore() == -1 ? null : grade.getScore();  // -1이면 null로 처리
                        scores.put(getGradeTypeName(grade.getGradeType().getCodeId()), score);
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

                    // CurriculumSubject에서 Subject를 통해 subjectCredits 조회
                    subjectCredits = completion.getOfRegistration().getLectureId().getCurriculumSubject().getSubject().getSubjectCredits();  // subject_credits

                    // 미이수 상태일 경우 취득 학점만 0으로 설정
                    int earnedCredits = (completion.getCompletionState().getCodeId() == 30) ? 0 : subjectCredits;

                    // 학년도 계산
                    int academicYear = completion.getOfRegistration().getRegDate().getYear(); // LocalDateTime 기준
                    // String 타입이라면: Integer.parseInt(completion.getOfRegistration().getRegDate().substring(0, 4));

                    return new GradeDTO(professorName, lectureName, scores, finalGrade.getCodeName(), finalTotalScore, subjectCredits,earnedCredits,academicYear,earnedCredits);
                })
                .collect(Collectors.toList());
// **총 취득 학점 계산**
        int totalEarnedCredits = result.stream()
                .mapToInt(GradeDTO::getEarnedCredits)
                .sum();

// **각 DTO에 총 취득 학점 세팅**
        result.forEach(dto -> dto.setTotalEarnedCredits(totalEarnedCredits));
        return result;
    }



    private int calculateTotalScore(List<Grade> grades) {
        int totalScore = 0;

        // 각 성적 항목(중간, 기말, 과제, 출석)을 더함
        for (Grade grade : grades) {
            Long codeId = grade.getGradeType().getCodeId();  // 성적 항목의 codeId

            // 점수가 -1인 경우는 계산에서 제외
            if (grade.getScore() == -1) {
                continue;
            }
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
        Long lectureId = gradeForm.getLectureId();

        for (GradeForm.StudentGrade studentGrade : gradeForm.getStudentGrades()) {
            Long memberId = studentGrade.getMemberId();
            String gradeType = studentGrade.getGradeType();
            int score = studentGrade.getScore();

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("학생이 존재하지 않습니다."));

            if (member.getMemberType().getCodeId() != 101L) {
                throw new IllegalArgumentException("학생이 아닙니다.");
            }

            List<Ofregistration> ofregistrations = ofregistrationRepository.findByMember_MemberIdAndLectureId(memberId, lectureId);
            if (ofregistrations.isEmpty()) {
                throw new IllegalStateException("선택한 강의에 등록되지 않은 학생입니다.");
            }

            for (Ofregistration ofregistration : ofregistrations) {
                List<Completion> completions = completionRepository.findAllByOfRegistration(ofregistration);
                Completion completion = completions.isEmpty()
                        ? createCompletion(ofregistration)
                        : completions.get(0);

                CommonCode gradeTypeCode = commonCodeRepository.findById(Long.parseLong(gradeType))
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 성적 유형입니다."));

                // 이미 해당 성적 유형이 부여된 경우 처리 방지
                boolean gradeExists = gradeRepository.existsByCompletionAndGradeType(completion, gradeTypeCode);
                if (gradeExists) {
                    throw new IllegalStateException("이미 해당 유형의 성적이 부여되었습니다.");
                }

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
        // Ofregistration에 해당하는 Completion이 이미 존재하는지 확인
        Optional<Completion> existingCompletion = completionRepository.findByOfRegistration(ofregistration);
        if (existingCompletion.isPresent()) {
            // 이미 존재하면 새로 생성하지 않고 기존의 Completion 반환
            return existingCompletion.get();
        }

        // Completion 생성 및 초기화
        CommonCode initialCompletionState = commonCodeRepository.findById(31L)
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

    public List<GradeProfessorDTO> getStudentGradesByProfessor(Long professorId, Long targetStudentId, List<Long> gradeTypeList) {
        // 교수인지 검증
        Member professor = memberRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Long memberType = professor.getMemberType().getCodeId();
        if (memberType != 102L) { // 102L: 교수
            throw new IllegalArgumentException("교수만 조회할 수 있습니다.");
        }

        // 교수가 담당하는 강의 목록 조회
        List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(professorId);
        if (lectureIds.isEmpty()) {
            throw new IllegalArgumentException("담당 강의가 없습니다.");
        }

        // 특정 학생의 성적 조회 (교수가 담당하는 강의 내에서만)
        List<Grade> grades = gradeRepository.findByLectureIdsAndStudentIdAndGradeTypes(lectureIds, targetStudentId, gradeTypeList);
        if (grades.isEmpty()) {
            // 성적 정보가 없으면 빈 리스트 반환
            return Collections.emptyList();
        }

        // 데이터 그룹화
        Map<String, List<Grade>> groupedGrades = grades.stream()
                .collect(Collectors.groupingBy(grade ->
                        grade.getCompletion().getOfRegistration().getLectureId().getLectureName()
                ));

        // 그룹화된 데이터를 DTO로 변환
        List<GradeProfessorDTO> result = groupedGrades.entrySet().stream()
                .map(entry -> {
                    String lectureName = entry.getKey();

                    Map<String, Integer> scores = new HashMap<>();
                    int totalScore = 0;
                    int subjectCredits = 0;

                    for (Grade grade : entry.getValue()) {
                        Integer score = grade.getScore() == -1 ? null : grade.getScore();  // -1이면 null로 처리
                        scores.put(getGradeTypeName(grade.getGradeType().getCodeId()), score);
                    }


                    int finalTotalScore = calculateTotalScore(entry.getValue());
                    Long finalGradeCode = calculateFinalGrade(finalTotalScore);

                    CommonCode finalGrade = commonCodeRepository.findById(finalGradeCode)
                            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 성적 코드입니다."));

                    Completion completion = entry.getValue().get(0).getCompletion();
                    completion.setFinalGrade(finalGrade);
                    completionRepository.save(completion);

                    subjectCredits = completion.getOfRegistration().getLectureId().getCurriculumSubject().getSubject().getSubjectCredits();
                    int earnedCredits = (completion.getCompletionState().getCodeId() == 30) ? 0 : subjectCredits;

                    // 선택된 강의 ID를 현재 강의로 설정 (lectureIds에서 해당 강의에 맞는 강의 ID를 찾아서 전달)
                    Long selectedLectureId = entry.getValue().get(0).getCompletion().getOfRegistration().getLectureId().getLectureId();

                    return new GradeProfessorDTO(lectureIds, selectedLectureId, professor.getName(), lectureName, scores, finalGrade.getCodeName(), finalTotalScore, subjectCredits, earnedCredits,null,null);
                })
                .collect(Collectors.toList());

        return result;
    }


    public List<GradeProfessorDTO> getAllStudentGradesByProfessor(Long professorId, List<Long> gradeTypeList) {
        // 교수인지 검증
        Member professor = memberRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!professor.getMemberType().getCodeId().equals(102L)) { // 102L: 교수
            throw new IllegalArgumentException("교수만 조회할 수 있습니다.");
        }

        // 교수가 담당하는 강의 목록 조회
        List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(professorId);
        if (lectureIds.isEmpty()) {
            throw new IllegalArgumentException("담당 강의가 없습니다.");
        }

        // 해당 강의를 듣는 모든 학생의 성적 조회
        List<Grade> grades = gradeRepository.findByLectureIdsAndGradeTypes(lectureIds, gradeTypeList);
        if (grades.isEmpty()) {
            return Collections.emptyList();
        }

        // 학생별 그룹화
        Map<Long, Map<String, List<Grade>>> studentGroupedGrades = grades.stream()
                .collect(Collectors.groupingBy(
                        grade -> grade.getCompletion().getOfRegistration().getMember().getMemberId(),
                        Collectors.groupingBy(grade -> grade.getCompletion().getOfRegistration().getLectureId().getLectureName())
                ));

        // DTO 변환
        List<GradeProfessorDTO> result = studentGroupedGrades.entrySet().stream()
                .flatMap(studentEntry -> {
                    Long studentId = studentEntry.getKey();
                    Map<String, List<Grade>> groupedGrades = studentEntry.getValue();

                    return groupedGrades.entrySet().stream().map(entry -> {
                        String lectureName = entry.getKey();

                        Map<String, Integer> scores = new HashMap<>();
                        for (Grade grade : entry.getValue()) {
                            Integer score = grade.getScore() == -1 ? null : grade.getScore();
                            scores.put(getGradeTypeName(grade.getGradeType().getCodeId()), score);
                        }

                        int finalTotalScore = calculateTotalScore(entry.getValue());
                        Long finalGradeCode = calculateFinalGrade(finalTotalScore);
                        CommonCode finalGrade = commonCodeRepository.findById(finalGradeCode)
                                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 성적 코드입니다."));

                        Completion completion = entry.getValue().get(0).getCompletion();
                        completion.setFinalGrade(finalGrade);
                        completionRepository.save(completion);

                        int subjectCredits = completion.getOfRegistration().getLectureId().getCurriculumSubject().getSubject().getSubjectCredits();
                        int earnedCredits = (completion.getCompletionState().getCodeId() == 30) ? 0 : subjectCredits;

                        Long selectedLectureId = completion.getOfRegistration().getLectureId().getLectureId();
                        String studentName = entry.getValue().get(0).getCompletion().getOfRegistration().getMember().getName(); // 학생 이름

                        return new GradeProfessorDTO(lectureIds, selectedLectureId, professor.getName(), lectureName, scores, finalGrade.getCodeName(), finalTotalScore, subjectCredits, earnedCredits, studentName, studentId);
                    });
                })
                .collect(Collectors.toList());

        return result;
    }


    @Transactional
    public void updateGrade(GradeFormProfessor gradeFormProfessor) {
        Long lectureId = gradeFormProfessor.getLectureId();
        log.info("강의 ID: {}", lectureId);

        for (GradeFormProfessor.StudentGrade studentGrade : gradeFormProfessor.getStudentGrades()) {
            Long memberId = studentGrade.getMemberId();
            String gradeType = studentGrade.getGradeType();
            int score = studentGrade.getScore();
            log.info("학생 ID: {}, 성적 유형: {}, 점수: {}", memberId, gradeType, score);

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("학생이 존재하지 않습니다."));
            log.info("조회된 학생 정보: {}", member);

            if (member.getMemberType().getCodeId() != 101L) {
                throw new IllegalArgumentException("학생이 아닙니다.");
            }

            List<Ofregistration> ofregistrations = ofregistrationRepository.findByMember_MemberIdAndLectureId(memberId, lectureId);
            if (ofregistrations.isEmpty()) {
                throw new IllegalStateException("선택한 강의에 등록되지 않은 학생입니다.");
            }
            log.info("조회된 수강 등록 정보: {}", ofregistrations);

            for (Ofregistration ofregistration : ofregistrations) {
                List<Completion> completions = completionRepository.findAllByOfRegistration(ofregistration);
                Completion completion = completions.isEmpty()
                        ? createCompletion(ofregistration)
                        : completions.get(0);
                log.info("조회된 혹은 생성된 이수 정보: {}", completion);

                Long gradeTypeCodeId = getGradeTypeCodeId(gradeType);
                CommonCode gradeTypeCode = commonCodeRepository.findById(gradeTypeCodeId)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 성적 유형입니다."));
                log.info("조회된 성적 유형 코드: {}", gradeTypeCode);

                Grade existingGrade = gradeRepository.findByCompletionAndGradeType(completion, gradeTypeCode);
                if (existingGrade != null) {
                    existingGrade.setScore(score);
                    gradeRepository.save(existingGrade);
                    log.info("기존 성적 업데이트: {}", existingGrade);
                } else {
                    Grade grade = Grade.builder()
                            .completion(completion)
                            .gradeType(gradeTypeCode)
                            .score(score)
                            .build();
                    gradeRepository.save(grade);
                    log.info("새로운 성적 저장: {}", grade);
                }

                List<Grade> grades = gradeRepository.findByCompletion(completion);
                int totalScore = calculateTotalScore(grades);
                log.info("계산된 총점: {}", totalScore);

                Long finalGradeCodeId = calculateFinalGrade(totalScore);
                CommonCode finalGrade = commonCodeRepository.findById(finalGradeCodeId)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 최종 등급입니다."));
                log.info("최종 등급 코드: {}", finalGrade);

                completion.setFinalGrade(finalGrade);

                boolean allGradesAssigned = true;
                List<Long> gradeTypeList = List.of(67L, 68L, 69L, 70L);
                for (Long type : gradeTypeList) {
                    Grade gradeByType = gradeRepository.findByCompletionAndGradeType(completion, commonCodeRepository.findById(type).get());
                    if (gradeByType == null || gradeByType.getScore() == -1) {
                        allGradesAssigned = false;
                        break;
                    }
                }

                Long completionStateCode = allGradesAssigned ? (finalGradeCodeId == 50L ? 30L : 29L) : 31L;
                CommonCode completionState = commonCodeRepository.findById(completionStateCode)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이수 상태 코드입니다."));
                completion.setCompletionState(completionState);
                log.info("이수 상태 업데이트: {}", completionState);

                completionRepository.save(completion);
            }
        }
    }

    // 성적 유형에 맞는 코드 ID를 반환하는 메서드
    private Long getGradeTypeCodeId(String gradeType) {
        switch (gradeType) {
            case "중간":
                return 67L;  // 중간 성적 코드 ID
            case "기말":
                return 68L;  // 기말 성적 코드 ID
            case "과제":
                return 69L;  // 과제 성적 코드 ID
            case "출석":
                return 70L;  // 출석 성적 코드 ID
            default:
                throw new IllegalArgumentException("유효하지 않은 성적 유형입니다.");
        }
    }
    }

