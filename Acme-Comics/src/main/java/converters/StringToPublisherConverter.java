
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PublisherRepository;
import domain.Publisher;

@Component
@Transactional
public class StringToPublisherConverter implements Converter<String, Publisher> {

	@Autowired
	PublisherRepository	publisherRepository;


	@Override
	public Publisher convert(final String text) {
		Publisher result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.publisherRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
