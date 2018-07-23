
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.UserComic;

@Component
@Transactional
public class UserComicToStringConverter implements Converter<UserComic, String> {

	@Override
	public String convert(final UserComic user) {
		String result;

		if (user == null)
			result = null;
		else
			result = String.valueOf(user.getId());

		return result;
	}

}
