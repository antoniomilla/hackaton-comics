
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ComicCharacter;

@Component
@Transactional
public class ComicCharacterToStringConverter implements Converter<ComicCharacter, String> {

	@Override
	public String convert(final ComicCharacter personaje) {
		String result;

		if (personaje == null)
			result = null;
		else
			result = String.valueOf(personaje.getId());

		return result;
	}

}
