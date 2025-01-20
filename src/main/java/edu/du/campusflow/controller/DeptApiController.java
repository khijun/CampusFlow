package edu.du.campusflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.campusflow.dto.DeptApiDTO;
import edu.du.campusflow.dto.DeptDTO;
import edu.du.campusflow.dto.DeptSearchFilter;
import edu.du.campusflow.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
