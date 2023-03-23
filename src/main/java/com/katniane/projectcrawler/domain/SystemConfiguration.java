package com.katniane.projectcrawler.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "kn_system_configuration")
public class SystemConfiguration {
	
	@Id
    private String configurationName;
	
	private String configurationValue;
}
