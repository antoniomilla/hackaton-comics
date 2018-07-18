
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Author;

@Component
@Transactional
public class AutorToStringConverter implements Converter<Author, String> {

	@Override
	public String convert(final Author autor) {
		String result;

		if (autor == null)
			result = null;
		else
			result = String.valueOf(autor.getId());

		return result;
	}

}
