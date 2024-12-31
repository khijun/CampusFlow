package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Tuition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;

/**
 * 등록금 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 */
@Repository
public interface TuitionRepository extends JpaRepository<Tuition, Long> {
    /**
     * 특정 년도의 등록금 정보를 조회
     * @param year 조회할 년도
     * @return 해당 년도의 등록금 목록
     */
    List<Tuition> findByTuiYear(Year year);

    /**
     * 특정 학과의 등록금 정보를 조회
     * @param deptId 조회할 학과 ID
     * @return 해당 학과의 등록금 목록
     */
    List<Tuition> findByDeptId_DeptId(Long deptId);

    /**
     * 특정 년도와 학과의 등록금 정보를 조회
     * @param year 조회할 년도
     * @param deptId 조회할 학과 ID
     * @return 해당 년도와 학과의 등록금 목록
     */
    List<Tuition> findByTuiYearAndDeptId_DeptId(Year year, Long deptId);
}
