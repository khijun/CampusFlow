package edu.du.campusflow;

import edu.du.campusflow.dto.CategoryDTO;
import edu.du.campusflow.entity.Category;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.enums.*;
import edu.du.campusflow.repository.CategoryRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.FileInfoRepository;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.service.CategoryService;
import edu.du.campusflow.service.CommonCodeService;
import edu.du.campusflow.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootTest
public class HijunTest {

    @Autowired
    private CommonCodeService commonCodeService;
    @Autowired
    private CommonCodeRepository commonCodeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MemberRepository memberRepository;


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
        commonCodeService.insertCodeFromEnum(AcademicStatus.class);
        commonCodeService.insertCodeFromEnum(ApplicationStatus.class);
        commonCodeService.insertCodeFromEnum(AttendanceStatus.class);
        commonCodeService.insertCodeFromEnum(Building.class);
        commonCodeService.insertCodeFromEnum(ClassStatus.class);
        commonCodeService.insertCodeFromEnum(CompletionStatus.class);
        commonCodeService.insertCodeFromEnum(CurriculumStatus.class);
        commonCodeService.insertCodeFromEnum(DeptStatus.class);
        commonCodeService.insertCodeFromEnum(FacilityStatus.class);
        commonCodeService.insertCodeFromEnum(FamilyRelation.class);
        commonCodeService.insertCodeFromEnum(FinalGradeStatus.class);
        commonCodeService.insertCodeFromEnum(Floor.class);
        commonCodeService.insertCodeFromEnum(Gender.class);
        commonCodeService.insertCodeFromEnum(GradeLevelRatio.class);
        commonCodeService.insertCodeFromEnum(GradeRatio.class);
        commonCodeService.insertCodeFromEnum(GradeType.class);
        commonCodeService.insertCodeFromEnum(InquiryStatus.class);
        commonCodeService.insertCodeFromEnum(LectureDay.class);
        commonCodeService.insertCodeFromEnum(LectureStatus.class);
        commonCodeService.insertCodeFromEnum(MilitaryStatus.class);
        commonCodeService.insertCodeFromEnum(RegStatus.class);
        commonCodeService.insertCodeFromEnum(Semester.class);
        commonCodeService.insertCodeFromEnum(SubjectType.class);
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
        List<CategoryDTO> dtos = categoryService.findByType(student);
        Member professor = memberRepository.findById(1001L).orElse(null);
        dtos = categoryService.findByType(professor);
        System.out.println(dtos.size());
        dtos.forEach(System.out::println);


        int iterations = 1; // 메서드 실행 횟수
        long startTime, endTime, duration;

        System.out.println("-- findByType --");
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            dtos = categoryService.findByType(student);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("메서드 종료시간: " + endTime);
        System.out.println("평균 실행시간 (findByType): " + (duration / iterations) + " 나노초");

        System.out.println("-- findByTypeNew --");
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
//            dtos = categoryService.findByTypeOld(student);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("메서드 종료시간: " + endTime);
        System.out.println("평균 실행시간 (findByTypeOld): " + (duration / iterations) + " 나노초");
    }
    @Test
    public void fileTest(){
        System.out.println(FileUtils.getFileName("뚱이.jpg"));
        fileInfoRepository.findByFileTypeIn(Collections.singletonList("jpg")).forEach(System.out::println);
        System.out.println(Arrays.toString(fileInfoRepository.findAll().stream().map(FileInfo::getId).toArray()));
    }
}