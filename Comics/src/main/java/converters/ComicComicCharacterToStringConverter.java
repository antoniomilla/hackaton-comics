
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ComicComicCharacter;

@Component
@Transactional
public class ComicComicCharacterToStringConverter implements Converter<ComicComicCharacter, String> {

	@Override
	public String convert(final ComicComicCharacter ccc) {
		String result;

		if (ccc == null)
			result = null;
		else
			result = String.valueOf(ccc.getId());

		return result;
	}

}
