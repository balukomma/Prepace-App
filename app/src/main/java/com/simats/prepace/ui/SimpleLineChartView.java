package com.simats.prepace.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class SimpleLineChartView extends View {

    private Paint linePaint;
    private Paint dotPaint;
    private Paint gridPaint;
    private Paint textPaint;
    
    private List<Integer> scores = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    public SimpleLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#4A4AE2")); // Blue-ish purple like design
        linePaint.setStrokeWidth(8f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        dotPaint = new Paint();
        dotPaint.setColor(Color.parseColor("#4A4AE2"));
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setAntiAlias(true);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setPathEffect(new android.graphics.DashPathEffect(new float[]{10, 10}, 0));
        gridPaint.setAlpha(100);

        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(30f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Integer> dataPoints, List<String> dayLabels) {
        this.scores = dataPoints;
        this.labels = dayLabels;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (scores == null || scores.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float paddingBottom = 60f;
        float paddingLeft = 80f; // Space for Y axis labels
        float paddingRight = 40f;
        float paddingTop = 40f;

        float graphHeight = height - paddingBottom - paddingTop;
        float graphWidth = width - paddingLeft - paddingRight;

        // Draw Y Axis Labels (0, 25, 50, 75, 100)
        textPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i <= 4; i++) {
            float y = paddingTop + graphHeight - (i * (graphHeight / 4));
            canvas.drawText(String.valueOf(i * 25), paddingLeft - 10, y + 10, textPaint);
            
            // Draw Grid Line
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, gridPaint);
        }

        // Draw Line and Points
        Path path = new Path();
        float xInterval = graphWidth / (Math.max(scores.size(), 1) - 1);
        if (scores.size() == 1) xInterval = graphWidth;

        for (int i = 0; i < scores.size(); i++) {
            float x = paddingLeft + (i * xInterval);
            // Invert Y: 100 is top (paddingTop), 0 is bottom (height - paddingBottom)
            float scorePercent = (float) scores.get(i) / 100f;
            float y = (height - paddingBottom) - (scorePercent * graphHeight);

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            
            // Draw Dots
            canvas.drawCircle(x, y, 12f, dotPaint);

            // Draw X Axis Labels
            if (i < labels.size()) {
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(labels.get(i), x, height - 15, textPaint);
            }
        }
        
        canvas.drawPath(path, linePaint);
        
        // Draw Axis Lines
        canvas.drawLine(paddingLeft, paddingTop, paddingLeft, height - paddingBottom, textPaint); // Y Axis
        canvas.drawLine(paddingLeft, height - paddingBottom, width - paddingRight, height - paddingBottom, textPaint); // X Axis
    }
}
