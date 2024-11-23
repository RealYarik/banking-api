package com.solution.bank.domain;

import java.util.UUID;

import org.hibernate.Hibernate;

public interface UUIDEntity {

	UUID getUuid();

	default boolean equalsByUuid(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}

		final UUIDEntity that = (UUIDEntity) obj;

		if (getUuid() == null) {
			throw new IllegalStateException("Equals was called before uuid initialization");
		}

		return getUuid().equals(that.getUuid());
	}

	default int hashcodeByUuid() {
		if (getUuid() == null) {
			throw new IllegalStateException("Hashcode was called before uuid initialization");
		}

		return getUuid().hashCode();
	}
}