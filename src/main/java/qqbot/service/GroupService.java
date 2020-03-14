package qqbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qqbot.bean.QQGroup;
import qqbot.repository.GroupRepository;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;
	
	public QQGroup findByGroupId(Long groupId) {
		return groupRepository.findByGroupId(groupId);
	}
	
	public QQGroup save(QQGroup group) {
		return groupRepository.save(group);
	}

	public List<QQGroup> findAll() {
		return groupRepository.findAll();
	}
}
