package org.jhotdraw.interfaces;

import org.jhotdraw.enums.AttributeTypeEnum;

public interface AttributeTypeElement {
	
	AttributeTypeEnum getAttributeType();
	void setAttributeType(AttributeTypeEnum attributeType);
	
	boolean isNullable();
	void setNullable(boolean nullable);
}
