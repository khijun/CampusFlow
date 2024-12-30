package edu.du.campusflow;

import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.service.InquiryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InquiryFlowTest {

    @Autowired
    private InquiryService inquiryService;

    @Test
    public void testCreateInquiry() {
        Inquiry inquiry = new Inquiry();
        inquiry.setSubject("Test Inquiry");
        inquiry.setContent("This is a test inquiry.");
        inquiry.setCreatedAt(LocalDateTime.now());

        Inquiry createdInquiry = inquiryService.createInquiry(inquiry);
        assertNotNull(createdInquiry);
        assertEquals("Test Inquiry", createdInquiry.getSubject());
    }

    // 추가적인 테스트 메서드 작성 가능
}