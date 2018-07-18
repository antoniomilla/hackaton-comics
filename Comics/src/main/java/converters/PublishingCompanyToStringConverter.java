
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Publisher;

@Component
@Transactional
public class PublishingCompanyToStringConverter implements Converter<Publisher, String> {

	@Override
	public String convert(final Publisher editorial) {
		String result;

		if (editorial == null)
			result = null;
		else
			result = String.valueOf(editorial.getId());

		return result;
	}

}
