package qqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qqbot.bean.QQGroup;

@Repository
public interface GroupRepository extends JpaRepository<QQGroup, Long> {
	QQGroup findByGroupId(Long groupId);
}
