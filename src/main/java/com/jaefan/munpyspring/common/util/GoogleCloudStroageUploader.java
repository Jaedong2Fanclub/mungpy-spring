package com.jaefan.munpyspring.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;

/**
 * Google Cloud Storage 이미지 업로더
 */
@Component
@RequiredArgsConstructor
public class GoogleCloudStroageUploader {

	@Value("${spring.cloud.gcp.storage.bucket}")
	private String bucketName;

	private final Storage storage;

	public List<String> upload(List<MultipartFile> files, String dirName) throws IOException {
		List<String> imageUrls = new ArrayList<>();

		for (MultipartFile file : files) {
			imageUrls.add(upload(file, dirName));
		}
		return imageUrls;
	}

	public String upload(MultipartFile file, String dirName) throws IOException {
		String uuid = UUID.randomUUID().toString();
		String ext = file.getContentType();
		String filePath = dirName + "/" + uuid;

		BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filePath)
			.setContentType(ext)
			.build();

		Blob blob = storage.create(blobInfo, file.getBytes());

		return "https://storage.googleapis.com/" + bucketName + "/" + filePath;
	}
}
