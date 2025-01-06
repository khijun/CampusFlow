package edu.du.campusflow;

import edu.du.campusflow.entity.Category;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.repository.CategoryRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.service.CommonCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
public class HijunTest {

    @Autowired
    private CommonCodeService commonCodeService;
    @Autowired
    private CommonCodeRepository commonCodeRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @Transactional
    void createDummyData() {

    }


    @Test
    @Transactional
    void getMember() {
    }

    @Test
    void inputCommonCode() throws NoSuchFieldException {
//        commonCodeService.insertCodeFromEnum(AcademicStatus.class);
//        commonCodeService.insertCodeFromEnum(ApplicationStatus.class);
//        commonCodeService.insertCodeFromEnum(AttendanceStatus.class);
//        commonCodeService.insertCodeFromEnum(Building.class);
//        commonCodeService.insertCodeFromEnum(ClassStatus.class);
//        commonCodeService.insertCodeFromEnum(CompletionStatus.class);
//        commonCodeService.insertCodeFromEnum(CurriculumStatus.class);
//        commonCodeService.insertCodeFromEnum(DeptStatus.class);
//        commonCodeService.insertCodeFromEnum(FacilityStatus.class);
//        commonCodeService.insertCodeFromEnum(FamilyRelation.class);
//        commonCodeService.insertCodeFromEnum(FinalGradeStatus.class);
//        commonCodeService.insertCodeFromEnum(Floor.class);
//        commonCodeService.insertCodeFromEnum(Gender.class);
//        commonCodeService.insertCodeFromEnum(GradeLevelRatio.class);
//        commonCodeService.insertCodeFromEnum(GradeRatio.class);
//        commonCodeService.insertCodeFromEnum(GradeType.class);
//        commonCodeService.insertCodeFromEnum(InquiryStatus.class);
//        commonCodeService.insertCodeFromEnum(LectureDay.class);
//        commonCodeService.insertCodeFromEnum(LectureStatus.class);
//        commonCodeService.insertCodeFromEnum(MilitaryStatus.class);
//        commonCodeService.insertCodeFromEnum(RegStatus.class);
//        commonCodeService.insertCodeFromEnum(Semester.class);
//        commonCodeService.insertCodeFromEnum(SubjectType.class);
    }

    @Test
    public void dbtest() {
            commonCodeRepository.findAll().forEach(System.out::println);
    }

    @Test
    @Transactional
    public void categoryTest(){
        Category category = categoryRepository.findById(50L).orElse(null);
        System.out.println(category);
        CommonCode student = commonCodeRepository.findByCodeValue("STUDENT");
        List<Category> studentCategory = categoryRepository.findByParentIsNullAndMemberTypeOrderByOrderNoAsc(student);
        studentCategory.forEach(c->System.out.println(c.getName()));
        System.out.println();
    }
}