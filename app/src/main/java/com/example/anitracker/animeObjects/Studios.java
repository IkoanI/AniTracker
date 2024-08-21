package com.example.anitracker.animeObjects;

import java.util.ArrayList;
import java.util.List;

public class Studios {
    private final List<String> animationStudios, producers;

    public Studios() {
        this.producers = new ArrayList<>();
        this.animationStudios = new ArrayList<>();
    }

    public List<String> getProducers() {
        return producers;
    }

    public List<String> getAnimationStudios() {
        return animationStudios;
    }

    public void addProducer(String producer) {this.producers.add(producer); }

    public void addAnimationStudio(String animationStudio) {this.animationStudios.add(animationStudio); }

}
