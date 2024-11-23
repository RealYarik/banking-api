package com.solution.bank.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AbstractEntityTest {

	private static class TestEntity extends AbstractEntity<Long> {

		private final Long id;

		TestEntity(Long id) {
			this.id = id;
		}

		@Override
		public Long getId() {
			return id;
		}
	}

	@Test
	void shouldReturnTrueWhenEqualIds() {
		TestEntity entity1 = new TestEntity(1L);
		TestEntity entity2 = new TestEntity(1L);

		assertEquals(entity1, entity2);
	}

	@Test
	void shouldReturnFalseWhenDifferentIds() {
		TestEntity entity1 = new TestEntity(1L);
		TestEntity entity2 = new TestEntity(2L);

		assertNotEquals(entity1, entity2);
	}

	@Test
	void shouldReturnFalseWhenNullId() {
		TestEntity entity1 = new TestEntity(null);
		TestEntity entity2 = new TestEntity(1L);

		assertNotEquals(entity1, entity2);
	}

	@Test
	void shouldReturnFalseWhenDifferentClass() {
		TestEntity entity1 = new TestEntity(1L);
		String entity2 = "Some String";

		assertFalse(entity1.equals(entity2));
	}

	@Test
	void shouldReturnSameHashCodeForEqualIds() {
		TestEntity entity1 = new TestEntity(1L);
		TestEntity entity2 = new TestEntity(1L);

		assertEquals(entity1.hashCode(), entity2.hashCode());
	}

	@Test
	void shouldReturnHashCodeBasedOnClass() {
		TestEntity entity = new TestEntity(1L);
		assertEquals(entity.hashCode(), TestEntity.class.hashCode());
	}
}