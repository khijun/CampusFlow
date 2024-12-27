package edu.du.campusflow;

import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.enums.DeptStatus;
import edu.du.campusflow.enums.Gender;
import edu.du.campusflow.service.CommonCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class hjinTest {

    @Autowired
    private CommonCodeService commonCodeService;


    @Test
    void commonCodeServiceTest() throws NoSuchFieldException {
        commonCodeService.insertCodeFromEnum(Gender.class);
        commonCodeService.insertCodeFromEnum(DeptStatus.class);
        commonCodeService.insertCodeFromEnum(AcademicStatus.class);
    }
}
