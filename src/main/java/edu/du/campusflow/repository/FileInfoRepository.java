package edu.du.campusflow.repository;

import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    public List<FileInfo> findByFileTypeIn(List<String> fileType);
    public List<FileInfo> findByMember(Member member);
} 