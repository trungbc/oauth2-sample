package oauth.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oauth.dao.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByAccountAndTypeId(String account, int typeId);
	
	public User findByAccount(String account);
	
	public User findByAccountAndTypeIdAndCredential(String account, int typeId, String credential);

}
