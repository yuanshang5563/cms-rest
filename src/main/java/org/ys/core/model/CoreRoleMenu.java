package org.ys.core.model;

import java.io.Serializable;

public class CoreRoleMenu implements Serializable{
	private static final long serialVersionUID = -2656570296085484133L;

	 private Long coreMenuId;
	 
	 private Long coreRoleId;

	public Long getCoreMenuId() {
		return coreMenuId;
	}

	public void setCoreMenuId(Long coreMenuId) {
		this.coreMenuId = coreMenuId;
	}

	public Long getCoreRoleId() {
		return coreRoleId;
	}

	public void setCoreRoleId(Long coreRoleId) {
		this.coreRoleId = coreRoleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coreMenuId == null) ? 0 : coreMenuId.hashCode());
		result = prime * result + ((coreRoleId == null) ? 0 : coreRoleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoreRoleMenu other = (CoreRoleMenu) obj;
		if (coreMenuId == null) {
			if (other.coreMenuId != null)
				return false;
		} else if (!coreMenuId.equals(other.coreMenuId))
			return false;
		if (coreRoleId == null) {
			if (other.coreRoleId != null)
				return false;
		} else if (!coreRoleId.equals(other.coreRoleId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CoreRoleMenu [coreMenuId=" + coreMenuId + ", coreRoleId=" + coreRoleId + "]";
	}

}
