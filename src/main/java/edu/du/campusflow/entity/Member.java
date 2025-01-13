package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    private Long memberId;
    // 자동으로 상승하는 값이 아님.

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept dept; // Dept 테이블과 연관

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "tel")
    private String tel;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "gender")
    private CommonCode gender; // CommonCode 테이블과 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "academic_status")
    private CommonCode academicStatus; // CommonCode 테이블과 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "grade")
    private CommonCode grade; // CommonCode 테이블과 연관

    @ManyToOne
    @JoinColumn(name = "member_type")
    private CommonCode memberType; // CommonCode 테이블과 연관, 보통 필요해서 일단 Lazy 안달음

    @Column(name = "start_date")
    private Date startDate; // 입학, 임용 날짜

    @Column(name = "end_date")
    private Date endDate; // 졸업, 퇴직 날짜

    public boolean isStudent(){
        return this.memberType!=null&&this.getMemberType().getCodeValue().equals("STUDENT");
    }

    public boolean isProfessor(){
        return this.memberType!=null&&this.getMemberType().getCodeValue().equals("PROFESSOR");
    }

    public boolean isStaff(){
        return this.memberType!=null&&this.getMemberType().getCodeValue().equals("STAFF");
    }

    public boolean equals(Long memberId){
        return this.memberId!=null&&this.memberId.equals(memberId);
    }
}