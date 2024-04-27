package ru.trilla.masking;

import org.springframework.stereotype.Component;
import ru.trilla.dto.SignInRequest;

@Component
public class SignInRequestMasker implements ObjectMasker {

    @Override
    public boolean isAcceptable(Object o) {
        return o instanceof SignInRequest;
    }

    @Override
    public Object mask(Object o) {
        final var signIn = (SignInRequest) o;
        return new SignInRequest(
                signIn.email(),
                "****"
        );
    }
}
