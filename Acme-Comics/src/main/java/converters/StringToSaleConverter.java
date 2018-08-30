
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Comic;
import domain.Sale;
import repositories.ComicRepository;
import repositories.SaleRepository;

@Component
@Transactional
public class StringToSaleConverter implements Converter<String, Sale> {

	@Autowired
	SaleRepository saleRepository;


	@Override
	public Sale convert(final String text) {
		Sale result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.saleRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
