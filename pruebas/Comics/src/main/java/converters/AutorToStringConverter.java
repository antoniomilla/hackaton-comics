
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Autor;

@Component
@Transactional
public class AutorToStringConverter implements Converter<Autor, String> {

	@Override
	public String convert(final Autor autor) {
		String result;

		if (autor == null)
			result = null;
		else
			result = String.valueOf(autor.getId());

		return result;
	}

}
