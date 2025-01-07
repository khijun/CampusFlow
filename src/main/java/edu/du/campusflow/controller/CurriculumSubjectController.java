package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CurriculumSubjectDTO;
import edu.du.campusflow.service.CurriculumSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class CurriculumSubjectController {

    @Autowired
    CurriculumSubjectService curriculumSubjectService;

    //강의 개설 페이지에서 사용할 검색
    @GetMapping("/api/curriculum-subjects/search")
    public ResponseEntity<List<CurriculumSubjectDTO>> searchCurriculumSubjects(
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String curriculumName) {
        List<CurriculumSubjectDTO> subjects = curriculumSubjectService.searchCurriculumSubjectBySubjectName(subjectName, curriculumName);
        return ResponseEntity.ok(subjects);
    }

}
