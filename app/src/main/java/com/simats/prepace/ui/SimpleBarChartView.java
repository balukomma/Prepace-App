package com.simats.prepace.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class SimpleBarChartView extends View {

    private Paint barPaint;
    private Paint textPaint;
    private Paint gridPaint;
    
    private List<Integer> counts = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private int maxCount = 5; // Dynamic max

    public SimpleBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.parseColor("#7B61FF")); // Purple like design
        barPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(30f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setPathEffect(new android.graphics.DashPathEffect(new float[]{10, 10}, 0));
        gridPaint.setAlpha(100);
    }

    public void setData(List<Integer> counts, List<String> dayLabels) {
        this.counts = counts;
        this.labels = dayLabels;
        
        // Find max for scaling
        this.maxCount = 0;
        for(int c : counts) {
            if(c > maxCount) maxCount = c;
        }
        if(maxCount < 3) maxCount = 3; // Minimum top range
        
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (counts == null || counts.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float paddingBottom = 60f;
        float paddingLeft = 60f;
        float paddingRight = 30f;
        float paddingTop = 40f;

        float graphHeight = height - paddingBottom - paddingTop;
        float graphWidth = width - paddingLeft - paddingRight;

        // Draw Y Axis Labels & Grid
        textPaint.setTextAlign(Paint.Align.RIGHT);
        // Draw 4 levels
        for (int i = 0; i <= 3; i++) {
             // 0, 1/3, 2/3, 1 (of max)
             float val = (float)maxCount * i / 3.0f;
             float y = height - paddingBottom - (i * (graphHeight / 3.0f));
             
             String label = String.format("%.1f", val);
             if((val == Math.floor(val)) && !Double.isInfinite(val)) {
                 label = String.valueOf((int)val);
             }
             
             canvas.drawText(label, paddingLeft - 10, y + 10, textPaint);
             canvas.drawLine(paddingLeft, y, width - paddingRight, y, gridPaint);
        }

        // Draw Bars
        float barWidth = (graphWidth / counts.size()) * 0.6f; // 60% width, 40% spacing
        float interval = graphWidth / counts.size();

        for (int i = 0; i < counts.size(); i++) {
            float xCenter = paddingLeft + (i * interval) + (interval / 2);
            float count = counts.get(i);
            float barHeight = (count / (float)maxCount) * graphHeight;
            
            float left = xCenter - (barWidth / 2);
            float right = xCenter + (barWidth / 2);
            float top = (height - paddingBottom) - barHeight;
            float bottom = height - paddingBottom;

            // Draw rounded top bar
            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rect, 16f, 16f, barPaint);

            // Draw X Label
            if(i < labels.size()) {
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(labels.get(i), xCenter, height - 15, textPaint);
            }
        }
        
        // Axis Lines
        canvas.drawLine(paddingLeft, paddingTop, paddingLeft, height - paddingBottom, textPaint);
        canvas.drawLine(paddingLeft, height - paddingBottom, width - paddingRight, height - paddingBottom, textPaint);
    }
}
