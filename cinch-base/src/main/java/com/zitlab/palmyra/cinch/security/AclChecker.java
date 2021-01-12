package com.zitlab.palmyra.cinch.security;

import java.util.Set;

import com.zitlab.palmyra.cinch.pojo.Tuple;

public interface AclChecker {
	
	public Set<String> getNonUpdatableFields(Tuple item);
	
	public Set<String> getNonInsertableFields(Tuple item);
	
	public boolean isInsertable(Tuple item);
	
	public boolean isUpdatable(Tuple item);
	
	public boolean isDeletable(Tuple item);

}