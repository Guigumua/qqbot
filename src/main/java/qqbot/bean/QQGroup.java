package qqbot.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import qqbot.utils.Privileges;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private Long groupId;
	private int privileges;

	public int addPrivilege(int privilege) {
		return this.privileges = (this.privileges | privilege);
	}

	public int removePrivilege(int privilege) {
		return this.privileges = this.hasPrivilege(privilege) ? (this.privileges ^ privilege) : this.privileges;
	}

	public boolean hasPrivilege(int privilege) {
		return (this.privileges & privilege) != 0;
	}

	public static QQGroup defaultQQGroup(Long groupId) {
		return QQGroup.builder().groupId(groupId)
				.privileges(Privileges.SUPER_ADMIN).build();
	}

}
