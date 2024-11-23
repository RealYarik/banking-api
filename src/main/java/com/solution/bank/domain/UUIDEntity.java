package com.solution.bank.domain;

public interface UUIDEntity {

	String uuid();

	default boolean equalsByUuid(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		final UUIDEntity that = (UUIDEntity) obj;

		if (uuid() == null) {
			throw new IllegalStateException("Equals was called before UUID initialization");
		}

		return uuid().equals(that.uuid());
	}

	default int hashcodeByUuid() {
		if (uuid() == null) {
			throw new IllegalStateException("Hashcode was called before uuid initialization");
		}

		return uuid().hashCode();
	}
}