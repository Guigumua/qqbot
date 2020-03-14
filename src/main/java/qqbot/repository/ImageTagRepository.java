package qqbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qqbot.bean.ImageTag;
import qqbot.bean.ImageTag.ImageTagPK;

@Repository
public interface ImageTagRepository extends JpaRepository<ImageTag, ImageTagPK> {
	List<ImageTag> findByTag(String tag);
}
