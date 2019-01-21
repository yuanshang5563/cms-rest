package org.ys.core.model;

import java.io.Serializable;

public class CoreUserRole implements Serializable{
	private static final long serialVersionUID = -2656570296085484133L;

	 private Long coreUserId;
	 
	 private Long coreRoleId;

	public Long getCoreUserId() {
		return coreUserId;
	}

	public void setCoreUserId(Long coreUserId) {
		this.coreUserId = coreUserId;
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
		result = prime * result + ((coreRoleId == null) ? 0 : coreRoleId.hashCode());
		result = prime * result + ((coreUserId == null) ? 0 : coreUserId.hashCode());
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
		CoreUserRole other = (CoreUserRole) obj;
		if (coreRoleId == null) {
			if (other.coreRoleId != null)
				return false;
		} else if (!coreRoleId.equals(other.coreRoleId))
			return false;
		if (coreUserId == null) {
			if (other.coreUserId != null)
				return false;
		} else if (!coreUserId.equals(other.coreUserId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CoreUserRole [coreUserId=" + coreUserId + ", coreRoleId=" + coreRoleId + "]";
	}
}
