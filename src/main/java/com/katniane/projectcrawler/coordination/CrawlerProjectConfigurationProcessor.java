package com.katniane.projectcrawler.coordination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.katniane.projectcrawler.controller.ProjectCrawlerController;
import com.katniane.projectcrawler.dataaccesslayer.SystemConfigurationRepository;
import com.katniane.projectcrawler.domain.SystemConfiguration;

@Service
public class CrawlerProjectConfigurationProcessor implements CrawlerProjectConfigurationProcessorService {
	
	@Autowired
	private SystemConfigurationRepository systemConfigRepository;
	
	private static final Logger LOG = LoggerFactory.getLogger(CrawlerProjectConfigurationProcessor.class);

	@Override
	public SystemConfiguration retrieveConfigForCrawlerProjectType(String projectType, Integer projectId) throws Exception {
		SystemConfiguration systemConfig = null;
		String crawlerProjectsJarDir = "crawler_projects_jar_dir";
		
		// NOTE: Only jar files are supported right now
		if (projectType.equals("jar")) {
			systemConfig = systemConfigRepository.findById(crawlerProjectsJarDir).get();	
		} else {
			// TODO: Throw error here
			LOG.error("Unsupported Project Type {} received", projectType);
			throw new Exception();
		}
		
		if (systemConfig == null) {
			// Throw error here or raise alert
			LOG.error("System Configuration of id {} is not found", crawlerProjectsJarDir);
			throw new Exception();
		} else {
			return systemConfig;
		}
	}

}
