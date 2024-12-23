package edu.du.academic_management_system;

import edu.du.academic_management_system.entity.Student;
import edu.du.academic_management_system.entity.UploadedFile;
import edu.du.academic_management_system.repository.UploadedFileRepository;
import edu.du.academic_management_system.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class AcademicManagementSystemApplicationTests {

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
    void contextLoads() {
    }

    @Test
    @Transactional
    @Rollback(false)
    void createDummyData() {
        // 0. 파일 데이터 생성
        uploadedFileService.createDummyFiles();
        
        // 1. 공통 코드 생성
        commonCodeService.createCommonCodes();
        
        // 2. 학생 더미 데이터 생성
        studentService.createDummyStudents();
        
        // 3. 교수 더미 데이터 생성
        professorService.createDummyProfessors();
        
        // 4. 교직원 더미 데이터 생성
        staffService.createDummyStaffs();
        
        // 데이터 확인
        List<Student> students = studentService.getAllStudents();
        List<UploadedFile> files = uploadedFileRepository.findAll();
        
        System.out.println("생성된 학생 수: " + students.size());
        System.out.println("생성된 파일 수: " + files.size());
    }

    @Test
    @Transactional
    void getMember(){
    }
}
