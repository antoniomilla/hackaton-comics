
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PublishingCompanyRepository;
import domain.Publisher;

@Component
@Transactional
public class StringToPublishingCompanyConverter implements Converter<String, Publisher> {

	@Autowired
	PublishingCompanyRepository	editorialRepository;


	@Override
	public Publisher convert(final String text) {
		Publisher result;
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
