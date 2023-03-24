package com.katniane.projectcrawler.dataaccesslayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.katniane.projectcrawler.domain.CrawlerProject;

public interface CrawlerProjectRepository extends CrudRepository<CrawlerProject, Integer> {
}
