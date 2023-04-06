package com.batch_drop.batch.repositories;

import com.batch_drop.batch.entity.TestObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestObjectRepository extends JpaRepository<TestObject,Integer> {
}
