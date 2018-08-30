
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.DirectMessageRepository;
import domain.DirectMessage;

@Component
@Transactional
public class StringToDirectMessageConverter implements Converter<String, DirectMessage> {

	@Autowired
	DirectMessageRepository	directMessageRepository;


	@Override
	public DirectMessage convert(final String text) {
		DirectMessage result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.directMessageRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
