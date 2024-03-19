package Tools;

public class output {
    public static void warnMessage(String content) {
        int content_length = content.indexOf('\n');
        if (content_length == -1)
            content_length = content.length();

        StringBuilder border = new StringBuilder(content_length);
        for (int i = 0; i < content_length; i++) {
            border.append("-");
        }
        System.out.println(border+"\n"+content+"\n"+border);
    }
}
