package com.katniane.projectcrawler.utility;

import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Service
public class SftpHelper {
	
	private FileSystemManager manager;
	
	public SftpHelper() {
		try {
			this.manager = VFS.getManager();
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void downloadFileFromHost(String remoteFile, String remoteHost) throws FileSystemException {
		
		// TODO: Must pull these values from a config file accessible by the user
		
		String localDir = "/tmp/";
		String username = "test";
		String password = "password";
		
		FileObject local = manager.resolveFile(System.getProperty("user.dir") + "/" + localDir + "vfsFile.txt");
		FileObject remote = manager.resolveFile("sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFile);
		
		local.copyFrom(remote, Selectors.SELECT_SELF);
		
		local.close();
		remote.close();
	}
}
