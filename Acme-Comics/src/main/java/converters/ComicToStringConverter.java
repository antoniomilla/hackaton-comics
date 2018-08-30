
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Comic;

@Component
@Transactional
public class ComicToStringConverter implements Converter<Comic, String> {

	@Override
	public String convert(final Comic comic) {
		String result;

		if (comic == null)
			result = null;
		else
			result = String.valueOf(comic.getId());

		return result;
	}

}
