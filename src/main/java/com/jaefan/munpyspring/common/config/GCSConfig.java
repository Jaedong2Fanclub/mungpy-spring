package com.jaefan.munpyspring.common.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * Google Cloud Storage Configuration
 */
@Configuration
public class GCSConfig {

	@Value("${spring.cloud.gcp.storage.credentials.location}")
	private String keyFileLocation; // Google Cloud Storage 인증 Key File 경로 위치 (실제 경로에 존재해야 함.)

	@Bean
	public Storage storage() throws IOException { // Google Cloud Storage와 상호작용하기 위한 Storage 클라이언트
		InputStream keyFile = ResourceUtils.getURL(keyFileLocation).openStream();
		return StorageOptions.newBuilder()
			.setCredentials(GoogleCredentials.fromStream(keyFile))
			.build()
			.getService();
	}
}
