package edu.du.campusflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.campusflow.dto.DeptCreateRequest;
import edu.du.campusflow.dto.DeptDTO;
import edu.du.campusflow.dto.DeptSearchFilter;
import edu.du.campusflow.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<?> postDept(@RequestBody DeptCreateRequest deptCreateRequest) {
        try{
            return ResponseEntity.ok(deptService.create(deptCreateRequest));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
