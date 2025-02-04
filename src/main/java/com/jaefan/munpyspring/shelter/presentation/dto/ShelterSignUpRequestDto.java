package com.jaefan.munpyspring.shelter.presentation.dto;

import static com.jaefan.munpyspring.user.domain.model.Provider.*;
import static com.jaefan.munpyspring.user.domain.model.Role.*;
import static com.jaefan.munpyspring.user.domain.model.Status.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.jaefan.munpyspring.region.domain.model.Region;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.user.domain.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 보호소 회원가입 요청 Dto
 */
@Getter
@AllArgsConstructor
public class ShelterSignUpRequestDto {

	@NotBlank
	@Pattern(regexp = "^(?! ).*(?<! )$", message = "닉네임의 앞이나 뒤에 공백이 올 수 없습니다.")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_ ]{2,50}$", message = "닉네임은 특수문자를 제외한 2~50자리여야 합니다.")
	private String nickname;

	private MultipartFile profileImage;

	@NotBlank
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotBlank
	@Pattern(regexp = "^[\\S]{8,16}$", message = "비밀번호는 8~16자 공백 없이 입력해야 합니다.")
	private String password;

	@NotBlank
	@Size(min = 1, max = 300, message = "주소는 300자 이내로 입력해주세요.")
	private String address;

	@NotBlank
	private String owner;

	@NotBlank
	@Pattern(regexp = "^s\\d{2,3}-\\d{3,4}-\\d{4}$|^\\d{4}-\\d{4}$", message = "전화번호는 올바른 형식이어야 합니다. 예: 02-123-1234, 010-1234-5678, 1588-1234")
	@Size(min = 9, max = 13, message = "전화번호는 최소 8자리, 최대 11자리입니다.") // XXXX-XXXX (8자리), XXX-XXXX-XXXX (11자리)
	private String telNo;

	public Shelter toShelter(String imageUrl, Region region, Double latitude, Double longitude) {

		User user = User.builder()
			.uuid(UUID.randomUUID())
			.nickname(nickname)
			.profileImage(imageUrl)
			.providerType(DEFAULT)
			.email(email)
			.status(ACTIVE)
			.password(password)
			.createdAt(LocalDateTime.now())
			.role(SHELTER)
			.build();

		return Shelter.builder()
			.name(nickname)
			.address(address)
			.telNo(telNo)
			.region(region)
			.latitude(latitude)
			.longitude(longitude)
			.user(user)
			.build();
	}
}
