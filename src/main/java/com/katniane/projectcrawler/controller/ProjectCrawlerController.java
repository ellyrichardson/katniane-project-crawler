package com.katniane.projectcrawler.controller;

import java.util.List;

import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.katniane.projectcrawler.coordination.CrawlerProjectConfigurationProcessorService;
import com.katniane.projectcrawler.dataaccesslayer.CrawlerProjectRepository;
import com.katniane.projectcrawler.dataaccesslayer.SystemConfigurationRepository;
import com.katniane.projectcrawler.domain.CrawlerProject;
import com.katniane.projectcrawler.domain.SystemConfiguration;
import com.katniane.projectcrawler.service.CpeExtractionManagerService;
import com.katniane.projectcrawler.service.CpeExtractionManager;
import com.katniane.projectcrawler.service.SftpProcessManagerService;
import com.katniane.projectcrawler.service.SftpProcessManager;

@RestController
@RequestMapping(value = "/")
public class ProjectCrawlerController {
	
	@Autowired
	private CrawlerProjectRepository crawlerProjectRepository;
	
	@Autowired
	private CpeExtractionManagerService cpeExtractor;
	
	@Autowired
	private SftpProcessManagerService sftpProcessManager;
	
	@Autowired
	private CrawlerProjectConfigurationProcessorService crawlerProjectConfigurationProcessor;
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectCrawlerController.class);
	
	@GetMapping(value = "/scanRequest/{projectType}/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void scanRequest(@PathVariable String projectType, @PathVariable Integer projectId) {
		LOG.info("Received scan request for project {} of type {}", projectId, projectType);
		try {
			SystemConfiguration requestTypeConfig = crawlerProjectConfigurationProcessor.retrieveConfigForCrawlerProjectType(projectType, projectId);
			
			CrawlerProject crawlerProject = crawlerProjectRepository.findById(projectId).get();
			if (crawlerProject != null ) {
				handleCpeExtractionForJar(crawlerProject, requestTypeConfig);
			} else {
				// Throw error here or raise alert
				LOG.error("Crawler Project of id {} is not found", projectId);
			}
			
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void handleCpeExtractionForJar(CrawlerProject crawlerProject, SystemConfiguration systemConfig) throws Exception {
		if (crawlerProject != null ) {
			//handleCpeExtractionForJar(crawlerProject, systemConfig);
			String fileName = crawlerProject.getProjectName()
					.concat("+")
					.concat(crawlerProject.getProjectType());
			
			sftpProcessManager.downloadFileFromHost(fileName, "ip-of-the-sftp-box");
			
			// Run the scanning of the file here
			String jarDir = systemConfig.getConfigurationName()
					.concat("/")
					.concat(fileName);
			
			List<String> foundCpes = cpeExtractor.extractCpeForJar(jarDir);
			if (foundCpes.size() > 0 || foundCpes != null) {
				LOG.info("Found CPEs for project with name of {}", crawlerProject.getProjectName());
				// TODO: Check vulnerabilities for the CPEs found
			}
		} else {
			// Throw error here or raise alert
			throw new Exception();
		}
	}

}
