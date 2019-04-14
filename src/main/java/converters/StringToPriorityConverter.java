
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.PriorityRepository;
import domain.Priority;

@Component
@Transactional
public class StringToPriorityConverter implements Converter<String, Priority> {

	@Autowired
	PriorityRepository	priorityRepository;


	@Override
	public Priority convert(final String text) {
		Priority result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.priorityRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
