package oauth.dao.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String account;

	@Column(name="created_date")
	private Timestamp createdDate;

	private String credential;

	@Column(name="type_id")
	private int typeId;

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", createdDate=" + createdDate + ", credential=" + credential
				+ ", typeId=" + typeId + ", updatedDate=" + updatedDate + ", userStatusId=" + userStatusId + "]";
	}

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@Column(name="user_status_id")
	private int userStatusId;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCredential() {
		return this.credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public int getTypeId() {
		return this.typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getUserStatusId() {
		return this.userStatusId;
	}

	public void setUserStatusId(int userStatusId) {
		this.userStatusId = userStatusId;
	}

}