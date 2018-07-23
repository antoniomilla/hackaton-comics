
package converters;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StringToDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(final String str) {
		Date result;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				final String patterns[] = new String[1];
				patterns[0] = "dd/mm/yyyy hh:mm:ss";
				result = DateUtils.parseDate(str, patterns);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
