package edu.du.campusflow.repository;


import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.CommonCode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest,Long> {
    // Member의 memberId로 조회
    @EntityGraph(attributePaths = {
//            "member.dept.curriculums.curriculumStatus", "member.dept.deptStatus", "member.dept.curriculums.grade", "member.dept.curriculums.dayNight", "member.dept.curriculums.gradingMethod",
            "beforeCode", "afterCode", "academicStatus", "applicationStatus"})
    List<ChangeRequest> findByMember_MemberId(Long memberId);

    // Member의 memberId로 조회
    @EntityGraph(attributePaths = {
//            "member.dept.curriculums.curriculumStatus", "member.dept.deptStatus", "member.dept.curriculums.grade", "member.dept.curriculums.dayNight", "member.dept.curriculums.gradingMethod",
            "beforeCode", "afterCode", "academicStatus", "applicationStatus"})
    List<ChangeRequest> findAll();
}
