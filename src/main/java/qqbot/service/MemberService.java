package qqbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qqbot.bean.Member;
import qqbot.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	public Member findById(Long qq) {
		return this.save(memberRepository.findById(qq).orElse(Member.defaultMember(qq)));
	}
	
	public Member save(Member member) {
		return memberRepository.save(member);
	}
	
	public Member removePrivilege(Member member,int privileges) {
		member.removePrivilege(privileges);
		return this.save(member);
	}
	public Member setPrivileges(Member member,int privileges) {
		member.setPrivileges(privileges);
		return this.save(member);
	}
}
