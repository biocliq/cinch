package com.zitlab.palmyra.sqlbuilder.condition;

import java.util.List;

import com.zitlab.palmyra.sqlbuilder.query.Column;
import com.zitlab.palmyra.sqlbuilder.query.Table;

public class JoinCondition extends Condition{
	private String left;
	private String right;
	public JoinCondition(String left,String right)
	{
		this.left=left;
		this.right=right;
	}
	@Override
	public void appendValue(List<Object> valueList) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setColumn(Table<? extends Column> table, String column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void append(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append(left).append(" = ").append(right);
	}
	
}
