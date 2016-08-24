package org.jhotdraw.enums;

import java.util.Locale;

import org.jhotdraw.util.ResourceBundleUtil;

public enum AttributeTypeEnum {	
	CHAR("type.char"), TEXT("type.text"), INTEGER("type.integer"), NUMBER("type.number"), DATE("type.date");
	
	private ResourceBundleUtil labels =
            ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels", Locale.getDefault());
	
	private final String description;
	
	private AttributeTypeEnum(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return labels.getString(this.description);
	}
	
	
	
}
