package edu.du.campusflow.service;

import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {

   @Autowired
   private DeptRepository deptRepository;

   public List<Dept> findAllDepartments() {
      return deptRepository.findAll();
   }
}
