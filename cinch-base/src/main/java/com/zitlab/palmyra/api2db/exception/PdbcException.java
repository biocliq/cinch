/** 
 * (C) Copyright 2018 ZitLab - https://github.com/ksvraja
 *
 * Licensed under the MIT license;
 * 
 * Refer to the LICENSE file in the project root folder 
 * 
 */
package com.zitlab.palmyra.api2db.exception;

/**
 * @author ksvraja
 *
 */
public class PdbcException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	
	public PdbcException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
	
	public PdbcException(Validation reason,  String message, Throwable e) {
        super(message, e);
        this.errorCode = reason.getCode();
    }
	
	public PdbcException(Validation reason, String message) {
        super(message);
        this.errorCode = reason.getCode();
    }

	public int getErrorCode() {
		return errorCode;
	}
}
