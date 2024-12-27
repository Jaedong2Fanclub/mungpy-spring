package com.jaefan.munpyspring.region.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.jaefan.munpyspring.region.domain.model.Region;
import com.jaefan.munpyspring.region.domain.repository.RegionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionService {

	private final RegionRepository regionRepository;

	/**
	 * 데이터베이스에 행정구역에 대한 데이터가 없을 때 사용하는 일회용 메서드
	 */
	public void initRegions() {
		final String filePath = "src/main/resources/region.csv";

		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				regionRepository.save(Region.builder()
					.upper(values[0])
					.lower(values[1])
					.build());
			}
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}
}
