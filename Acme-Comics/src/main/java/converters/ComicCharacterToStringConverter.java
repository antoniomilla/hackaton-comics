
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ComicCharacter;

@Component
@Transactional
public class ComicCharacterToStringConverter implements Converter<ComicCharacter, String> {

	@Override
	public String convert(final ComicCharacter comicCharacter) {
		String result;

		if (comicCharacter == null)
			result = null;
		else
			result = String.valueOf(comicCharacter.getId());

		return result;
	}

}
