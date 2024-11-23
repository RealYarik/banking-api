package com.solution.bank.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractUuidEntityTest {

	private static class TestEntity extends AbstractUuidEntity<Long> {

		private final Long id;

		TestEntity(Long id) {
			this.id = id;
		}

		@Override
		public Long getId() {
			return id;
		}
	}

	private TestEntity entity1;
	private TestEntity entity2;

	@BeforeEach
	void setUp() {
		entity1 = new TestEntity(1L);
		entity2 = new TestEntity(1L);
	}

	@Test
	void shouldGenerateUniqueUuid() {
		assertNotNull(entity1.uuid());
		assertNotNull(entity2.uuid());
		assertNotEquals(entity1.uuid(), entity2.uuid());
	}

	@Test
	void shouldReturnTrueWhenUuidsAreEqual() {
		String uuid = entity1.uuid();
		entity2 = new TestEntity(2L);
		entity2.uuid = uuid;

		assertEquals(entity1, entity2);
	}

	@Test
	void shouldReturnFalseWhenUuidsAreDifferent() {
		entity2 = new TestEntity(2L);

		assertNotEquals(entity1, entity2);
	}

	@Test
	void shouldReturnHashCodeBasedOnUuid() {
		String uuid = entity1.uuid();
		entity2 = new TestEntity(2L);
		entity2.uuid = uuid;

		assertEquals(entity1.hashCode(), entity2.hashCode());
	}

	@Test
	void shouldGenerateUuidWhenNull() {
		entity1 = new TestEntity(null);
		assertNotNull(entity1.uuid());
	}
}