
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Editorial;

@Component
@Transactional
public class EditorialToStringConverter implements Converter<Editorial, String> {

	@Override
	public String convert(final Editorial editorial) {
		String result;

		if (editorial == null)
			result = null;
		else
			result = String.valueOf(editorial.getId());

		return result;
	}

}
