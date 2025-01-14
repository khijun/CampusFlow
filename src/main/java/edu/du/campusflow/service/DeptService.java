package edu.du.campusflow.service;

import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptService {

   private final DeptRepository deptRepository;

   public List<Dept> getAllDepartments() {
      return deptRepository.findAll();
   }

   // 특정 학과 조회
   public Dept getDepartmentById(Long deptId) {
      return deptRepository.findById(deptId)
              .orElseThrow(() -> new IllegalArgumentException("Invalid Dept ID: " + deptId));
   }
}