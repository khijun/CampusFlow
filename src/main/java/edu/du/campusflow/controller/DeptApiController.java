package edu.du.campusflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.campusflow.dto.DeptCreateRequest;
import edu.du.campusflow.dto.DeptDTO;
import edu.du.campusflow.dto.DeptSearchFilter;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dept")
public class DeptApiController {

    private final DeptService deptService;

    @GetMapping
    public List<DeptDTO> getDept(@RequestParam(required = false, name = "filter") String searchURL) {
        ObjectMapper mapper = new ObjectMapper();
        DeptSearchFilter filter = null;
        try {
            filter = mapper.readValue(searchURL, DeptSearchFilter.class);
        }catch(Exception e){
            filter = DeptSearchFilter.builder().build();
        }
        return DeptDTO.fromEntityList(deptService.findAllWithFilter(filter));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeptDTO>> searchDept(
        @RequestParam(required = false) Long deptId,
        @RequestParam(required = false) String deptName) {

        // 학과 검색 로직
        List<Dept> depts;
        if (deptId != null && deptName != null) {
            depts = deptService.findByDeptIdAndDeptName(deptId, deptName);
        } else if (deptId != null) {
            depts = deptService.findByDeptId(deptId);
        } else if (deptName != null) {
            depts = deptService.findByDeptNameContaining(deptName);
        } else {
            depts = deptService.findAll(); // 전체 조회
        }

        List<DeptDTO> deptList = depts.stream()
            .map(DeptDTO::fromEntity)
            .collect(Collectors.toList());

        return ResponseEntity.ok(deptList);
    }

    @PostMapping
    public ResponseEntity<?> postDept(@RequestBody DeptCreateRequest deptCreateRequest) {
        try{
            return ResponseEntity.ok(deptService.create(deptCreateRequest));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
