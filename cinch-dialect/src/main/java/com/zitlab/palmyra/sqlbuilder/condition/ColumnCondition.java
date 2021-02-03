package com.zitlab.palmyra.sqlbuilder.condition;

import com.zitlab.palmyra.sqlbuilder.query.Column;
import com.zitlab.palmyra.sqlbuilder.query.Table;

public abstract class ColumnCondition extends Condition{
	private String attribute;
	private String column;
	
	public ColumnCondition(String attribute) {
		this.attribute = attribute;
	}
	
	public ColumnCondition(String attribute, Table<? extends Column> table, String column) {
		this.attribute = attribute;
		this.column = getColumnAlias(table, column);
	}

	public String getAttribute() {
		return attribute;
	}

	public String getColumn() {
		return column;
	}
	
	public void setColumn(Table<? extends Column> table, String column) {
		this.column = getColumnAlias(table, column);
	}
}
