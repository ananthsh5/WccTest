package com.app.wcc.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 * Entity class for Post Code history
 */
@Data
@Entity
public class PostCodeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postcode1;

    @Column(nullable = false)
    private String postcode2;
    
    @CreatedDate
    private Timestamp createdDate;

    @CreatedBy
    private String createdBy;


}
