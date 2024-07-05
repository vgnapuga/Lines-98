package ru.vsu.cs.lines98;

import static android.graphics.Color.TRANSPARENT;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    public GameEngine gameEngine;
    private GridAdapter adapter;
    private int selectedX = -1, selectedY = -1;
    private TextView scoreView;
    public String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setTranslucent(true);
        }

        getWindow().setStatusBarColor(TRANSPARENT);
        getWindow().setNavigationBarColor(TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        gameEngine = new GameEngine();

        GridView gridView = findViewById(R.id.gameField);
        adapter = new GridAdapter(this, gameEngine.getBoard());
        gridView.setAdapter(adapter);

        scoreView = findViewById(R.id.scoreText);

        updateScore();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int row = position / 9;
                int col = position % 9;

                if (selectedX == -1 && selectedY == -1) {
                    selectedX = row;
                    selectedY = col;
                } else {
                    if (!gameEngine.isValidMove(selectedX, selectedY, row, col)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid move", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (gameEngine.moveBall(selectedX, selectedY, row, col)) {
                            adapter.updateBoard(gameEngine.getBoard());

                            updateScore();
                        }
                    }

                    selectedX = -1;
                    selectedY = -1;
                }


                if (gameEngine.isEnd()) {
                    Intent intentEnd = new Intent(GameActivity.this, EndActivity.class);
                    intentEnd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentEnd.putExtra("score", score);
                    startActivity(intentEnd);
                    finish();
                }
            }
        });
    }

    private void updateScore() {
        score = String.valueOf(gameEngine.getScore());
        scoreView.setText(score);
    }

    public void pauseGame(View view) {
        Intent intent = new Intent(this, PauseActivity.class);

        startActivity(intent);
    }
}