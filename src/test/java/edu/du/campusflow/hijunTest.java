package edu.du.campusflow;

import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.enums.CurriculumStatus;
import edu.du.campusflow.enums.DeptStatus;
import edu.du.campusflow.enums.Gender;
import edu.du.campusflow.service.CommonCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class hijunTest {

        @Autowired
        private CommonCodeService commonCodeService;

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
            commonCodeService.insertCodeFromEnum(Gender.class);
            commonCodeService.insertCodeFromEnum(DeptStatus.class);
            commonCodeService.insertCodeFromEnum(AcademicStatus.class);
            commonCodeService.insertCodeFromEnum(CurriculumStatus.class);
        }
    }
