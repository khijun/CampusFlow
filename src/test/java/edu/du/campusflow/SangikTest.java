package edu.du.campusflow;

import edu.du.campusflow.enums.*;
import edu.du.campusflow.repository.UploadedFileRepository;
import edu.du.campusflow.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SangikTest {

   @Autowired
   private CommonCodeService commonCodeService;

   @Autowired
   private StudentService studentService;

   @Autowired
   private ProfessorService professorService;

   @Autowired
   private StaffService staffService;

   @Autowired
   private UploadedFileService uploadedFileService;
   @Autowired
   private UploadedFileRepository uploadedFileRepository;
   @Autowired
   private MemberService memberService;

   @Test
   void commonCodeServiceTest() throws NoSuchFieldException {
      commonCodeService.insertCodeFromEnum(Gender.class);
      commonCodeService.insertCodeFromEnum(DeptStatus.class);
      commonCodeService.insertCodeFromEnum(AcademicStatus.class);
      commonCodeService.insertCodeFromEnum(CurriculumStatus.class);
      commonCodeService.insertCodeFromEnum(CompletionStatus.class);
      commonCodeService.insertCodeFromEnum(FinalGradeStatus.class);
   }
}
