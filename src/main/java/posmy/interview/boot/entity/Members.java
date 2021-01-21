/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import posmy.interview.boot.enums.Gender;

/**
 *
 * @author syahirghariff
 */
@Entity
@Table(name="MEMBERS")
public class Members implements Serializable {
    
    
    @Id
    @Column(name="ID")
    private String id; 
    
    @Column(name="USER_ID")
    private String userId;
    
    @Column(name="NAME")
    private String name; 
    
    @Column(name="AGE")
    private Integer age; 
    
    @Column(name="GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender; 
    
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date createdDate;
    
    @Transient
    private String username; 
    
    @OneToOne
    @JoinColumn(name="USER_ID", insertable = false, updatable = false)
    private User user; 

    public Members() {
    }
    
    public static final Members createMember(Members req, User user) {
    
        Members member = new Members(req);
        member.setUserId(user.getId());
        return member;
    }
    
    public Members (Members req) {
    
        this.id = req.getId() != null ? req.getId() : UUID.randomUUID().toString();
        this.userId = req.getUserId() != null ? req.getUserId() : null;
        this.name = req.getName().trim();
        this.age = req.getAge(); 
        this.gender = req.getGender(); 
        this.createdDate = req.getCreatedDate() != null? req.getCreatedDate() : new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return "Members{" + "id=" + id + ", user=" + user + ", name=" + name + ", age=" + age + ", gender=" + gender + ", createdDate=" + createdDate + '}';
    }
    
}
