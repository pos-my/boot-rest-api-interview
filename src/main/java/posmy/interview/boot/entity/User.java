/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import posmy.interview.boot.enums.ActiveStatus;
import posmy.interview.boot.enums.Roles;

/**
 *
 * @author syahirghariff
 */

@Entity
@Table(name="USER")
public class User implements Serializable {
    
    @Id
    @Column(name="ID")
    private String id; 
    
    @Column(name="USERNAME")
    private String username; 
    
    
    @Column(name="PASSWORD")
    @JsonIgnore
    private String password; 
    
    @Column(name="ROLE")
    @Enumerated(EnumType.STRING)
    private Roles role; 
    
    @Column(name="ACTIVE")
    @Enumerated(EnumType.STRING)
    private ActiveStatus active; 
    
    @Column(name="CREATED_DATE")
    private Date createdDate;
    
    private static final String DEFAULT_PASSWORD = "PASSWORD";

    public User() {
    }
    
    public static final User createMembers(String username){
        
        User user = new User(); 
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(DEFAULT_PASSWORD);
        user.setRole(Roles.ROLE_MEMBERS);
        user.setActive(ActiveStatus.A);
        user.setCreatedDate( new Date());
        
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ActiveStatus getActive() {
        return active;
    }

    public void setActive(ActiveStatus active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", active=" + active + ", createdDate=" + createdDate + '}';
    }
    
}
