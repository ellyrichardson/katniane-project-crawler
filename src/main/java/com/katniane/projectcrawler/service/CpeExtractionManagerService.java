package com.katniane.projectcrawler.service;

import java.util.List;

public interface CpeExtractionManagerService {
	public List<String> extractCpeForJar(String jarDir);
}
