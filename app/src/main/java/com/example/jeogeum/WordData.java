package com.example.jeogeum;

public class WordData {
    private String word;
    private Boolean used;
    private String date;

    public WordData(String word, Boolean used, String date) {
        this.word = word;
        this.used = used;
        this.date = date;
    }
    public WordData(){}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
