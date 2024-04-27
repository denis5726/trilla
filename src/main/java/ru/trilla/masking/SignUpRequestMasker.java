package ru.trilla.masking;

import org.springframework.stereotype.Component;
import ru.trilla.dto.SignUpRequest;

@Component
public class SignUpRequestMasker implements ObjectMasker {

    @Override
    public boolean isAcceptable(Object o) {
        return o instanceof SignUpRequest;
    }

    @Override
    public Object mask(Object o) {
        final var signUp = (SignUpRequest) o;
        return new SignUpRequest(
                signUp.email(),
                MaskingUtils.maskCommonText(signUp.firstName()),
                MaskingUtils.maskCommonText(signUp.lastName()),
                "****"
        );
    }
}
