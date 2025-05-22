package com.bookreum.domain.user.repository;

import java.util.Optional;
import com.bookreum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	 // JpaRepository를 상속하면 findById(), save(), deleteById() 등이 자동으로 제공됩니다.
		Optional<User> findByKakaoId(String kakaoId);
}
