package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@ToString
@Table(name = "member")
public abstract class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    
    @Column(length = 100)
    private String password;
    
    @Column(length = 20)
    private String name;
    
    @Column(length = 11)
    private String tel;
    
    @Column(length = 200)
    private String address;
    
    private LocalDate birthDate;
    
    @Column(columnDefinition = "BOOLEAN")
    private Boolean isActive;
    
    private LocalDateTime createAt;
    
    private LocalDateTime updateAt;
    
    @Column(length = 100)
    private String email;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_code_id")
    private CommonCode gender;

    private Long imageFileId;

    public String getRole(){
        return this.getClass().getSimpleName().toUpperCase();
    }

}