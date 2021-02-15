package com.zitlab.palmyra.sqlbuilder.condition;

import java.util.ArrayList;
import java.util.List;

import com.zitlab.palmyra.sqlbuilder.query.Column;
import com.zitlab.palmyra.sqlbuilder.query.Table;

public class InCondition extends ColumnCondition {
	private List<Object> dataList;
	private boolean _negate = false;

	public InCondition(String attribute) {
		super(attribute);
		// TODO Auto-generated constructor stub
	}

	public InCondition(String attribute, boolean negate, Object... values) {
		super(attribute);
		_negate = negate;
		dataList=new ArrayList<Object>();
		for(int i=0;i<values.length;i++) {
			dataList.add(values[i]);
		}
	}

	public InCondition(Table<? extends Column> table, boolean negate, String column, Object... values) {
		super(null, table, column);
		_negate = negate;
		for(int i=0;i<values.length;i++) {
			dataList.add(values[i]);
		}
	}

	@Override
	public void appendValue(List<Object> valueList) {
		for(int i=0;i<dataList.size();i++)
			valueList.add(dataList.get(i));
	}

	@Override
	public void append(StringBuilder sb) {
		sb.append(getColumn())
	      .append(_negate ? " NOT IN " : " IN ");
		//sb.append("( ? )");
		if(dataList.isEmpty())
			sb.append("(?)");
		else {
			sb.append("(");
			for(int i=0;i<dataList.size();i++) {
				sb.append("?");
			if(i+1 != dataList.size())
				sb.append(",");
			}
			sb.append(")");
		}

	}

}
