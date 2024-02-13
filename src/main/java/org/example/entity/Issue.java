package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "id")
    private POS pos;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "id")
    private IssueType mainType;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "id")
    private IssueType subType;
    @Size(min = 5, max = 25)
    private String title;
    @Min(1) @Max(5)
    private Integer priority;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "id")
    private Status status;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "id")
    @CreatedBy
    private User createdBy;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "id")
    private User assignedTo;
    @Size(min = 10, max = 255)
    private String description;
    @Temporal(TemporalType.DATE)
    private Date assignedDate;
    @CreatedDate
    private Date creationDate;
    @LastModifiedDate
    private Date modifiedDate;
    private String solution;
}
