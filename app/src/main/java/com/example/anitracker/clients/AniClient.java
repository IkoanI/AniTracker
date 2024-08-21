package com.example.anitracker.clients;

import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.BPossibleTypes;

// singleton class ensures only one connection to api
public enum AniClient {
    INSTANCE;
    final ApolloClient client;

    AniClient() {
        ApolloClient.Builder builder = new ApolloClient.Builder()
                .serverUrl("https://graphql.anilist.co");

        client = builder.build();
    }

    public ApolloClient getClient() {
        return client;
    }
}
