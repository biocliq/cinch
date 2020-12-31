package com.zitlab.palmyra.api2db.core.security;

import java.util.Set;

import com.zitlab.palmyra.api2db.pojo.Tuple;

public interface AclChecker {
	
	public Set<String> getNonUpdatableFields(Tuple item);
	
	public Set<String> getNonInsertableFields(Tuple item);
	
	public boolean isInsertable(Tuple item);
	
	public boolean isUpdatable(Tuple item);
	
	public boolean isDeletable(Tuple item);

}