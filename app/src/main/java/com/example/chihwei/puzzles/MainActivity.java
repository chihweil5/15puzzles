package com.example.chihwei.puzzles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static int BOARD_SIZE = 4;

    // init directions {right, left, up, down}
    private static final int[][] dir = new int[][] {{1,0}, {-1,0}, {0,1}, {0,-1}};

    private List<Button> tiles;
    private GridLayout board;
    private Button restartBtn;
    private TextView tvEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = findViewById(R.id.board);
        restartBtn = findViewById(R.id.restart);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBoard();
            }
        });
        tvEnd = findViewById(R.id.tvEnd);
        initBoard();
    }

    public void initBoard() {
        tiles = new ArrayList<>();
        board.removeAllViews();
        List<Integer> tileList = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            tileList.add(i);
        }

        Collections.shuffle(tileList);

        for(int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            final Button tileBtn = new Button(this);
            if(tileList.get(i) == 15) {
                tileBtn.setText("");
            } else {
                tileBtn.setText(Integer.toString(tileList.get(i)+1));
            }
            tileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button btn = (Button) view;
                    if(btn.getText().toString().equals("")) {
                        return;
                    }
                    for(int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
                        if(view == tiles.get(i)) {
                            Log.d(TAG, "click on: " + btn.getText());
                            int x = i / BOARD_SIZE;
                            int y = i % BOARD_SIZE;
                            Log.d(TAG, "pos: " + x + "," + y);
                            moveTile(x, y);
                            checkFinish();
                        }
                    }
                }
            });

            tiles.add(tileBtn);
            board.addView(tileBtn);
            setBoardEnable(true);
            setFinishText(false);
        }
    }

    private void moveTile(int x, int y) {
        int tileToMove = x * BOARD_SIZE + y;
        for(int i = 0; i < dir.length; i++) {
            int x1 = x + dir[i][0];
            int y1 = y + dir[i][1];
            if(x1 < 0 || x1 >= BOARD_SIZE || y1 < 0 || y1 >= BOARD_SIZE) {
                continue;
            }
            int index = x1 * BOARD_SIZE + y1;
            if(tiles.get(index).getText().toString().equals("")) {
                Log.d(TAG, "Empty: " + dir[i][0] + "," + dir[i][1]);
                // swap
                String num = tiles.get(tileToMove).getText().toString();
                tiles.get(index).setText(num);
                tiles.get(tileToMove).setText("");
                break;
            }
        }
    }

    private void checkFinish() {
        boolean finish = true;
        for(int i = 0; i < tiles.size()-1; i++) {
            String num = tiles.get(i).getText().toString();
            if(num.equals("") || Integer.parseInt(num) != i+1) {
                finish = false;
            }
        }

        if(finish) {
            Log.d(TAG, "You Win!");
            setFinishText(true);
            setBoardEnable(false);
        }
    }

    private void setFinishText(boolean enable) {
        if(enable) {
            tvEnd.setVisibility(View.VISIBLE);
        } else {
            tvEnd.setVisibility(View.INVISIBLE);
        }
    }

    private void setBoardEnable(boolean enable) {
        for(int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setEnabled(enable);
        }
    }
}
