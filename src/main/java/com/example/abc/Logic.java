package com.example.abc;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Logic {
    public static final int N = 10;
    int[][] field = new int[N][N];
    boolean player1Finished = false;
    boolean player2Finished = false;
    boolean gameOver = false;
    int round = 0;
    int lastPlayer = -1;
    int[] canFit (int H, int W, int player){
        //функция возвращает массив данных
        if ((H>N) || (W>N)) return null;
        if (player == 1){
            lastPlayer = 1;
            if (round == 0) {
                for (int dy = 0; dy < H; dy++){
                    for(int dx = 0; dx < W; dx++){
                        field[dy][dx]=player;
                    }
                }
                return new int[]{0,0};
            }
            for (int i = 0; i <= N-H;i++){
                for (int j = 0; j<=N-W;j++){
                    int neib = 0;
                    if ((i >0) && (field[i-1][j] == 1)) neib++;
                    if ((j>0) && (field[i][j-1] == 1)) neib++;
                    if (neib == 0) continue;
                    boolean ok = true;
                    for (int dy = 0; dy < H; dy++){
                        for(int dx = 0; dx < W; dx++){
                            if (field[i+dy][j+dx] != 0) {
                                ok = false;
                                break;
                            }
                        }
                    }
                    if (ok) {
                        for (int dy = 0; dy < H; dy++){
                            for(int dx = 0; dx < W; dx++){
                                field[i+dy][j+dx]=player;
                            }
                        }
                        return new int[]{i,j};
                    }
                }
            }
            int Z=W;
            W=H;
            H=Z;
            for (int i = 0; i<=N-H;i++){
                for (int j = 0; j<=N-W;j++){
                    int neib = 0;
                    if ((i >0) && (field[i-1][j] == 1)) neib++;
                    if ((j>0) && (field[i][j-1] == 1)) neib++;
                    if (neib == 0) continue;
                    boolean ok = true;
                    for (int dy = 0; dy < H; dy++){
                        for(int dx = 0; dx < W; dx++){
                            if (field[i+dy][j+dx] != 0) {
                                ok = false;
                                break;
                            }
                        }
                    }
                    if (ok) {
                        for (int dy = 0; dy < H; dy++){
                            for(int dx = 0; dx < W; dx++){
                                field[i+dy][j+dx]=player;
                            }
                        }
                        return new int[]{i,j};
                    }
                }
            }
            player1Finished = true;
            return null;
        }
        else {
            lastPlayer = 2;
            if (round == 0) {
                round ++;
                for (int dy = 0; dy < H; dy++){
                    for(int dx = 0; dx < W; dx++){
                        field[N-H+dy][N-W+dx]=player;
                    }
                }
                return new int[]{N-H,N-W};
            }
            for (int i = N-1; i>=H-1;i--){
                for (int j = N-1; j>=W-1;j--){
                    int neib = 0;
                    if ((i <N-1) && (field[i+1][j] == player)) neib++;
                    if ((j<N-1) && (field[i][j+1] == player)) neib++;
                    if (neib == 0) continue;
                    boolean ok = true;
                    for (int dy = 0; dy < H; dy++){
                        for(int dx = 0; dx < W; dx++){
                            if (field[i-dy][j-dx] != 0) {
                                ok = false;
                                break;
                            }
                        }
                    }
                    if (ok) {
                        for (int dy = 0; dy < H; dy++){
                            for(int dx = 0; dx < W; dx++){
                                field[i-dy][j-dx]=player;
                            }
                        }
                        return new int[]{i,j};
                    }
                }
            }
            int Z=W;
            W=H;
            H=Z;
            for (int i = N-1; i>=H;i--){
                for (int j = N-1; j>=W;j--){
                    int neib = 0;
                    if ((i <N-1) && (field[i+1][j] == player)) neib++;
                    if ((j<N-1) && (field[i][j+1] == player)) neib++;
                    if (neib == 0) continue;
                    boolean ok = true;
                    for (int dy = 0; dy < H; dy++){
                        for(int dx = 0; dx < W; dx++){
                            if (field[i-dy][j-dx] != 0) {
                                ok = false;
                                break;
                            }
                        }
                    }
                    if (ok) {
                        for (int dy = 0; dy < H; dy++){
                            for(int dx = 0; dx < W; dx++){
                                field[i-dy][j-dx]=player;
                            }
                        }
                        return new int[]{i,j};
                    }
                }
            }
            player2Finished = true;
            return null;
        }
    }
    public int getWhoCell(int row, int col){
        if (row < 0 || row >=10 || col < 0 || col >=10){
            return -1;
        }
        return field[row][col];
    }
}

