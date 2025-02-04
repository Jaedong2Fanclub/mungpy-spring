package com.jaefan.munpyspring.shelter.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jaefan.munpyspring.region.domain.model.Region;
import com.jaefan.munpyspring.region.domain.repository.RegionRepository;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.shelter.domain.repository.ShelterRepository;
import com.jaefan.munpyspring.user.domain.model.Provider;
import com.jaefan.munpyspring.user.domain.model.Role;
import com.jaefan.munpyspring.user.domain.model.Status;
import com.jaefan.munpyspring.user.domain.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShelterInitializeService {

	public static final long DELAY_SECONDS = 1L;
	private static final String URL = "https://www.animal.go.kr/front/awtis/institution/institutionList.do?menuNo=1000000059";

	private final WebDriver webDriver;
	private final RegionRepository regionRepository;
	private final ShelterRepository shelterRepository;

	@Transactional
	public void init() {
		webDriver.get(URL);

		int pageSize = Integer.parseInt(
			webDriver.findElement(By.className("count")).findElement(By.tagName("span")).getText()
		);

		webDriver.get(URL + "&pageSize=" + pageSize);

		for (int i = 0; i < pageSize; i++) {
			WebElement tbody = webDriver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			WebElement tr = tbody.findElements(By.tagName("tr")).get(i);

			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(tr));

			tr.findElements(By.tagName("td")).get(1).click();

			log.info("{} 번째 보호소", i + 1);
			saveCrawledShelter();

			webDriver.navigate().back();
			delay();
		}
	}

	private void saveCrawledShelter() {
		WebElement tbody = webDriver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> trList = tbody.findElements(By.tagName("tr"));

		Shelter.ShelterBuilder shelterBuilder = Shelter.builder();
		for (WebElement tr : trList) {
			List<WebElement> thList = tr.findElements(By.tagName("th"));
			List<WebElement> tdList = tr.findElements(By.tagName("td"));

			for (int i = 0; i < thList.size(); i++) {
				String th = thList.get(i).getText();
				String td = tdList.get(i).getText();

				switch (th) {
					case "보호센터명" -> shelterBuilder.name(td);
					case "전화번호" -> shelterBuilder.telNo(td);
					case "보호센터주소" -> {
						shelterBuilder.address(td);
						shelterBuilder.region(getRegion(td));
					}
					case "위도" -> {
						if (StringUtils.hasText(td)) {
							shelterBuilder.longitude(Double.parseDouble(td));
						}
					}
					case "경도" -> {
						if (StringUtils.hasText(td)) {
							shelterBuilder.latitude(Double.parseDouble(td));
						}
					}
					default -> {
					}
				}
			}
		}

		LocalDateTime now = LocalDateTime.now();
		User testUser = User.builder()
			.uuid(UUID.randomUUID())
			.nickname("test user")
			.providerType(Provider.DEFAULT)
			.email("test@gmail.com")
			.status(Status.ACTIVE)
			.password("test123")
			.createdAt(now)
			.visitedAt(now)
			.role(Role.SHELTER)
			.build();

		shelterBuilder.user(testUser);

		Shelter shelter = shelterBuilder.build();

		if (shelterRepository.findByNameAndTelNo(shelter.getName(), shelter.getTelNo()).isEmpty()) {
			shelterRepository.save(shelter);
		}
	}

	private Region getRegion(String input) {
		String[] address = input.split(" ");
		String upper = switch (address[0]) {
			case "경상북도" -> "경북";
			case "경상남도" -> "경남";
			case "전라북도" -> "전북";
			case "전라남도" -> "전남";
			case "충청북도" -> "충북";
			case "충청남도" -> "충남";
			default -> address[0].substring(0, 2);
		};
		String lower = address[1];

		return regionRepository.findByUpperAndLower(upper, lower).orElseGet(() -> {
			if (upper.equals("세종")) {
				return regionRepository.findByUpperAndLower(upper, "세종시").get();
			} else {
				throw new NoSuchElementException("Region not found");
			}
		});
	}

	private void delay() {
		try {
			Thread.sleep(1000 * DELAY_SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
