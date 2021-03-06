package praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class UserDataPattern {
    private String email;
    private String password;
    private String name;

    public UserDataPattern(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDataPattern(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static UserDataPattern getRandom(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru".toLowerCase();
        final String password = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        return new UserDataPattern(email, password, name);
    }

    public static String getRandomEmail(){

        return RandomStringUtils.randomAlphabetic(10)+"@yandex.ru".toLowerCase();
    }
    public static String getRandomData(){

        return RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }

    @Override
    public String toString() {
        return "UserDataPattern{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

