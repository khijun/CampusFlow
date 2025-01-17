package edu.du.campusflow.service;

import edu.du.campusflow.entity.Notice;
import edu.du.campusflow.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private AuthService authService;

    // 모든 공지 조회
    public Page<Notice> getAllNotices(Pageable pageable) {
        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 특정 공지 조회
    public Notice getNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));
    }

    // 공지 생성
    public Notice createNotice(Notice notice) {
        if (!isStaff()) {
            throw new AccessDeniedException("교직원만 공지사항을 작성할 수 있습니다.");
        }
        var currentMember = authService.getCurrentMember();
        notice.setMember(currentMember);
        notice.setCreatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    // 공지 업데이트
    public Notice updateNotice(Long noticeId, Notice notice) {
        if (!isStaff()) {
            throw new AccessDeniedException("교직원만 공지사항을 수정할 수 있습니다.");
        }
        Notice existingNotice = getNoticeById(noticeId);
        existingNotice.setSubject(notice.getSubject());
        existingNotice.setContent(notice.getContent());
        existingNotice.setUpdatedAt(LocalDateTime.now());
        return noticeRepository.save(existingNotice);
    }

    // 교직원 여부 확인
    public boolean isStaff() {
        var currentMember = authService.getCurrentMember();
        if (currentMember == null || currentMember.getMemberType() == null) {
            return false;
        }
        return "ADMIN".equals(currentMember.getMemberType().getCodeValue());
    }

    // 공지 삭제
    public void deleteNotice(Long noticeId) {
        if (!isStaff()) {
            throw new AccessDeniedException("교직원만 공지사항을 삭제할 수 있습니다.");
        }
        noticeRepository.deleteById(noticeId);
    }

    // 공지 검색
    public Page<Notice> searchNoticesBySubject(String keyword, Pageable pageable) {
        return noticeRepository.findBySubjectContainingOrderByCreatedAtDesc(keyword, pageable);
    }

    public Page<Notice> searchNoticesByMember(String keyword, Pageable pageable) {
        return noticeRepository.findByMember_NameContainingOrderByCreatedAtDesc(keyword, pageable);
    }

    public long getTotalCount() {
        return noticeRepository.count();
    }

    public long calculateNoticeNumber(Long noticeId) {
        // 현재 공지사항보다 최신인(createdAt이 더 큰) 공지사항의 개수를 구함
        Notice currentNotice = getNoticeById(noticeId);
        long countOfNewerNotices = noticeRepository.countByCreatedAtGreaterThan(currentNotice.getCreatedAt());
        // 최신 글이 가장 큰 번호를 가지도록 계산
        return getTotalCount() - countOfNewerNotices;
    }
}