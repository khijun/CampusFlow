package edu.du.campusflow;

import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.ChangeRequestRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.StudentRepository;
import edu.du.campusflow.service.ChangeRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class HjinTest {


    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @Autowired
    private ChangeHistoryRepository changeHistoryRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student student;
    private CommonCode beforeCode;
    private CommonCode afterCode;
    private CommonCode academicStatus;
    private CommonCode applicationStatus;
    @Autowired
    private ChangeRequestService changeRequestService;


    @Test
    void academic(){
        List<Student> student1 = studentRepository.findAll();
        System.out.println(student1);
    }




}
