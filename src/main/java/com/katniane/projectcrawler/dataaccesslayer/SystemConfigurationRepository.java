package com.katniane.projectcrawler.dataaccesslayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.katniane.projectcrawler.domain.SystemConfiguration;

@Repository
public interface SystemConfigurationRepository extends CrudRepository<SystemConfiguration, String> {

}
