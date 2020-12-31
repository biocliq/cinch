/*******************************************************************************
 * Copyright 2020 BioCliq Technologies
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.zitlab.palmyra.api2db.pdbc.pojo;

import java.util.function.BiConsumer;

import com.zitlab.palmyra.api2db.exception.FieldValidationException;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.converter.Converter;

public class TupleAttribute {
//	private static final Logger logger = LoggerFactory.getLogger(TupleAttribute.class);
	private static final int EDITABLE = 1;
	private static final int CHANGE_LOG = 2;
	private static final int AGGREGATE = 4;
	private static final int FORMULA = 8;

	private static final int PRIMARY_KEY = 1;
	private static final int AUTO_INCREMENT = 2;
	private static final int NPRIMARY_KEY = 0b11110;
	private static final int NAUTO_INCREMENT = 0b11101;

	private int id;
	private String attribute;
	private String columnName;
	private boolean active = true;
	private String displayLabel;
	private int dataType;

	private Converter<?> converter;
	private BiConsumer<String, Object> mandatoryChecker;
	
	// 0 - not a primary key, 1 - primary key, 2 - Auto incremental key
	// private int primaryKey;
	private int primaryAutoKey = 0;

	private boolean mandatory;
	private String defaultValue;
	private String selectValue;
	private Integer displayOrder;
	private Integer length;
	// Number of columns of the foreignKey - if this column is associated to a
	// foreignkey.
	private int foreignKeyCount = 0;
//	private TupleAttribute fKeyAttribute;
//	private ForeignKey foreignKey;

	private int options = 0;
	private String formula;

	public final Integer getLength() {
		return length;
	}

	public final String getSelectValue() {
		return selectValue;
	}

	public final boolean isMandatory() {
		return mandatory;
	}

	public final void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
		mandatoryChecker = mandatory ? new MandatoryVerifier() : NoopVerifier.instance();
	}

	public final void setMandatory(int mandatory) {
		this.mandatory = mandatory > 0;
	}

	public final void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}

	public final void setLength(Integer length) {
		this.length = length;
	}

	public final String getColumnName() {
		return columnName;
	}

	public final void setColumnName(String fieldName) {
		this.columnName = fieldName;
	}

	public final String getAttribute() {
		return attribute;
	}

	public final void setAttribute(String attribName) {
		this.attribute = attribName;
	}

	public final void setPrimaryKey(boolean flag) {
		primaryAutoKey = flag ? (primaryAutoKey | PRIMARY_KEY) : (primaryAutoKey & NPRIMARY_KEY);
	}

	public final void setAutoIncrement(boolean flag) {
		primaryAutoKey = flag ? (primaryAutoKey | AUTO_INCREMENT) : (primaryAutoKey & NAUTO_INCREMENT);
	}

	public final boolean isPrimaryKey() {
		return (primaryAutoKey & PRIMARY_KEY) > 0;
	}

	public final boolean isAutoIncrement() {
		return (primaryAutoKey & AUTO_INCREMENT) > 0;
	}

	public final int getDataType() {
		return dataType;
	}

	public final void setDataType(int dataType) {
		this.dataType = dataType;
		this.converter = Converter.getConverter(dataType);
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getDefaultValue() {
		return defaultValue;
	}

	public final void setDefaultValue(String defValue) {
		this.defaultValue = defValue;
	}

	public final String getDisplayLabel() {
		return displayLabel;
	}

	public final void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	public final Integer getDisplayOrder() {
		return displayOrder;
	}

	public final void setDisplayOrder(Integer order) {
		this.displayOrder = order;
	}

	public final Integer getOptions() {
		return options;
	}

	public final void setOptions(Integer options) {
		this.options = null != options ? options : 0;
	}

	public final boolean isEditable() {
		return (options & EDITABLE) > 0;
	}

	public final boolean isChangeLogEnabled() {
		return (options & CHANGE_LOG) > 0;
	}

	public final boolean isAggregate() {
		return (options & AGGREGATE) > 0;
	}

	public final boolean isNonFormula() {
		return (options & FORMULA) < 1;
	}

	public final boolean isFormula() {
		return (options & FORMULA) > 0;
	}

	public final void setChangeLogEnabled(boolean flag) {
		options = flag ? (options | CHANGE_LOG) : (options & ~CHANGE_LOG);
	}

	public final void setEditable(boolean flag) {
		options = flag ? (options | EDITABLE & ~FORMULA & ~AGGREGATE) : (options & ~EDITABLE & ~CHANGE_LOG);
	}

	public final void setAggregate(boolean flag) {
		options = flag ? (options | AGGREGATE & ~EDITABLE & ~CHANGE_LOG) : (options & ~AGGREGATE);
	}

	public final void setFormula(boolean flag) {
		options = flag ? (options | FORMULA & ~EDITABLE & ~CHANGE_LOG) : (options & ~FORMULA);
	}

	public final boolean isActive() {
		return active;
	}

//	public final void setActive(boolean active) {
//		this.active = active;
//	}

	public final void setActive(int active) {
		this.active = (active > 0);
	}

	public final String getFormula() {
		return formula;
	}

	public final void setFormula(String formula) {
		this.formula = formula;
	}

	@Override
	public final int hashCode() {
		if (0 != id) {
			return id;
		}
		return super.hashCode();
	}

	public final int getForeignKeyCount() {
		return foreignKeyCount;
	}

	public final int getPrimaryAutoKey() {
		return primaryAutoKey;
	}

	public final void setPrimaryAutoKey(int primaryAutoKey) {
		this.primaryAutoKey = primaryAutoKey;
	}

	public Converter<?> getConverter() {
		return converter;
	}

	public final void setForeignKey(String srcTable, ForeignKey foreignKey, TupleAttribute tgt) {

		this.foreignKeyCount = foreignKey.getSource().size();
//		if (null != fKeyAttribute) {
//			this.foreignKey = foreignKey;
//			this.fKeyAttribute = tgt;
//			this.foreignKeyCount = foreignKey.getSource().size();
//		} else
//			logger.error("Multiple foreign Keys on the same column is not supported Table - " + srcTable
//					+ ":" + this.columnName + " However the already assigned foreign key will be used " + foreignKey.getAlias() + ":" + this.columnName);
	}

//	public final TupleAttribute getfKeyAttribute() {
//		return fKeyAttribute;
//	}
//
//	public final ForeignKey getForeignKey() {
//		return foreignKey;
//	}

	public void checkMandatory(String type, Object value) {
		mandatoryChecker.accept(type, value);
	}
		
	private class MandatoryVerifier implements BiConsumer<String, Object> {
		@Override
		public void accept(String type, Object value) {
			if (null != value)
				return;
			throw new FieldValidationException(attribute, type,
					"Mandatory parameter `" + attribute + "` is missing for the CI Type `" + type + "`",
					Validation.MANDATORY);
		}
	}

}
