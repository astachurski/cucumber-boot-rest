package intj.ghchecker3.tools;

public class HttpReformatter {

    public static void main(String[] args) {

        String test = "http://advokat-kristiansund.no";
        System.out.println(getCoreHostName(test));
        test = "http://www.advokat-kristiansund.no";
        System.out.println(getCoreHostName(test));
        test = "www.advokat-kristiansund.no";
        System.out.println(getCoreHostName(test));
        test = "advokat-kristiansund.no";
        System.out.println(getCoreHostName(test));
    }


    public static String getCoreHostName(String address) {
        String result = "";
        String[] split = address.split("http://");
        if (split.length == 2) {
            result = getCoreHostNameInt(split[1]);
        } else if (split.length == 1) {
            result = getCoreHostNameInt(split[0]);
        }

        return result;
    }

    private static String getCoreHostNameInt(String address) {
        String result = "";
        String[] wwws = address.split("www.");
        if (wwws.length == 2) {
            result = wwws[1];
        } else if (wwws.length == 1) {
            return wwws[0];
        }
        return result;
    }
}
