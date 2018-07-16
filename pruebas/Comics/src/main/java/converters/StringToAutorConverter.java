
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AutorRepository;
import domain.Autor;

@Component
@Transactional
public class StringToAutorConverter implements Converter<String, Autor> {

	@Autowired
	AutorRepository	autorRepository;


	@Override
	public Autor convert(final String text) {
		Autor result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.autorRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
