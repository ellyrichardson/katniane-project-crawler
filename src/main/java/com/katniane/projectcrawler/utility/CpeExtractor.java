package com.katniane.projectcrawler.utility;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyGraphBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.DefaultProjectDependenciesResolver;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectDependenciesResolver;

@Service
public class CpeExtractor {
	
	public List<String> extractCpeForJar(String jarDir) {
        File jarFile = new File(jarDir);
        return extractCpeNames(jarFile);
    }

	private List<String> extractCpeNames(File jarFile) {
		ProjectBuildingRequest request = new DefaultProjectBuildingRequest();
		ArtifactFilter filter = new AndArtifactFilter();
		ProjectDependenciesResolver resolver = new DefaultProjectDependenciesResolver();
        DependencyGraphBuilder builder = new DefaultDependencyGraphBuilder(resolver);
        try {
            DependencyNode nodeWithGraph = builder.buildDependencyGraph(request, filter);
            
            return nodeWithGraph.getChildren().stream()
                    .map(dependency -> dependency.getArtifact().toString())
                    .filter(artifactString -> artifactString.contains(":"))
                    .map(artifactString -> {
                        String[] split = artifactString.split(":");
                        return "cpe:/a:" + split[0] + ":" + split[1] + ":" + split[3];
                    })
                    .distinct()
                    .collect(Collectors.toList());
        } catch (DependencyGraphBuilderException e) {
        	System.out.println("Failed to extract dependencies from " + jarFile);
			e.printStackTrace();
			return Collections.emptyList();
		}
    }
	
}


