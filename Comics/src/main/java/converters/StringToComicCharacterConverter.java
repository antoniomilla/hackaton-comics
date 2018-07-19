
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ComicCharacterRepository;
import domain.ComicCharacter;

@Component
@Transactional
public class StringToComicCharacterConverter implements Converter<String, ComicCharacter> {

	@Autowired
	ComicCharacterRepository	comicCharacterRepository;


	@Override
	public ComicCharacter convert(final String text) {
		ComicCharacter result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.comicCharacterRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
