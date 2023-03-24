package com.katniane.projectcrawler.service;

import org.apache.commons.vfs2.FileSystemException;

public interface SftpProcessManagerService {
	public void downloadFileFromHost(String remoteFile, String remoteHost) throws FileSystemException;
}	
