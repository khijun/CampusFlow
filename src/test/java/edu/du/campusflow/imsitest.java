package edu.du.campusflow;

import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.enums.InquiryStatus;
import edu.du.campusflow.service.CommonCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class imsitest {

    @Autowired
    private CommonCodeService commonCodeService;

    @Test
    void inputCommonCode() throws NoSuchFieldException {
        commonCodeService.insertCodeFromEnum(InquiryStatus.class);
    }
}
