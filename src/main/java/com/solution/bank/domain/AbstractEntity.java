package com.solution.bank.domain;

import java.io.Serializable;

public abstract class AbstractEntity<T extends Serializable> implements Serializable {

	public abstract T getId();

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		final AbstractEntity that = (AbstractEntity) obj;

		return getId() != null && getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
