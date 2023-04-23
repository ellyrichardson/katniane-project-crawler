package com.katniane.projectcrawler.domain;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "kn_krawler_project_artifact")
public class CrawlerProjectArtifact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	// NOTE: Not sure if this works, but the DB table has foreign key to kn_crawler_project
	@OneToMany
	private CrawlerProject knCrawlerProject;
	
	private String artifactName;
	
	private String artifactType;
	
	private ZonedDateTime uploadDateTime;
	
	private ZonedDateTime lastScannedDateTime;
}
