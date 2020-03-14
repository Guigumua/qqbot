package qqbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qqbot.bean.ImageTag;
import qqbot.repository.ImageTagRepository;

@Service
public class ImageTagService {
	@Autowired
	private ImageTagRepository imageTagRepository;
	
	public ImageTag save(ImageTag imageTag) {
		return imageTagRepository.save(imageTag);
	}
	
	public List<ImageTag> findByTag(String tag) {
		return imageTagRepository.findByTag(tag);
	}
	
}
