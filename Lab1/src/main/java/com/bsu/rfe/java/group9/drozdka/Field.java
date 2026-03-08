package com.bsu.rfe.java.group9.drozdka;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel implements MouseListener {
    private boolean paused;
    private boolean holdingBall;
    private long time;
    private int startX;
    private int startY;
    private BouncingBall holdedBall;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE);
        addMouseListener(this);
        repaintTimer.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    public synchronized void mousePressed(MouseEvent e) {
        for(BouncingBall ball : balls) {
            if(ball.isInBall(e.getX(), e.getY())) {
                pause();
                startX = e.getX();
                startY = e.getY();
                holdingBall = true;
                holdedBall = ball;
                time = System.currentTimeMillis();
                break;
            }
        }
    }

    public synchronized void mouseReleased(MouseEvent e) {
        if(holdingBall) {
            time = System.currentTimeMillis() - time;
            holdedBall.newDirection(startX, startY, e.getX(), e.getY(), time);
            holdingBall = false;
            resume();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }

    public void addBall() {
        balls.add(new BouncingBall(this));
    }
    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
            wait();
        }
    }
}