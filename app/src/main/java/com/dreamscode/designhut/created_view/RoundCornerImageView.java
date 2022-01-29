package com.dreamscode.designhut.created_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class RoundCornerImageView extends androidx.appcompat.widget.AppCompatImageView {
    private float radius= 20f;
    private Path path;
    private RectF rectF;
    public RoundCornerImageView(@NonNull Context context) {
        super(context);
        init();
    }


    public RoundCornerImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RoundCornerImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectF = new RectF(0,0,this.getWidth(),this.getHeight());
        path.addRoundRect(rectF,radius,radius,Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
