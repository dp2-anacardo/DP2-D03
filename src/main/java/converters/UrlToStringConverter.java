
package converters;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import datatype.Url;

@Component
@Transactional
public class UrlToStringConverter implements Converter<Url, String> {

	@Override
	public String convert(final Url url) {
		String result;
		StringBuilder builder;

		if (url == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLDecoder.decode(url.getLink(), StandardCharsets.ISO_8859_1.name()));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
}
