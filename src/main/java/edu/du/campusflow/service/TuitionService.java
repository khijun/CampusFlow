package edu.du.campusflow.service;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.Tuition;
import edu.du.campusflow.repository.TuitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 등록금 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TuitionService {
    
    private final TuitionRepository tuitionRepository;

    /**
     * 모든 등록금 정보를 조회
     * @return 모든 등록금 정보 목록
     */
    public List<TuitionDTO> getAllTuitions() {
        return tuitionRepository.findAll().stream()
                .map(TuitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 특정 등록금 정보를 조회
     * @param id 조회할 등록금 ID
     * @return 등록금 정보
     * @throws IllegalArgumentException 해당 ID의 등록금 정보가 없을 경우
     */
    public TuitionDTO getTuitionById(Long id) {
        return tuitionRepository.findById(id)
                .map(TuitionDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("해당 등록금 정보가 존재하지 않습니다. ID: " + id));
    }

    /**
     * 특정 연도의 등록금 정보를 조회
     * @param year 조회할 연도
     * @return 해당 연도의 등록금 정보 목록
     */
    public List<TuitionDTO> getTuitionsByYear(Year year) {
        return tuitionRepository.findByTuiYear(year).stream()
                .map(TuitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 학과의 등록금 정보를 조회
     * @param deptId 조회할 학과 ID
     * @return 해당 학과의 등록금 정보 목록
     */
    public List<TuitionDTO> getTuitionsByDept(Long deptId) {
        return tuitionRepository.findByDeptId_DeptId(deptId).stream()
                .map(TuitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 연도와 학과의 등록금 정보를 조회
     * @param year 조회할 연도
     * @param deptId 조회할 학과 ID
     * @return 해당 연도와 학과의 등록금 정보 목록
     */
    public List<TuitionDTO> getTuitionsByYearAndDept(Year year, Long deptId) {
        return tuitionRepository.findByTuiYearAndDeptId_DeptId(year, deptId).stream()
                .map(TuitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 등록금 정보를 저장
     * @param tuition 저장할 등록금 정보
     * @return 저장된 등록금 정보
     */
    @Transactional
    public TuitionDTO saveTuition(Tuition tuition) {
        return TuitionDTO.fromEntity(tuitionRepository.save(tuition));
    }

    /**
     * 기존 등록금 정보를 수정
     * @param id 수정할 등록금 ID
     * @param updatedTuition 수정할 내용
     * @return 수정된 등록금 정보
     * @throws IllegalArgumentException 해당 ID의 등록금 정보가 없을 경우
     */
    @Transactional
    public TuitionDTO updateTuition(Long id, Tuition updatedTuition) {
        return tuitionRepository.findById(id)
                .map(tuition -> {
                    tuition.setDeptId(updatedTuition.getDeptId());
                    tuition.setTuiYear(updatedTuition.getTuiYear());
                    tuition.setSemester(updatedTuition.getSemester());
                    tuition.setAmount(updatedTuition.getAmount());
                    return TuitionDTO.fromEntity(tuitionRepository.save(tuition));
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 등록금 정보가 존재하지 않습니다. ID: " + id));
    }

    /**
     * 등록금 정보를 삭제
     * @param id 삭제할 등록금 ID
     */
    @Transactional
    public void deleteTuition(Long id) {
        tuitionRepository.deleteById(id);
    }
} 