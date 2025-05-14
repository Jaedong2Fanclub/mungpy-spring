package com.jaefan.munpyspring.region;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jaefan.munpyspring.region.application.RegionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class RegionServiceTest {

	@Autowired
	RegionService regionService;

	@Test
	void getAllRegions() {
		Map<String, List<String>> regions = regionService.getAllRegions();

		log.info(regions.keySet().toString());
		assertThat(regions.keySet()).isNotEmpty();
	}
}
