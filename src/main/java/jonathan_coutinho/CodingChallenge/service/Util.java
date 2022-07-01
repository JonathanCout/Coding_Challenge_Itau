package jonathan_coutinho.CodingChallenge.service;

import jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static void validatePassword(String password) {
        String password_pattern ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{10,}$";
        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()){
            throw new BadRequestException(
                    "Senha inválida. " +
                            "A senha deve ter:  "+
                            "um mínimo de 10 caracteres, " +
                            "1 caractere caixa baixa, " +
                            "1 caractere caixa alta, " +
                            "1 número e 1 caractere especial");
        }
    }
}
