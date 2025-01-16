package edu.du.campusflow;

import edu.du.campusflow.service.CommonCodeService;
import edu.du.campusflow.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SangikTest {

   @Autowired
   private CommonCodeService commonCodeService;

   @Autowired
   private MemberService memberService;

}
