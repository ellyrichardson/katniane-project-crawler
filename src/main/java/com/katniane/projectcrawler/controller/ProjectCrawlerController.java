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
import com.katniane.projectcrawler.dataaccesslayer.CrawlerProjectArtifactRepository;
import com.katniane.projectcrawler.dataaccesslayer.CrawlerProjectRepository;
import com.katniane.projectcrawler.dataaccesslayer.SystemConfigurationRepository;
import com.katniane.projectcrawler.domain.CrawlerProject;
import com.katniane.projectcrawler.domain.CrawlerProjectArtifact;
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
	private CrawlerProjectArtifactRepository crawlerProjectArtifactRepository;
	
	@Autowired
	private CpeExtractionManagerService cpeExtractor;
	
	@Autowired
	private SftpProcessManagerService sftpProcessManager;
	
	@Autowired
	private CrawlerProjectConfigurationProcessorService crawlerProjectConfigurationProcessor;
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectCrawlerController.class);
	
	/*
	 * TODO: Might have to modify params or add another parameter here for the `projectName` and `artifactName`
	 *  This update will include new table that is foreign-keyed with the crawler_projects table
	 *  and also update to the `scanRequest` logic below to utilize an object like CrawlerProjectArtifact
	 * */
	@GetMapping(value = "/scanRequest/{projectType}/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void scanRequest(@PathVariable String projectType, @PathVariable Integer projectId) {
		LOG.info("Received scan request for project {} of type {}", projectId, projectType);
		try {
			SystemConfiguration requestTypeConfig = crawlerProjectConfigurationProcessor.retrieveConfigForCrawlerProjectType(projectType);
			
			CrawlerProject crawlerProject = crawlerProjectRepository.findById(projectId).get();
			if (crawlerProject != null ) {
				handleCpeExtractionForJar(crawlerProject, requestTypeConfig);
			} else {
				// Throw error here or raise alert
				LOG.error("Crawler Project of id {} is not found", projectId);
			}
			
			// TODO: somewhere here, add a logic to update the CrawlerProject last scan date
			
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * TODO: Looks into refining the logic for retrieving the right jar for a Crawler Project. This is because Crawler Projects
	 * 	artifacts can have new names for newer artifacts, and the Crawler app must be able to retrieve those
	 * */
	private void handleCpeExtractionForJar(CrawlerProject crawlerProject, SystemConfiguration systemConfig) throws Exception {
		if (crawlerProject != null ) {
			//handleCpeExtractionForJar(crawlerProject, systemConfig);
			
			
			CrawlerProjectArtifact crawlerProjectArtifact = crawlerProjectArtifactRepository.findByCrawlerProjectId(crawlerProject.getId());
			
			String fileDir = crawlerProject.getProjectName()
					.concat("/")
					.concat(crawlerProjectArtifact.getArtifactName())
					.concat(crawlerProjectArtifact.getArtifactType());
			
			LOG.info("Retrieving file with name: {} from SFTP server", fileDir);
			sftpProcessManager.downloadFileFromHost(fileDir, "0.0.0.0:22");
			
			// Run the scanning of the file here
			String jarDir = systemConfig.getConfigurationValue()
					.concat("/")
					.concat(fileDir);
			
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
