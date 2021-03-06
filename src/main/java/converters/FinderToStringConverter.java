package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Finder;

@Component
@Transactional
public class FinderToStringConverter implements Converter<Finder, String> {

    @Override
    public String convert(final Finder f) {
        String result;

        if (f == null)
            result = null;
        else
            result = String.valueOf(f.getId());

        return result;
    }

}
