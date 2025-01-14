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

    public List<Dept> findAll(){
        return deptRepository.findAll();
    }

   public List<Dept> getAllDepartments() {
      return deptRepository.findAll();
   }
    public Dept findById(Long deptId){
        return deptRepository.findById(deptId).orElse(null);
    }

    public List<Dept> findAllWithFilter(String deptName,
                                        Integer minStudentValue,
                                        Integer maxStudentValue,
                                        Long deptStatus){
        if(deptName!=null&&!deptName.isEmpty()) deptName = '%'+deptName+'%';
        return deptRepository.findAllWithFilter(deptName, minStudentValue, maxStudentValue, deptStatus);
    }
    // 특정 학과 조회
    public Dept getDepartmentById(Long deptId) {
        return deptRepository.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Dept ID: " + deptId));
    }

}