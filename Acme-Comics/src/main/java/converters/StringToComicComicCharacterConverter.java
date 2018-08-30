
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ComicComicCharacterRepository;
import domain.ComicComicCharacter;

@Component
@Transactional
public class StringToComicComicCharacterConverter implements Converter<String, ComicComicCharacter> {

	@Autowired
	ComicComicCharacterRepository	comicComicCharacterRepository;


	@Override
	public ComicComicCharacter convert(final String text) {
		ComicComicCharacter result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.comicComicCharacterRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
