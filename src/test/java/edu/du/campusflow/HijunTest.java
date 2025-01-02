package edu.du.campusflow;

import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.enums.*;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.FileInfoRepository;
import edu.du.campusflow.service.CommonCodeService;
import edu.du.campusflow.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;


@SpringBootTest
public class HijunTest {

    @Autowired
    private CommonCodeService commonCodeService;
    @Autowired
    private CommonCodeRepository commonCodeRepository;
    @Autowired
    private FileInfoRepository fileInfoRepository;


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
    public void fileTest(){
        System.out.println(FileUtils.getFileName("뚱이.jpg"));
        fileInfoRepository.findByFileTypeIn(Collections.singletonList("jpg")).forEach(System.out::println);
        System.out.println(Arrays.toString(fileInfoRepository.findAll().stream().map(FileInfo::getId).toArray()));
    }
}