
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Company;

@Component
@Transactional
public class CompanyToStringConverter implements Converter<Company, String> {

	@Override
	public String convert(final Company a) {
		String result;

		if (a == null)
			result = null;
		else
			result = String.valueOf(a.getId());

		return result;
	}
}
