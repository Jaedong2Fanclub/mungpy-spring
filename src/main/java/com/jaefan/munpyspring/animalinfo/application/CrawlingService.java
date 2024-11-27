package com.jaefan.munpyspring.animalinfo.application;

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

import com.jaefan.munpyspring.animalinfo.application.util.AnimalConverter;
import com.jaefan.munpyspring.animalinfo.domain.model.PublicAnimal;
import com.jaefan.munpyspring.animalinfo.domain.repository.PublicAnimalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

	public static final long DELAY_SECONDS = 1L;
	private static final String URL = "https://www.animal.go.kr/front/awtis/";
	private static final String PUBLIC = "public/publicList.do?menuNo=1000000055";
	private static final String PROTECTION = "protection/protectionList.do?menuNo=1000000060";

	private final WebDriver webDriver;
	private final AnimalConverter animalConverter;
	private final PublicAnimalRepository publicAnimalRepository;

	@Transactional
	public void crawl(String category) {
		String baseUrl = URL;
		baseUrl += category.equals("public") ? PUBLIC : PROTECTION;

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

			Map<String, String> map = new HashMap<>();

			List<WebElement> images = webDriver.findElements(By.className("photoArea"));
			switch (category) {
				case "public" -> {
					extractTableInfo(map, "동물의 정보");
					extractTableInfo(map, "구조 정보");
					extractTableInfo(map, "동물보호센터 안내");

					// for (int j = 0; j < images.size(); j++) {
					// 	String savePath = String.format("src/main/resources/images/%s(%d).jpg", map.get("공고번호"), j + 1);
					// 	ImageDownloader.downloadImage(images.get(j).getAttribute("src"), savePath);
					// }

					PublicAnimal publicAnimal = animalConverter.convertMapToPublic(map);
					if (publicAnimal != null) {
						log.info(publicAnimal.toString());
						publicAnimalRepository.save(publicAnimal);
					}
				}
				case "protection" -> {
					extractTableInfo(map);
				}
			}

			log.info(map.toString());

			webDriver.navigate().back();
			delay();
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
				map.put(thList.get(i).getText(), tdList.get(i).getText());
			}
		});
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
