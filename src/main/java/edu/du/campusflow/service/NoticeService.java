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

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }
    public Notice getNotticeById(Long noticeId){
        return noticeRepository.findById(noticeId).orElse(null);
    }
    public Notice createNotice(Notice notice){
        notice.setCreatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }
    public Notice updateNotice(Long noticeId,Notice notice){
        Notice existingNotice = getNotticeById(noticeId);
        if(existingNotice != null){
            existingNotice.setSubject(notice.getSubject());
            existingNotice.setContent(notice.getContent());
            existingNotice.setCreatedAt(LocalDateTime.now());
            return noticeRepository.save(existingNotice);
        }
        return null;
    }
    public void deleteNotice(Long noticeId){
        noticeRepository.deleteById(noticeId);
    }
}
