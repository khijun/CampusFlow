package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}