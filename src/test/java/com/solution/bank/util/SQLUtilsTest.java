package com.solution.bank.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.jpa.domain.Specification;

class SQLUtilsTest {

	@Test
	void shouldAddSpecificationIfNotNull() {
		List<Specification<Object>> specs = new ArrayList<>();

		Function<String, Specification<Object>> specFunction = val -> (root, query, builder) -> builder.equal(root.get("field"), val);

		SQLUtils.addIfNotNull(specs, "testValue", specFunction);

		assertEquals(1, specs.size());
	}

	@Test
	void shouldNotAddSpecificationIfNull() {
		List<Specification<Object>> specs = new ArrayList<>();

		Function<String, Specification<Object>> specFunction = val -> (root, query, builder) -> builder.equal(root.get("field"), val);

		SQLUtils.addIfNotNull(specs, null, specFunction);

		assertEquals(0, specs.size());
	}

	@ParameterizedTest
	@ValueSource(strings = {"testValue", "", "null"})
	void shouldAddSpecificationIfNotEmpty(String inputValue) {
		List<Specification<Object>> specs = new ArrayList<>();

		Function<String, Specification<Object>> specFunction = val -> (root, query, builder) -> builder.equal(root.get("field"), val);

		if ("null".equals(inputValue)) {
			inputValue = null;
		}

		SQLUtils.addIfNotEmpty(specs, inputValue, specFunction);

		if (inputValue != null && !inputValue.isEmpty()) {
			assertEquals(1, specs.size());
		} else {
			assertEquals(0, specs.size());
		}
	}
}
