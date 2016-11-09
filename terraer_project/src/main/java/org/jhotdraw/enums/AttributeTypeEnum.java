package org.jhotdraw.enums;

import java.util.Locale;

import org.jhotdraw.util.ResourceBundleUtil;

public enum AttributeTypeEnum {
	CHAR("type.char", "CHAR(128)"), TEXT("type.text", "VARCHAR2(128)"), INTEGER("type.integer",
			"NUMBER"), NUMBER("type.number", "NUMBER(9,2)"), DATE("type.date", "DATE");

	private ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels",
			Locale.getDefault());

	private final String description;
	private final String sqlType;

	private AttributeTypeEnum(String description, String sqlType) {
		this.description = description;
		this.sqlType = sqlType;
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return labels.getString(this.sqlType);
	}

	public String getSqlType() {
		return this.sqlType;
	}

	public static AttributeTypeEnum getAttributeTypeByString(String strAttributeType) {
		for (AttributeTypeEnum ate : values()) {
			if (ate.getSqlType().equals(strAttributeType)) {
				return ate;
			}
		}
		return null;
	}

}
