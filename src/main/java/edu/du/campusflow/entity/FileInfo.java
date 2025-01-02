package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Data
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_info")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;  // 복합키로 FileId 클래스를 사용

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "file_uuid")
    private String fileUuid;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private double fileSize;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getSaveName(){
        return this.fileName + "." + this.fileType;
    }

    public Path getSavePath(){
        return Paths.get(this.getFilePath(), this.getSaveName());
    }
}