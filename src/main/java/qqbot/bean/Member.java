package qqbot.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import qqbot.utils.Privileges;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Member {
	@Id
	private Long qq;
	private Integer coldTime;
	private int privileges;

	public int addPrivilege(int privilege) {
		return this.privileges | privilege;
	}

	public int removePrivilege(int privilege) {
		return this.hasPrivilege(privilege) ? this.privileges ^ privilege : this.privileges;
	}

	public boolean hasPrivilege(int privilege) {
		return (this.privileges & privilege) != 0;
	}

	public static Member defaultMember(Long qq) {
		return Member.builder().qq(qq).privileges(Privileges.SETU | Privileges.PIXIV | Privileges.IMAGE).coldTime(30).build();
	}

}
