package qqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qqbot.bean.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
