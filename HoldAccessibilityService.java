package com.example.s26basilitut;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class HoldAccessibilityService extends AccessibilityService {
    public static HoldAccessibilityService instance;
    private WindowManager wm;
    private View bubble;
    private boolean holding = false;
    private float startX, startY;

    @Override public void onServiceConnected() { instance = this; }
    @Override public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent e) {}
    @Override public void onInterrupt() {}

    public void showOverlay() {
        if (bubble != null) return;
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);

        TextView v = new TextView(this);
        v.setText("BASILI\nTUT");
        v.setTextColor(Color.WHITE);
        v.setTextSize(14);
        v.setGravity(17);
        v.setBackgroundColor(Color.rgb(30,120,220));

        v.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startX = event.getRawX();
                startY = event.getRawY();
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!holding) startHold(startX, startY);
                else stopHold();
                return true;
            }
            return true;
        });

        bubble = v;
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
            160, 110,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        p.x = 40; p.y = 500;
        wm.addView(v, p);
    }

    private void startHold(float x, float y) {
        holding = true;
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.StrokeDescription stroke =
            new GestureDescription.StrokeDescription(path, 0, 60000, true);
        dispatchGesture(new GestureDescription.Builder()
            .addStroke(stroke).build(), null, null);
    }

    private void stopHold() {
        holding = false;
        Path path = new Path();
        path.moveTo(startX, startY);
        GestureDescription.StrokeDescription stroke =
            new GestureDescription.StrokeDescription(path, 0, 1, false);
        dispatchGesture(new GestureDescription.Builder()
            .addStroke(stroke).build(), null, null);
    }

    @Override public void onDestroy() {
        if (bubble != null && wm != null) {
            try { wm.removeView(bubble); } catch (Exception ignored) {}
            bubble = null;
        }
        instance = null;
        super.onDestroy();
    }
}