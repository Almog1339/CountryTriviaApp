package com.example.countrytriviaapp.Interfaces;

import java.util.ArrayList;

public interface IAsyncCallback {
    <T> void processFinished(T response);
}
