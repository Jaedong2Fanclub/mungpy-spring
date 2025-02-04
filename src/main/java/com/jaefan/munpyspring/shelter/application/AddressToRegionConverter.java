package com.jaefan.munpyspring.shelter.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.region.domain.model.Region;
import com.jaefan.munpyspring.region.domain.repository.RegionRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 문자열 주소를 Region 엔티티로 라벨링하는 모듈
 */
@Component
@RequiredArgsConstructor
public class AddressToRegionConverter {
	private final RegionRepository regionRepository;
	private Map<String, List<Region>> regionMap = new HashMap<>(); // 전체 지역 정보 HashMap Key: 지역 대분류 ex) 서울시, Value: 지역 대분류에 속하는 Regions 리스트

	@PostConstruct // 빈 초기화 시 한번만 수행
	private void initializeRegionMap() {
		List<Region> regions = regionRepository.findAll(); // 모든 지역을 지역 리포지토리에서 조회

		for (Region region : regions) { // regionMap 초기화. ex) key: 서울시, value: List<서울시에 속하는 Region들>
			regionMap.computeIfAbsent(region.getUpper(), upperNewArray -> new ArrayList<>()).add(region);
		}

		/**
		 * regionMap의 Value인 List<Region> 안에서 Region lower의 길이가 길수록 앞으로 오도록 정렬합니다.
		 * lower가 길수록 앞에 오는 이유는 getRegionFromAddress 메소드에서 지역을 라벨링할 때 하위 지역 매칭이 더 긴 경우를 먼저 비교해보기 위함입니다.
		 * ex) 경기 고양시 일산동구 xx아파트 xxx동 xxxx호 -> (경기, 고양시) 보단 (경기, 고양시 일산동구)를 먼저 비교해봅니다.
		 */
		for (List<Region> regionList : regionMap.values()) {
			regionList.sort((r1, r2) -> Integer.compare(r2.getLower().length(), r1.getLower().length()));
		}
	}

	/**
	 * 문자열 주소를 기반으로 regionMap 안에서 매칭되는 Region을 리턴합니다.
	 */
	public Region getRegionFromAddress(String address) {
		for (String upper : regionMap.keySet()) { // 대분류 주소
			if (address.contains(upper)) { // 문자열 주소가 현재 대분류 주소를 포함하면
				for (Region region : regionMap.get(upper)) { // 소분류 주소를 비교합니다. (긴 소분류 주소 먼저)
					if (address.contains(region.getLower())) { // 매칭된다면 region 리턴
						return region;
					}
				}
			}
		}
		return null; // 적합한 Region을 찾지 못하면 null 반환
	}
}
