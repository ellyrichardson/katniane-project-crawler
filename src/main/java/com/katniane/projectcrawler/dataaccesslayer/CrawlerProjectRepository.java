package com.katniane.projectcrawler.dataaccesslayer;

import org.springframework.data.repository.CrudRepository;

import com.katniane.projectcrawler.domain.CrawlerProject;

public interface CrawlerProjectRepository extends CrudRepository<CrawlerProject, Integer> {
}
