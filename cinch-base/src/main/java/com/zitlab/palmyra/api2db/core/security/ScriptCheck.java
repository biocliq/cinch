/** 
 * (C) Copyright 2018 ZitLab - https://github.com/ksvraja
 *
 * Licensed under the MIT license;
 * 
 * Refer to the LICENSE file in the project root folder 
 * 
 */
package com.zitlab.palmyra.api2db.core.security;

public class ScriptCheck {
	
	private static final int PRE_CREATE 	= 1;
	private static final int PRE_UPDATE 	= 2;
	private static final int PRE_DELETE 	= 4;
	
	private static final int POST_CREATE 	= 8;
	private static final int POST_UPDATE 	= 16;
	private static final int POST_DELETE 	= 32;
	
	private static final int PRE_VIEW 		= 64;
	private static final int POST_VIEW 		= 128;
	
	private static final int PRE_SEARCH 	= 256;
	private static final int SEARCH_RESULT 	= 512;
	
	private static final int PRE_VALIDATE 	= 1024;
	private static final int AFTER_CREATE 	= 2048;
	private static final int AFTER_UPDATE 	= 4096;
	private static final int AFTER_DELETE 	= 8192;
	
	private static final int XPRE_CREATE 	= ~PRE_CREATE;
	private static final int XPRE_UPDATE 	= ~PRE_UPDATE;
	private static final int XPRE_DELETE 	= ~PRE_DELETE;
	
	private static final int XPOST_CREATE 	= ~POST_CREATE;
	private static final int XPOST_UPDATE 	= ~POST_UPDATE;
	private static final int XPOST_DELETE 	= ~POST_DELETE;
	
	private static final int XPRE_VIEW 		= ~PRE_VIEW;
	private static final int XPOST_VIEW 	= ~POST_VIEW;
	
	private static final int XPRE_SEARCH	= ~PRE_SEARCH;
	private static final int XSEARCH_RESULT	= ~SEARCH_RESULT;

	private static final int XPRE_VALIDATE 	= ~PRE_VALIDATE;
	private static final int XAFTER_CREATE 	= ~AFTER_CREATE;
	private static final int XAFTER_UPDATE 	= ~AFTER_UPDATE;
	private static final int XAFTER_DELETE 	= ~AFTER_DELETE;
	  
	private static int mask = 0xFFFFFFF;
	
	public boolean preCreate()
	{
		return (mask & PRE_CREATE) > 0;
	}
	public boolean preUpdate()
	{
		return (mask & PRE_UPDATE) > 0;
	}
	public boolean preDelete()
	{
		return (mask & PRE_DELETE) > 0;
	}
	public boolean postCreate()
	{
		return (mask & POST_CREATE) > 0;
	}
	public boolean postUpdate()
	{
		return (mask & POST_UPDATE) > 0;
	}
	public boolean postDelete()
	{
		return (mask & POST_DELETE) > 0;
	}
	public boolean preView()
	{
		return (mask & PRE_VIEW) > 0;
	}
	public boolean postView()
	{
		return (mask & POST_VIEW) > 0;
	}
	public boolean preSearch()
	{
		return (mask & PRE_SEARCH) > 0;
	}
	public boolean onQueryResult()
	{
		return (mask & SEARCH_RESULT) > 0;
	}
	public boolean preValidate()
	{
		return (mask & PRE_VALIDATE) > 0;
	}
	public boolean afterCreate()
	{
		return (mask & AFTER_CREATE) > 0;
	}
	public boolean afterUpdate()
	{
		return (mask & AFTER_UPDATE) > 0;
	}
	public boolean afterDelete()
	{
		return (mask & AFTER_DELETE) > 0;
	}
	
	
	public void setPreCreate(boolean grant) {
		mask = grant ? (mask | PRE_CREATE) : (mask & XPRE_CREATE);
	}	
	public void setPreUpdate(boolean grant) {
		mask = grant ? (mask | PRE_UPDATE) : (mask & XPRE_UPDATE);
	}	
	public void setPreDelete(boolean grant) {
		mask = grant ? (mask | PRE_DELETE) : (mask & XPRE_DELETE);
	}	
	public void setPostCreate(boolean grant) {
		mask = grant ? (mask | POST_CREATE) : (mask & XPOST_CREATE);
	}	
	public void setPostUpdate(boolean grant) {
		mask = grant ? (mask | POST_UPDATE) : (mask & XPOST_UPDATE);
	}	
	public void setPostDelete(boolean grant) {
		mask = grant ? (mask | POST_DELETE) : (mask & XPOST_DELETE);
	}
	public void setPreView(boolean grant) {
		mask = grant ? (mask | PRE_VIEW) : (mask & XPRE_VIEW);
	}
	public void setPostView(boolean grant) {
		mask = grant ? (mask | POST_VIEW) : (mask & XPOST_VIEW);
	}
	public void setPreSearch(boolean grant) {
		mask = grant ? (mask | PRE_SEARCH) : (mask & XPRE_SEARCH);
	}
	public void setQueryResult(boolean grant) {
		mask = grant ? (mask | SEARCH_RESULT) : (mask & XSEARCH_RESULT);
	}
	public void setPreValidate(boolean grant) {
		mask = grant ? (mask | PRE_VALIDATE) : (mask & XPRE_VALIDATE);
	}
	public void setAfterCreate(boolean grant) {
		mask = grant ? (mask | AFTER_CREATE) : (mask & XAFTER_CREATE);
	}
	public void setAfterUpdate(boolean grant) {
		mask = grant ? (mask | AFTER_UPDATE) : (mask & XAFTER_UPDATE);
	}
	public void setAfterDelete(boolean grant) {
		mask = grant ? (mask | AFTER_DELETE) : (mask & XAFTER_DELETE);
	}
}
