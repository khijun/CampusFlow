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
}
