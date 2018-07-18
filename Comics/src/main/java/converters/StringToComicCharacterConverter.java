
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PersonajeRepository;
import domain.ComicCharacter;

@Component
@Transactional
public class StringToComicCharacterConverter implements Converter<String, ComicCharacter> {

	@Autowired
	PersonajeRepository	personajeRepository;


	@Override
	public ComicCharacter convert(final String text) {
		ComicCharacter result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.personajeRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
