package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 특정 문의에 대한 답변을 찾기 위한 메서드
    List<Inquiry> findByResponseTo(Inquiry responseTo);
}