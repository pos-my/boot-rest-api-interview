package posmy.interview.boot.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
    
	private String description;
    
	private String borrowerName;
	
	private String status;
    
	private String remarks;
	
	@CreationTimestamp
	private Date createdDate;
	@UpdateTimestamp
	private Date lastUpdateDate;
}
