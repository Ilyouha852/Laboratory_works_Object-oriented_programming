public class Text {
    private String text;
    private String[] words;

    public Text(String enteredText) {
        text = enteredText;
        words = text.split("\\s+|(?=\\W)|(?<=\\W)");
    }

    public String getText(){
        return text;
    }

    // Метод для проверки наличия слов в тексте
    public boolean hasWords() {
        for (String word : words) {
            if (!word.isEmpty() && Character.isLetter(word.charAt(0))) {
                return true;
            }
        }
        return false;
    }

    // Сортировка слов по длине
    public Text sortWordsByLength() {
        String[] sortedWords = words.clone();
        int n = sortedWords.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (sortedWords[j].length() < sortedWords[j + 1].length()) {
                    String temp = sortedWords[j];
                    sortedWords[j] = sortedWords[j + 1];
                    sortedWords[j + 1] = temp;
                }
            }
        }

        String sortedText = new String();
        for (String word : sortedWords) {
            if (!word.isEmpty() && Character.isLetter(word.charAt(0))) {
                sortedText += word + " ";
            }
        }
        return new Text(sortedText);
    }
}