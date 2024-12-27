package edu.du.campusflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;





import edu.du.campusflow.enums.CurriculumStatus;

import edu.du.campusflow.service.CommonCodeService;



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
        commonCodeService.insertCodeFromEnum(AcademicStatus.class);
        commonCodeService.insertCodeFromEnum(Building.class);
        commonCodeService.insertCodeFromEnum(ClassStatus.class);
        commonCodeService.insertCodeFromEnum(CompletionStatus.class);
        commonCodeService.insertCodeFromEnum(CurriculumStatus.class);
        commonCodeService.insertCodeFromEnum(DeptStatus.class);
        commonCodeService.insertCodeFromEnum(FacilityStatus.class);
        commonCodeService.insertCodeFromEnum(FinalGradeStatus.class);
        commonCodeService.insertCodeFromEnum(Floor.class);
        commonCodeService.insertCodeFromEnum(Gender.class);
        commonCodeService.insertCodeFromEnum(GradeLevelRatio.class);
        commonCodeService.insertCodeFromEnum(GradeRatio.class);
        commonCodeService.insertCodeFromEnum(LectureDay.class);
        commonCodeService.insertCodeFromEnum(LectureStatus.class);
        commonCodeService.insertCodeFromEnum(RegStatus.class);
        commonCodeService.insertCodeFromEnum(Semester.class);
        commonCodeService.insertCodeFromEnum(SubjectType.class);
    }
}