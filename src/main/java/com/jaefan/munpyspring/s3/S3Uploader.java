package com.jaefan.munpyspring.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3Uploader {

	private final AmazonS3 amazonS3;
	private final String bucket;

	public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
		this.amazonS3 = amazonS3;
		this.bucket = bucket;
	}

	public List<String> upload(List<MultipartFile> multipartFiles, String dirName) throws IOException {
		List<String> s3Urls = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			String originalFileName = multipartFile.getOriginalFilename(); // 파일명 추출

			String uuid = UUID.randomUUID().toString(); // S3상에서 파일명 중복을 대비해 UUID 생성
			String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_"); // 공백(\\s)를 _로 대체

			String fileName = dirName + "/" + uniqueFileName; // S3에 업로드 할 최종 파일명
			File uploadFile = convert(multipartFile); // MultiPartFile을 임시 File로 변환 후 임시 디렉토리에 저장 (S3 업로드 안정성을 위해)

			String uploadImageUrl = putS3(uploadFile,
				fileName); // 클라이언트가 업로드한 파일을 받아 온전한 형태로 임시 저장을 성공한 파일 형태인 uploadFile을 S3에 업로드
			removeNewFile(uploadFile); // S3에 성공적으로 업로드 후 임시 저장한 파일은 삭제
			s3Urls.add(uploadImageUrl);
		}
		return s3Urls;
	}

	private File convert(MultipartFile file) throws IOException {
		String originalFileName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_"); // 로컬 디렉토리 상에서 파일명 중복을 대비해 UUID 생성

		File convertFile = new File(
			uniqueFileName); // MultipartFile 타입인 file의 내용을 File 타입인 convertFile에 쓰기 작업을 해 임시 저장한다.(MulriPartFile -> File로 변환)
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			} catch (IOException e) {
				log.error("파일 변환 중 오류 발생: {}", e.getMessage());
				throw e;
			}
			return convertFile;
		}
		throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
	}

	private String putS3(File uploadFile, String fileName) { // S3 이미지 업로드 메소드 uploadFile: 업로드할 파일, fileName: 파일명
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
			.withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일이 삭제되었습니다.");
		} else {
			log.info("파일이 삭제되지 못했습니다.");
		}
	}

	public void deleteFile(String fileName) {
		try {
			String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
			log.info("S3 파일 삭제 완료: " + decodedFileName);
			amazonS3.deleteObject(bucket, decodedFileName);
		} catch (UnsupportedEncodingException e) {
			log.error("Error while decoding the file name: {}", e.getMessage());
		}
	}

	public List<String> updateFile(List<MultipartFile> newFiles, String oldFileName, String dirName) throws
		IOException {
		deleteFile(oldFileName);
		return upload(newFiles, dirName);
	}
}
