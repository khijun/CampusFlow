package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Completion;
import edu.du.campusflow.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade,Long> {
    // 학생이 자신의 성적 조회 (여러 gradeType 처리)
    @Query("SELECT g FROM Grade g " +
            "JOIN g.completion c " +
            "JOIN c.ofRegistration o " +
            "WHERE o.member.memberId = :memberId " +
            "AND g.gradeType.codeId IN :gradeTypeList")
    List<Grade> findByMemberIdAndGradeTypes(@Param("memberId") Long memberId, @Param("gradeTypeList") List<Long> gradeTypeList);

    // 교수/교직원이 강의의 모든 학생 성적 조회 (여러 gradeType 처리)
    @Query("SELECT g FROM Grade g " +
            "JOIN g.completion c " +
            "JOIN c.ofRegistration o " +
            "WHERE o.lectureId.lectureId IN :lectureIds " +
            "AND g.gradeType.codeId IN :gradeTypeList")
    List<Grade> findByLectureIdsAndGradeTypes(@Param("lectureIds") List<Long> lectureIds, @Param("gradeTypeList") List<Long> gradeTypeList);


    List<Grade> findByCompletion(Completion completion);
}
