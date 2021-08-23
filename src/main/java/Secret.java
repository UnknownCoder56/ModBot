public class Secret {

    public static String getToken() {
        return System.getenv("TOKEN");
    }
}
