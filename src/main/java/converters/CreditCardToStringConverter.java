
package converters;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import datatype.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard creditCard) {
		String result;
		StringBuilder builder;

		if (creditCard == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(creditCard.getHolderName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getBrandName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getNumber(), "UTF-8"));
				builder.append("|");

				if (creditCard.getCvvCode() == null)
					creditCard.setCvvCode(0);

				builder.append(URLEncoder.encode(Integer.toString(creditCard.getCvvCode()), "UTF-8"));
				builder.append("|");
				final DateFormat df1 = new SimpleDateFormat("MM/YY");

				if (creditCard.getExpiration() == null)
					creditCard.setExpiration(new Date());
				builder.append(URLEncoder.encode(df1.format(creditCard.getExpiration()), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
}
