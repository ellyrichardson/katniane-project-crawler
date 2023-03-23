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
import org.springframework.web.bind.annotation.RestController;

import com.katniane.projectcrawler.dataaccesslayer.CrawlerProjectRepository;
import com.katniane.projectcrawler.dataaccesslayer.SystemConfigurationRepository;
import com.katniane.projectcrawler.domain.CrawlerProject;
import com.katniane.projectcrawler.domain.SystemConfiguration;
import com.katniane.projectcrawler.utility.CpeExtractor;
import com.katniane.projectcrawler.utility.SftpHelper;

@RestController
public class ProjectCrawlerController {
	
	@Autowired
	private CrawlerProjectRepository crawlerProjectRepository;
	
	@Autowired
	private SystemConfigurationRepository systemConfigRepository;
	
	@Autowired
	private CpeExtractor cpeExtractor;
	
	@Autowired
	private SftpHelper sftpHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectCrawlerController.class);
	
	@GetMapping(value = "/scanRequest/{projectType}/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void scanRequest(@PathVariable String projectType, @PathVariable Integer projectId) {
		LOG.info("Received scan request for project {} of type {}", projectId, projectType);
		try {
			evaluateRequestType(projectType, projectId);
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void evaluateRequestType(String projectType, Integer projectId) throws FileSystemException {
		// NOTE: Only jar files are supported right now
		if (projectType.equals("jar")) {
			CrawlerProject crawlerProject = crawlerProjectRepository.findById(projectId).get();
			String crawlerProjectsJarDir = "crawler_projects_jar_dir";
			SystemConfiguration systemConfig = systemConfigRepository.findById(crawlerProjectsJarDir).get();
			
			if (systemConfig == null) {
				// Throw error here or raise alert
				LOG.error("System Configuration of id {} is not found", crawlerProjectsJarDir);
			}
			
			if (crawlerProject != null ) {
				handleCpeExtractionForJar(crawlerProject, systemConfig);
			} else {
				// Throw error here or raise alert
				LOG.error("Crawler Project of id {} is not found", projectId);
			}
		} else {
			// TODO: Throw error here
			LOG.error("Unsupported Project Type {} received", projectType);
		}
	}
	
	private void handleCpeExtractionForJar(CrawlerProject crawlerProject, SystemConfiguration systemConfig) throws FileSystemException {
		String fileName = crawlerProject.getProjectName()
				.concat("+")
				.concat(crawlerProject.getProjectType());
		
		sftpHelper.downloadFileFromHost(fileName, "ip-of-the-sftp-box");
		
		// Run the scanning of the file here
		String jarDir = systemConfig.getConfigurationName()
				.concat("/")
				.concat(fileName);
		
		List<String> foundCpes = cpeExtractor.extractCpeForJar(jarDir);
		if (foundCpes.size() > 0 || foundCpes != null) {
			LOG.info("Found CPEs for project with name of {}", crawlerProject.getProjectName());
			// TODO: Check vulnerabilities for the CPEs found
		}
	}

}
