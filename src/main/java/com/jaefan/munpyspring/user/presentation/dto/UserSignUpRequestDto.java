package com.jaefan.munpyspring.user.presentation.dto;

import org.springframework.web.multipart.MultipartFile;

import com.jaefan.munpyspring.user.domain.model.Provider;
import com.jaefan.munpyspring.user.domain.model.Role;
import com.jaefan.munpyspring.user.domain.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 일반 회원가입 요청 Dto
 */
@Getter
@AllArgsConstructor
public class UserSignUpRequestDto {
	
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

	public static User toUser(UserSignUpRequestDto userSignUpRequestDto, String imageUrl, Provider providerType, Role role) {
		return User.builder()
			.nickname(userSignUpRequestDto.getNickname())
			.profileImage(imageUrl)
			.providerType(providerType)
			.email(userSignUpRequestDto.getEmail())
			.password(userSignUpRequestDto.getPassword())
			.role(role)
			.build();
	}
}
