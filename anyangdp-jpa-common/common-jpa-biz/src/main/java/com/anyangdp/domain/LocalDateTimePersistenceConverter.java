/**
 * For commerce and communication.
 *
 * Copyright 2016 Fara, Org. All rights reserved.
 * Use is subject to license terms.
 *
 * Create At 2015年11月29日
 */
package com.anyangdp.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * if hibernate version less than 5
 * 
 * @author william
 */
@Converter(autoApply = true)
public class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	/**
	 * @param attribute
	 * @return
	 */
	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {

		if (null != attribute) {
			return Timestamp.valueOf(attribute);
		}

		return null;
	}

	/**
	 * @param dbData
	 * @return
	 */
	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp dbData) {

		if (null != dbData) {
			return dbData.toLocalDateTime();
		}

		return null;
	}

}
