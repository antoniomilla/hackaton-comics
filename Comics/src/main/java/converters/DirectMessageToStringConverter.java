
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.DirectMessage;

@Component
@Transactional
public class DirectMessageToStringConverter implements Converter<DirectMessage, String> {

	@Override
	public String convert(final DirectMessage directMessage) {
		String result;

		if (directMessage == null)
			result = null;
		else
			result = String.valueOf(directMessage.getId());

		return result;
	}

}
