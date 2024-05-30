package myspringtest.demo.controller;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

public class TestEncrypt {

    public static void main(String[] args) {
        String password = "diubi";
        
        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encoded = passwordEncoder.encode(password);
        //String encoded2 = passwordEncoder.encode(password);
        
        System.out.println("Original password: " + password);
        //System.out.println("Encoded password: " + encoded);
        //System.out.println("Encoded password: " + encoded2);
    }
}