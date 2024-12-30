package edu.du.campusflow;

import edu.du.campusflow.entity.*;
import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.ChangeRequestRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.StudentRepository;
import edu.du.campusflow.service.ChangeService;
import edu.du.campusflow.service.CommonCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class HjinTest {

    @Autowired
    private CommonCodeService commonCodeService;

    @Autowired
    private ChangeService changeService;

    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @Autowired
    private ChangeHistoryRepository changeHistoryRepository;

    private Student student;
    private ChangeRequest changeRequest;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CommonCodeRepository commonCodeRepository;


    @Test
    void inputCommonCode() throws NoSuchFieldException{
        commonCodeService.insertCodeFromEnum(AcademicStatus.class);
    }


}
