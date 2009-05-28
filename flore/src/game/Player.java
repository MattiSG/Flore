package game;

import t2s.SIVOXDevint;

public class Player {
    private SIVOXDevint   sivox  = new SIVOXDevint();
    private static Player player = new Player();

    private Player() {}

    public static Player getPlayer() {
        return player;
    }

    public void playText(String text) {
        sivox.stop();
        sivox.playText(text);
    }

    public void playWav(String waveFile) {
        (new WaveThread(waveFile)).start();
    }

    class WaveThread extends Thread {
        private String waveFile;

        public WaveThread(String waveFile) {
            this.waveFile = waveFile;
        }

        public void run() {
            SIVOXDevint v = new SIVOXDevint();
            v.playWav(waveFile);
        }
    }
}
