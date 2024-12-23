package edu.du.academic_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileId implements Serializable {

    private Long fileId;
    private Long fileNo;

    // equals와 hashCode 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileId fileId1 = (FileId) o;
        return Objects.equals(fileId, fileId1.fileId) && Objects.equals(fileNo, fileId1.fileNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileNo);
    }
}
