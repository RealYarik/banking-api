package com.solution.bank.domain;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;

@Setter
@MappedSuperclass
public abstract class AbstractUuidEntity<T extends Serializable> extends AbstractEntity<T> implements UUIDEntity {

	@Column(name = "uuid", unique = true, nullable = false)
	protected UUID uuid = UUID.randomUUID();

	@Override
	public UUID getUuid() {
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
