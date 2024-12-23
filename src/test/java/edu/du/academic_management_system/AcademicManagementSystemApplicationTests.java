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

    @Test
    void contextLoads() {

    }
}
