package edu.du.campusflow.service;

import edu.du.campusflow.dto.DeptCreateRequest;
import edu.du.campusflow.dto.DeptDTO;
import edu.du.campusflow.dto.DeptSearchFilter;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeptService {
   private final DeptRepository deptRepository;
   private final CommonCodeService commonCodeService;

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

   public Dept save(Dept dept) {
      return deptRepository.save(dept);
   }

   @Transactional
   public DeptCreateRequest create(DeptCreateRequest deptCreateRequest) {

      List<DeptDTO> deptDTOList = deptCreateRequest.getDeptDTOList().stream().map(deptDTO->{
         Dept dept = Dept.builder()
             .deptName(deptDTO.getDeptName())
             .maxStudents(deptDTO.getMaxStudents())
             .deptStatus(commonCodeService.findById(deptCreateRequest.getDeptStatus()))
             .generalCredits(deptDTO.getGeneralCredits())
             .majorCredits(deptDTO.getMajorCredits())
             .graduationCredits(deptDTO.getGraduationCredits())
             .build();
         return DeptDTO.fromEntity(save(dept));
      }).collect(Collectors.toList());

      return DeptCreateRequest.builder()
          .deptDTOList(deptDTOList)
          .deptStatus(deptCreateRequest.getDeptStatus())
          .build();
   }

   public List<Dept> findByDeptId(Long deptId) {
      return deptRepository.findByDeptId(deptId);
   }

   public List<Dept> findByDeptNameContaining(String deptName) {
      return deptRepository.findByDeptNameContaining(deptName);
   }

   public List<Dept> findByDeptIdAndDeptName(Long deptId, String deptName) {
      return deptRepository.findByDeptIdAndDeptName(deptId, deptName);
   }
}
