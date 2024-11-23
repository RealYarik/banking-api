package com.solution.bank.domain;

public interface UUIDEntity {

	String getUuid();

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

		if (getUuid() == null) {
			throw new IllegalStateException("Equals was called before UUID initialization");
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