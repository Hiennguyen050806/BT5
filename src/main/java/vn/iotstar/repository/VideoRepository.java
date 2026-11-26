package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    long countByCategory_Categoryid(Integer categoryid);

    long countByUser_Id(Integer userId);
}
