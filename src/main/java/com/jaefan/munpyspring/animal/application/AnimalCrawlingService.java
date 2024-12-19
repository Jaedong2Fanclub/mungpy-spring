package com.jaefan.munpyspring.animal.application;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.animal.application.util.AnimalConverter;
import com.jaefan.munpyspring.animal.domain.model.CrawlingCategory;
import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animal.domain.model.PublicAnimal;
import com.jaefan.munpyspring.animal.domain.repository.ProtectionAnimalRepository;
import com.jaefan.munpyspring.animal.domain.repository.PublicAnimalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalCrawlingService {

	public static final long DELAY_SECONDS = 1L;
	private static final String BASE_URL = "https://www.animal.go.kr/front/awtis/";
	private static final String PUBLIC_URL = "public/publicList.do?menuNo=1000000055";
	private static final String PROTECTION_URL = "protection/protectionList.do?menuNo=1000000060";
	private static final String RELEASE_URL = "public/publicAllList.do?menuNo=1000000064";

	private final WebDriver webDriver;
	private final AnimalConverter animalConverter;
	private final PublicAnimalRepository publicAnimalRepository;
	private final ProtectionAnimalRepository protectionAnimalRepository;

	@Transactional
	public void crawl(CrawlingCategory category) {
		String baseUrl = BASE_URL;
		baseUrl += switch (category) {
			case PUBLIC -> PUBLIC_URL;
			case PROTECTION -> PROTECTION_URL;
			case RELEASE -> RELEASE_URL;
		};

		String url = makeUrl(baseUrl);
		webDriver.get(url);

		int pageSize = Integer.parseInt(
			webDriver.findElement(By.className("count")).findElement(By.tagName("span")).getText()
		);
		webDriver.get(url + "&pageSize=" + pageSize);

		WebElement ulElement = webDriver.findElement(By.className("animals-list"));
		List<WebElement> liElements = ulElement.findElements(By.xpath("./li"));

		for (int i = 0; i < liElements.size(); i++) {
			if (category.equals("protection")) {
				ulElement = webDriver.findElement(By.className("animals-list"));
				liElements = ulElement.findElements(By.xpath("./li"));
			}

			WebElement li = liElements.get(i);

			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(li));

			li.click();

			if (category.equals("protection")) {
				removePopup();
			}

			saveCrawledAnimal(category);

			webDriver.navigate().back();
			delay();
		}
	}

	private void saveCrawledAnimal(CrawlingCategory category) {
		Map<String, String> map = new HashMap<>();
		List<WebElement> images = webDriver.findElements(By.className("photoArea"));

		switch (category) {
			case PUBLIC -> {
				extractTableInfo(map, "동물의 정보");
				extractTableInfo(map, "구조 정보");
				extractTableInfo(map, "동물보호센터 안내");

				// TODO : GCP에 사진 업로드하는 로직 구현

				PublicAnimal publicAnimal = animalConverter.convertMapToPublic(map);
				if (publicAnimal != null) {
					log.info(publicAnimal.toString());

					boolean isAlreadyExist = publicAnimalRepository.findByNoticeNo(publicAnimal.getNoticeNo())
						.isPresent();
					if (!isAlreadyExist) {
						publicAnimalRepository.save(publicAnimal);
					}
				}
			}
			case PROTECTION -> {
				extractTableInfo(map);

				ProtectionAnimal protectionAnimal = animalConverter.convertMapToProtection(map);
				if (protectionAnimal != null) {
					log.info(protectionAnimal.toString());

					publicAnimalRepository.findByNoticeNo(protectionAnimal.getNoticeNo())
						.ifPresent(publicAnimal -> {
							publicAnimal.closeAnnouncement();
							// TODO : 실종 동물의 이미지를 보호 동물로 이전하는 로직 필요 (병합 후에)
						});

					boolean isAlreadyExist = protectionAnimalRepository.findByNoticeNo(
							protectionAnimal.getNoticeNo())
						.isPresent();
					if (!isAlreadyExist) {
						protectionAnimalRepository.save(protectionAnimal);
					}
				}
			}
			case RELEASE -> {
				extractTableInfo(map);

				String noticeNo = map.get("공고번호");
				publicAnimalRepository.findByNoticeNo(noticeNo).ifPresent(PublicAnimal::closeProtection);
				protectionAnimalRepository.findByNoticeNo(noticeNo).ifPresent(ProtectionAnimal::closeProtection);
			}
		}
	}

	private void removePopup() {
		String originalWindow = webDriver.getWindowHandle();
		webDriver.getWindowHandles().forEach(window -> {
			if (!window.equals(originalWindow)) {
				webDriver.switchTo().window(window);
				webDriver.close();
			}
		});
		webDriver.switchTo().window(originalWindow);
	}

	private void extractTableInfo(Map<String, String> map) {
		WebElement tbody = webDriver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		parseTableRows(map, tbody);
	}

	private void extractTableInfo(Map<String, String> map, String title) {
		WebElement table = webDriver.findElement(By.xpath("//h4[.='" + title + "']/following-sibling::table"));
		WebElement tbody = table.findElement(By.tagName("tbody"));
		parseTableRows(map, tbody);
	}

	private void parseTableRows(Map<String, String> map, WebElement tbody) {
		tbody.findElements(By.tagName("tr")).forEach(tr -> {
			List<WebElement> thList = tr.findElements(By.tagName("th"));
			List<WebElement> tdList = tr.findElements(By.tagName("td"));

			for (int i = 0; i < thList.size(); i++) {
				String thText = thList.get(i).getText();
				String tdText = tdList.get(i).getText();

				if (thText.equals("건강검진") || thText.equals("접종상태")) {
					List<WebElement> inputList = tdList.get(i).findElements(By.tagName("input"));
					tdText = getCheckedString(inputList);
				}

				map.put(thText, tdText);
			}
		});
	}

	private static String getCheckedString(List<WebElement> inputList) {
		StringBuilder checked = new StringBuilder();
		for (WebElement input : inputList) {
			boolean isChecked = input.getAttribute("checked") != null;
			checked.append(isChecked ? "O" : "X");
		}

		return checked.toString();
	}

	private String makeUrl(String baseUrl) {
		StringBuilder url = new StringBuilder(baseUrl);

		LocalDate today = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		url.append("&searchSDate=").append(today.format(format)); // 조회 시작일
		url.append("&searchEDate=").append(today.format(format)); // 조회 종료일

		return url.toString();
	}

	private void delay() {
		try {
			Thread.sleep(1000 * DELAY_SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
