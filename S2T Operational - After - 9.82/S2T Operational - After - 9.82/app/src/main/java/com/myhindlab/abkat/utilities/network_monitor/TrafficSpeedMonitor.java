package com.myhindlab.abkat.utilities.network_monitor;

import android.net.TrafficStats;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficSpeedMonitor {
    public interface Listener {
        /**
         * Called every sample interval.
         *
         * totalTxBaselineUsed = baseline-subtracted total bytes sent in the session (or -1 if unsupported)
         * totalRxBaselineUsed = baseline-subtracted total bytes received in the session (or -1 if unsupported)
         *
         * uploadBps = bytes uploaded in the last interval (B/s)
         * downloadBps = bytes downloaded in the last interval (B/s)
         */
        void onSpeed(long sessionTxBytes, long sessionRxBytes, double uploadBps, double downloadBps);
    }

    private final Listener listener;
    private ScheduledExecutorService scheduler;
    private long lastTx = 0, lastRx = 0;
    private long baselineTx = 0, baselineRx = 0;
    private boolean usePerUid = false;
    private int sampleIntervalSeconds = 1;

    /**
     * @param listener callback
     * @param usePerUid when true tries to use TrafficStats.getUidTxBytes/getUidRxBytes for app-only counters
     */
    public TrafficSpeedMonitor(Listener listener, boolean usePerUid) {
        this.listener = listener;
        this.usePerUid = usePerUid;
    }

    public void start() {
        // choose counters: per-UID when requested & supported, else total
        if (usePerUid) {
            long uidTx = TrafficStats.getUidTxBytes(android.os.Process.myUid());
            long uidRx = TrafficStats.getUidRxBytes(android.os.Process.myUid());
            // if UID counters aren't supported, they return -1; fallback to total counters
            if (uidTx >= 0 && uidRx >= 0) {
                baselineTx = uidTx;
                baselineRx = uidRx;
                lastTx = uidTx;
                lastRx = uidRx;
            } else {
                usePerUid = false; // fallback
                baselineTx = safeTotalTx();
                baselineRx = safeTotalRx();
                lastTx = baselineTx;
                lastRx = baselineRx;
            }
        } else {
            baselineTx = safeTotalTx();
            baselineRx = safeTotalRx();
            lastTx = baselineTx;
            lastRx = baselineRx;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::sample, 0, sampleIntervalSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    private long safeTotalTx() {
        long v = TrafficStats.getTotalTxBytes();
        return v < 0 ? 0 : v;
    }

    private long safeTotalRx() {
        long v = TrafficStats.getTotalRxBytes();
        return v < 0 ? 0 : v;
    }

    private void sample() {
        long tx, rx;
        if (usePerUid) {
            tx = TrafficStats.getUidTxBytes(android.os.Process.myUid());
            rx = TrafficStats.getUidRxBytes(android.os.Process.myUid());
            if (tx < 0 || rx < 0) { // fallback to totals if UID counters became unavailable
                tx = safeTotalTx();
                rx = safeTotalRx();
            }
        } else {
            tx = safeTotalTx();
            rx = safeTotalRx();
        }

        // guard
        if (tx < lastTx) lastTx = tx;
        if (rx < lastRx) lastRx = rx;

        long deltaTx = Math.max(0L, tx - lastTx);
        long deltaRx = Math.max(0L, rx - lastRx);

        lastTx = tx;
        lastRx = rx;

        // session totals = current counters - baseline recorded at start
        long sessionTx = Math.max(0L, tx - baselineTx);
        long sessionRx = Math.max(0L, rx - baselineRx);

        // bytes per second (since sampling interval)
        double upBps = deltaTx / (double) sampleIntervalSeconds;
        double downBps = deltaRx / (double) sampleIntervalSeconds;

        try {
            listener.onSpeed(sessionTx, sessionRx, upBps, downBps);
        } catch (Exception e) {
            // ignore listener exceptions
        }
    }
}
