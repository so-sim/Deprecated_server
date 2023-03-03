package com.sosim.server.jwt.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.RuleResult;


public class PasswordUtil {

    public static Pattern PASSWORD_VALUE_PATTERN = Pattern.compile("^[0-9a-z]{10,15}$");
    public static int MIN_LENGTH = 10;
    public static int MAX_LENGTH = 15;
    private static PasswordGenerator GENERATOR = new PasswordGenerator();
    private static PasswordValidator VALIDATOR = new PasswordValidator(
        new LengthRule(MIN_LENGTH, MAX_LENGTH),
        new CharacterRule(EnglishCharacterData.LowerCase, 1),
        new CharacterRule(EnglishCharacterData.Digit, 1)
    );

    private static List<CharacterRule> CHARACTER_RULE_LIST = Arrays.asList(
        new CharacterRule(EnglishCharacterData.LowerCase, 1),
        new CharacterRule(EnglishCharacterData.Digit, 1)
    );

    public static boolean isValid(String password) {
        Matcher matcher = PASSWORD_VALUE_PATTERN.matcher(password);
        if(matcher.find()) {
            RuleResult result = VALIDATOR.validate(new PasswordData(password));
            return result.isValid();
        }
        return false;
    }

    private static int getRandomLength() {return MAX_LENGTH;}

    public static String generateRandomPassword() {
        String password = GENERATOR.generatePassword(getRandomLength(), CHARACTER_RULE_LIST);
        while(!isValid(password)) {
            password = GENERATOR.generatePassword(getRandomLength(), CHARACTER_RULE_LIST);
        }
        return password;
    }

}
