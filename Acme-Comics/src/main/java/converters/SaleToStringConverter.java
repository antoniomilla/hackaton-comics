
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.DirectMessage;
import domain.Sale;

@Component
@Transactional
public class SaleToStringConverter implements Converter<Sale, String> {

	@Override
	public String convert(final Sale directMessage) {
		String result;

		if (directMessage == null)
			result = null;
		else
			result = String.valueOf(directMessage.getId());

		return result;
	}

}
