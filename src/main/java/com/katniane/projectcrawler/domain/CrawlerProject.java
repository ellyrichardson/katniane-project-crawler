package com.katniane.projectcrawler.domain;

import lombok.Data;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "kn_krawler_project")
public class CrawlerProject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String projectName;
	
	private String projectType;
	
	private String projectOwner;
	
	private ZonedDateTime createDateTime;
	
	private ZonedDateTime lastModifiedDateTime;
}
