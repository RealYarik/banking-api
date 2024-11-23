package com.solution.bank.util;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class SQLUtils {

	private SQLUtils() {
	}

	public static <F, E> void addIfNotNull(List<Specification<E>> specs, F value, Function<F, Specification<E>> specFunction) {
		if (value != null) {
			specs.add(specFunction.apply(value));
		}
	}

	public static <T> void addIfNotEmpty(List<Specification<T>> specs, String value, Function<String, Specification<T>> specFunction) {
		if (StringUtils.isNotEmpty(value)) {
			specs.add(specFunction.apply(value));
		}
	}

	public static <F, E> void addIfNotEmptyList(List<Specification<E>> specs, List<F> roles, Function<List<F>, Specification<E>> specFunction) {
		if (CollectionUtils.isNotEmpty(roles)) {
			specs.add(specFunction.apply(roles));
		}
	}
}
