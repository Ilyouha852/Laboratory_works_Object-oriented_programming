public class Text {
    private String text;
    private String[] words;

    public Text(String text) {
            this.text = text;
            this.words = text.split("\\s+|(?=\\W)|(?<=\\W)");
    }
    public Text(){
        this("Today is a good day!");
    }

    // Метод для вывода исходного предложения
    public void outputText(boolean isOriginal) {
        System.out.println(isOriginal ? "\nИсходный текст:" : "\nОтсортированный текст:");
        System.out.println(text);
    }

    public boolean hasWords() {
        for (String word : words) {
            if (!word.isEmpty() && Character.isLetter(word.charAt(0))) {
                return true;
            }
        }
        return false;
    }

    public String sortWordsByLength() {
        int n = words.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (words[j].length() < words[j + 1].length()) {
                    String temp = words[j];
                    words[j] = words[j + 1];
                    words[j + 1] = temp;
                }
            }
        }

        String result = new String();
        for (String word : words) {
            if (!word.isEmpty() && Character.isLetter(word.charAt(0))) {
                result += word + " ";
            }
        }
        return result.trim();
    }
}