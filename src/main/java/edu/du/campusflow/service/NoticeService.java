package edu.du.campusflow.service;

import edu.du.campusflow.entity.Notice;
import edu.du.campusflow.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private AuthService authService;

    // 모든 공지 조회
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    // 특정 공지 조회
    public Notice getNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElse(null);
    }

    // 공지 생성
    public Notice createNotice(Notice notice) {
        var currentMember = authService.getCurrentMember();
        notice.setMember(currentMember);
        notice.setCreatedAt(LocalDateTime.now()); // 생성 날짜 설정
        return noticeRepository.save(notice);
    }

    // 공지 업데이트
    public Notice updateNotice(Long noticeId, Notice notice) {
        Notice existingNotice = getNoticeById(noticeId);
        if (existingNotice != null) {
            existingNotice.setSubject(notice.getSubject());
            existingNotice.setContent(notice.getContent());
            existingNotice.setUpdatedAt(LocalDateTime.now()); // 업데이트 날짜 설정
            return noticeRepository.save(existingNotice);
        }
        return null;
    }
    public boolean isStaff() {
        var currentMember = authService.getCurrentMember();
        if (currentMember == null || currentMember.getMemberType() == null) {
            return false;
        }
        return "ADMIN".equals(currentMember.getMemberType().getCodeValue());
    }


    // 공지 삭제
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}