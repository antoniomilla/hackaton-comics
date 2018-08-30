
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.UserComicRepository;
import domain.UserComic;

@Component
@Transactional
public class StringToUserComicConverter implements Converter<String, UserComic> {

	@Autowired
	UserComicRepository	userComicRepository;


	@Override
	public UserComic convert(final String text) {
		UserComic result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.userComicRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
