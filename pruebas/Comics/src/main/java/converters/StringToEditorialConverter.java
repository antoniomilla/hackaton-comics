
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.EditorialRepository;
import domain.Editorial;

@Component
@Transactional
public class StringToEditorialConverter implements Converter<String, Editorial> {

	@Autowired
	EditorialRepository	editorialRepository;


	@Override
	public Editorial convert(final String text) {
		Editorial result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.editorialRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
