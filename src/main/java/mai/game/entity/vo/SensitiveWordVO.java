package mai.game.entity.vo;

import java.util.List;

public class SensitiveWordVO {
    private List<String> wordList;

    public List<String> getWordList() {
        return wordList;
    }

    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

    @Override
    public String toString() {
        return "SensitiveWordVO{" +
                "wordList=" + wordList +
                '}';
    }
}
