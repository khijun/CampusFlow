package edu.du.campusflow;

import edu.du.campusflow.enums.Semester;
import edu.du.campusflow.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DongTest {

    @Autowired
    private CommonCodeService commonCodeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private MemberService memberService;

    @Test
    @Transactional
    void createDummyData() {

    }

    @Test
    @Transactional
    void getMember(){
    }

    @Test
    void commonCodeServiceTest() throws NoSuchFieldException {
        commonCodeService.insertCodeFromEnum(Semester.class);
    }
}