package com.zitlab.palmyra.cinch.tuple.condition;




import com.zitlab.palmyra.sqlbuilder.condition.BetweenCondition;
import com.zitlab.palmyra.sqlbuilder.condition.BinaryCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition;
import com.zitlab.palmyra.sqlbuilder.condition.ComboCondition.Op;
import com.zitlab.palmyra.sqlbuilder.condition.Condition;
import com.zitlab.palmyra.sqlbuilder.condition.CustomCondition;
import com.zitlab.palmyra.sqlbuilder.condition.NotNullCondition;
import com.zitlab.palmyra.sqlbuilder.condition.NullCondition;
import com.zitlab.palmyra.sqlbuilder.condition.Operator;

public class ConditionBuilder {
	
//	private ArrayList<Condition> conditions = new ArrayList<Condition>();
//	public static Op operator;
	private ComboCondition conditions=new ComboCondition(Op.AND);
	
	public ComboCondition getConditions() {
		return conditions;
	}
	public void setConditions(ComboCondition conditions) {
		this.conditions = conditions;
	}

	public ConditionBuilder and() {
		conditions.setOperator(Op.AND);
		
		return this;
	}
	public ConditionBuilder or() {
		conditions.setOperator(Op.OR);
		return this;
	}
//	public ConditionBuilder build() {
//		return new ComboCondition(this);
//	}
	public ComboCondition build() {
		
		return conditions;
	}
	
	public ConditionBuilder equals(String field,Object value) {
		Condition condition = new BinaryCondition(Operator.EQUAL_TO,field,value);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder greaterThan(String field,String value) {
		
		Condition condition = new BinaryCondition(Operator.GREATER_THAN,field,value);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder greaterThanEq(String field,String value) {
		Condition condition = new BinaryCondition(Operator.GREATER_THAN_OR_EQUAL_TO,field,value);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder lesserThan(String field,String value) {
		
		Condition condition = new BinaryCondition(Operator.LESS_THAN,field,value);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder lesserThanEq(String field,String value) {
		Condition condition = new BinaryCondition(Operator.LESS_THAN_OR_EQUAL_TO,field,value);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder between(String field,String leftV,String rightV) {
		Condition condition = new BetweenCondition(field,leftV,rightV);
		this.conditions.addCondition(condition);
		return this;			
	}
	public ConditionBuilder notBetween(String field,String leftV,String rightV) {
		Condition condition = new BetweenCondition(field,true,leftV,rightV);
		this.conditions.addCondition(condition);
		return this;			
	}
	public ConditionBuilder isNull(String field)
	{
		Condition condition = new NullCondition(field);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder isNotNull(String field)
	{
		Condition condition = new NotNullCondition(field);
		this.conditions.addCondition(condition);
		return this;
	}
	public ConditionBuilder sqlExpression(String query) {
		Condition condition = new CustomCondition(query);
		this.conditions.addCondition(condition);
		return this;
	}
	

}
