package edu.du.campusflow.service;

import edu.du.campusflow.dto.DeptSearchFilter;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptService {
    private final DeptRepository deptRepository;

    public List<Dept> findAll() {
        return deptRepository.findAll();
    }

    public List<Dept> getAllDepartments() {
        return deptRepository.findAll();
    }

    public Dept findById(Long deptId) {
        return deptRepository.findById(deptId).orElse(null);
    }

    public List<Dept> findAllWithFilter(DeptSearchFilter filter) {
        if (filter != null) {
            if (filter.getDeptName() != null && !filter.getDeptName().isEmpty())
                filter.setDeptName('%' + filter.getDeptName() + '%');
        }
        return deptRepository.findAllWithFilter(filter);
    }
}
