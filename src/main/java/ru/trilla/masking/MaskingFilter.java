package ru.trilla.masking;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class MaskingFilter extends Filter<ILoggingEvent> {
    @Getter
    private static MaskingFilter instance;
    @Setter
    private List<ObjectMasker> maskers;

    public MaskingFilter() {
        initInstance(this);
    }
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        } else {
            maskEvent(event);
            return FilterReply.ACCEPT;
        }
    }

    private static void initInstance(MaskingFilter maskingFilter) {
        instance = maskingFilter;
    }

    private void maskEvent(ILoggingEvent event) {
        final var args = event.getArgumentArray();
        if (Objects.isNull(args)) {
            return;
        }
        for (var i = 0; i < args.length; i++) {
            args[i] = mask(args[i]);
        }
    }

    private Object mask(Object o) {
        if (CollectionUtils.isEmpty(maskers)) {
            return o;
        }
        for (final var masker : maskers) {
            if (o instanceof Object[] args) {
                final var maskedArgs = new Object[args.length];
                for (var i = 0; i < args.length; i++) {
                    maskedArgs[i] = mask(args[i]);
                }
                return maskedArgs;
            }
            try {
                if (masker.isAcceptable(o)) {
                    return masker.mask(o);
                }
            } catch (RuntimeException e) {
                // Ignoring
            }
        }
        return o;
    }
}
