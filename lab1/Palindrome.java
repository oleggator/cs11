/**
 * Определение палиндрома 
 **/
public class Palindrome {

    /**
     * Точка входа
     **/
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please enter words.");
            System.exit(1);
        }


        for (String word: args)
            if (isPalindrome(word))
                System.out.printf("\"%s\" is palindrome\n", word);
            else
                System.out.printf("\"%s\" is not palindrome\n", word);
    }

    /**
     * Функция, разворачивающая строку
     **/
    public static String reverseString(String s) {
        String reversedString = new StringBuffer(s).reverse().toString();
        return reversedString;
    }

    /**
     * Функция, удаляющая все символы из строки, кроме букв
     **/
    public static String removePunctuation(String s) {
        StringBuffer formatedStringBuffer = new StringBuffer(s.length());

        for (int i = 0; i < s.length(); ++i)
            if (Character.isLetter(s.charAt(i)))
                formatedStringBuffer.append(s.charAt(i));

        return formatedStringBuffer.toString();
    }

    /**
     * Функция, определяющая палиндром
     **/
    public static boolean isPalindrome(String s) {
        s = removePunctuation(s).toLowerCase();
        
        return s.equals(reverseString(s));
    }
}