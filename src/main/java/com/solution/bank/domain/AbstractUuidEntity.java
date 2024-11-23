package com.solution.bank.domain;

import java.io.Serializable;

import com.solution.bank.util.RandomUtils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;

@Setter
@MappedSuperclass
public abstract class AbstractUuidEntity<T extends Serializable> extends AbstractEntity<T> implements UUIDEntity {

	@Column(name = "uuid", unique = true, nullable = false, length = 36)
	protected String uuid = RandomUtils.generateUuid();

	@Override
	public String uuid() {
		return uuid;
	}

	@SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
	@Override
	public boolean equals(Object obj) {
		return equalsByUuid(obj);
	}

	@Override
	public int hashCode() {
		return hashcodeByUuid();
	}
}
