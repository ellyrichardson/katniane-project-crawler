package com.katniane.projectcrawler.dataaccesslayer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.katniane.projectcrawler.domain.CrawlerProjectArtifact;

public interface CrawlerProjectArtifactRepository extends CrudRepository<CrawlerProjectArtifact, Integer> {
	@Query("select * from CrawlerProjectArtifact cpa where cpa.crawlerProject.id=:crawlerProjectId")
	public CrawlerProjectArtifact findByCrawlerProjectId(@Param("crawlerProjectId") Integer crawlerProjectId);
}