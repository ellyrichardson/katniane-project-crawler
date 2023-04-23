package com.katniane.projectcrawler.coordination;

import com.katniane.projectcrawler.domain.SystemConfiguration;

public interface CrawlerProjectConfigurationProcessorService {
	
	public SystemConfiguration retrieveConfigForCrawlerProjectType(String projectType) throws Exception;
	
}
