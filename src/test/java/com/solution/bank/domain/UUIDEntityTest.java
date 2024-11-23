package com.solution.bank.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UUIDEntityTest {

	private record TestEntity(String uuid) implements UUIDEntity {

	}

	@Test
	void shouldReturnTrueWhenEqualUuids() {
		TestEntity entity1 = new TestEntity("12345");
		TestEntity entity2 = new TestEntity("12345");

		assertTrue(entity1.equalsByUuid(entity2));
	}

	@Test
	void shouldReturnFalseWhenDifferentUuids() {
		TestEntity entity1 = new TestEntity("12345");
		TestEntity entity2 = new TestEntity("67890");

		assertFalse(entity1.equalsByUuid(entity2));
	}

	@Test
	void shouldThrowExceptionWhenUuidIsNull() {
		TestEntity entity1 = new TestEntity(null);
		TestEntity entity2 = new TestEntity("12345");

		assertThrows(IllegalStateException.class, () -> entity1.equalsByUuid(entity2));
	}

	@Test
	void shouldReturnSameHashcodeWhenEqualUuids() {
		TestEntity entity1 = new TestEntity("12345");
		TestEntity entity2 = new TestEntity("12345");

		assertEquals(entity1.hashcodeByUuid(), entity2.hashcodeByUuid());
	}

	@Test
	void shouldThrowExceptionWhenUuidIsNullForHashcode() {
		TestEntity entity = new TestEntity(null);

		assertThrows(IllegalStateException.class, entity::hashcodeByUuid);
	}
}