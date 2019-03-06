package test;

public class Hello {
    public static void main(final String[] arg) {
        System.out.println("Hello world!");
        int i = 0;
        final int j = 1;
        if (i < j) {
            i++;
        }
    }

    public static String getFirstString() {
        return "Hello Ayse";
    }

    public String getSecondString() {
        return "Hello Ahmet";
    }
}
