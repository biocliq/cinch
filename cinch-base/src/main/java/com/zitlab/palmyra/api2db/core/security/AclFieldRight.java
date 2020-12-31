package com.zitlab.palmyra.api2db.core.security;

public interface AclFieldRight {
	public static final int READ_ONLY = 0;
	public static final int CREATE_ONLY = 1;
	public static final int UPDATE_ONLY = 2;
	public static final int EDITABLE = 3;
	public static final int SYSTEM_GENERATED = 7;

	public abstract void setEditMode(int mode);
	
	public abstract Integer getEditMode();

	public boolean getMandatory();

	public void setMandatory(boolean mandatory);
}
