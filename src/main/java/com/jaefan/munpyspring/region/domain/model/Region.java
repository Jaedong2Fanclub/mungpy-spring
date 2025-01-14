package com.jaefan.munpyspring.region.domain.model;

import java.util.List;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "region")
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Region {

	@Id
	@Column(name = "region_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String upper;
	private String lower;

	@OneToMany(mappedBy = "region")
	private List<Shelter> shelters;
}
