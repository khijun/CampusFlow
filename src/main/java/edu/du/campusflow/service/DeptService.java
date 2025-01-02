package edu.du.campusflow.service;

import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptService {

    @Autowired
    private DeptRepository deptRepository; // DeptRepository 주입

    // 모든 학과 조회
    public List<Dept> getAllDepartments() {
        return deptRepository.findAll(); // 모든 학과를 반환
    }
}
