/*
 * ActorToStringConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DateToStringConverter implements Converter<Date, String> {

	@Override
	public String convert(final Date date) {
		String result;

		if (date == null)
			result = null;
		else {
			final SimpleDateFormat dt = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
			result = dt.format(date);
		}
		return result;
	}

}
