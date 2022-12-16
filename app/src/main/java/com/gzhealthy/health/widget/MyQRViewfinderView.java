//package com.gzhealthy.health.widget;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.LinearGradient;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.Shader;
//import android.graphics.Typeface;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.google.zxing.ResultPoint;
//import com.gzhealthy.health.R;
//import com.gzhealthy.health.utils.DispUtil;
////import com.journeyapps.barcodescanner.CameraPreview;
////import com.journeyapps.barcodescanner.ViewfinderView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Justin_Liu
// * on 2021/8/17
// */
//public class MyQRViewfinderView extends ViewfinderView {
//
//
//    protected static final String TAG = ViewfinderView.class.getSimpleName();
//
//    protected static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
//    protected static final long ANIMATION_DELAY = 1L;
//    protected static final int CURRENT_POINT_OPACITY = 0xA0;
//    protected static final int MAX_RESULT_POINTS = 20;
//    protected static final int POINT_SIZE = 6;
//
//    protected final Paint paint;
//    protected Bitmap resultBitmap;
//    protected int maskColor;
//    protected final int resultColor;
//    protected final int laserColor;
//    protected final int resultPointColor;
//    protected int scannerAlpha;
//    protected List<ResultPoint> possibleResultPoints;
//    protected List<ResultPoint> lastPossibleResultPoints;
//    protected CameraPreview cameraPreview;
//
//    // Cache the framingRect and previewFramingRect, so that we can still draw it after the preview
//    // stopped.
//    protected Rect framingRect;
//    protected Rect previewFramingRect;
//
//    private float horizontalScorllFrameY = 0;
//    private LinearGradient linearGradient;
//    private Paint linePaint;
//    private Paint conrerPaint;
//    private Paint descPaint;
//    private Context context;
//    private Rect cornerRect1,cornerRect2;
//    private String topDesc;
//
//    // This constructor is used when the class is built from an XML resource.
//    public MyQRViewfinderView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        // Initialize these once for performance rather than calling them every time in onDraw().
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        Resources resources = getResources();
//
//        // Get setted attributes on view
//        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.zxing_finder);
//
//        this.maskColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_mask,
//                resources.getColor(R.color.zxing_viewfinder_mask));
//        this.resultColor = attributes.getColor(R.styleable.zxing_finder_zxing_result_view,
//                resources.getColor(R.color.zxing_result_view));
//        this.laserColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_laser,
//                resources.getColor(R.color.zxing_viewfinder_laser));
//        this.resultPointColor = attributes.getColor(R.styleable.zxing_finder_zxing_possible_result_points,
//                resources.getColor(R.color.zxing_possible_result_points));
//
//        attributes.recycle();
//
//        scannerAlpha = 0;
//        possibleResultPoints = new ArrayList<>(MAX_RESULT_POINTS);
//        lastPossibleResultPoints = new ArrayList<>(MAX_RESULT_POINTS);
//
//        linePaint=new Paint();
//        linePaint.setAntiAlias(true);
//        linePaint.setStyle(Paint.Style.FILL);
//        linePaint.setColor(context.getResources().getColor(R.color.ring_bg,null));
////        linePaint.setStrokeCap(Paint.Cap.ROUND);
//
//        conrerPaint = new Paint();
//        conrerPaint.setAntiAlias(true);
//        conrerPaint.setStyle(Paint.Style.FILL);
//        conrerPaint.setColor(context.getResources().getColor(R.color.colorPrimary,null));
//
//        cornerRect1 = new Rect();
//        cornerRect2 = new Rect();
//
//        descPaint = new Paint();
//        descPaint.setColor(context.getResources().getColor(R.color.white,null));
//        descPaint.setTextSize(DispUtil.sp2px(context,14));
//        descPaint.setAntiAlias(true);
//        descPaint.setTextAlign(Paint.Align.CENTER);
//        topDesc = "将扫码框对准手表屏幕上的二维码";
//    }
//
//    public void setCameraPreview(CameraPreview view) {
//        this.cameraPreview = view;
//        view.addStateListener(new CameraPreview.StateListener() {
//            @Override
//            public void previewSized() {
//                refreshSizes();
//                invalidate();
//            }
//
//            @Override
//            public void previewStarted() {
//
//            }
//
//            @Override
//            public void previewStopped() {
//
//            }
//
//            @Override
//            public void cameraError(Exception error) {
//
//            }
//
//            @Override
//            public void cameraClosed() {
//
//            }
//        });
//    }
//
//    protected void refreshSizes() {
//        if(cameraPreview == null) {
//            return;
//        }
//        Rect framingRect = cameraPreview.getFramingRect();
//        Rect previewFramingRect = cameraPreview.getPreviewFramingRect();
//        if(framingRect != null && previewFramingRect != null) {
//            this.framingRect = framingRect;
//            this.previewFramingRect = previewFramingRect;
//        }
//    }
//
//    @Override
//    public void onDraw(Canvas canvas) {
//        refreshSizes();
//        if (framingRect == null || previewFramingRect == null) {
//            return;
//        }
//
//        final Rect frame = framingRect;
//        final Rect previewFrame = previewFramingRect;
//
//        final int width = canvas.getWidth();
//        final int height = canvas.getHeight();
//
//        // Draw the exterior (i.e. outside the framing rect) darkened
//        paint.setColor(resultBitmap != null ? resultColor : maskColor);
//        canvas.drawRect(0, 0, width, frame.top, paint);
//        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
//        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
//        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
//
//        if (resultBitmap != null) {
//            // Draw the opaque result bitmap over the scanning rectangle
//            paint.setAlpha(CURRENT_POINT_OPACITY);
//            canvas.drawBitmap(resultBitmap, null, frame, paint);
//        } else {
//
//            // Draw a red "laser scanner" line through the middle to show decoding is active
//            paint.setColor(laserColor);
////            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
////            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//            final int laserTop = frame.top;
//            if (horizontalScorllFrameY <= 0){
//                horizontalScorllFrameY = laserTop;
//            }else {
//                if (horizontalScorllFrameY < frame.height() + frame.top){
//                    horizontalScorllFrameY+= 2;
//                }else {
//                    horizontalScorllFrameY = laserTop;
//                }
//            }
//
//            final int horizontalLineRectOffset = 1;//横线上下偏移量
////            final Paint linePaint = paint;
//            final float[] positions = {0f,0.5f,1.0f};
//            final int[] colors = {
//                    context.getColor(R.color.start_qr_line),
//                    context.getColor(R.color.colorPrimary),
//                    context.getColor(R.color.end_qr_line)
//            };
//            linePaint.setColor(Color.parseColor("#fb8b2f"));
//            linearGradient = new LinearGradient(
//                    frame.left,
//                    horizontalScorllFrameY,
//                    frame.right,
//                    horizontalScorllFrameY,
//                    colors,
//                    positions,
//                    Shader.TileMode.CLAMP);
//            linePaint.setShader(linearGradient);
//            canvas.drawRect(frame.left + 1, horizontalScorllFrameY - horizontalLineRectOffset, frame.right - 1, horizontalScorllFrameY + horizontalLineRectOffset, linePaint);
//
//            //绘制四边角
//            int cornerWidth = DispUtil.dp2px(context,5);//宽
//            int cornerLong = cornerWidth *4;//长
//            //左上角
//            cornerRect1.set(frame.left - cornerWidth ,frame.top - cornerWidth ,
//                    frame.left + cornerLong - cornerWidth,frame.top);
//            cornerRect2.set(frame.left - cornerWidth ,frame.top - cornerWidth ,
//                    frame.left , frame.top - cornerWidth + cornerLong);
//            canvas.drawRect(cornerRect1,conrerPaint);
//            canvas.drawRect(cornerRect2,conrerPaint);
//            //左下角
//            cornerRect1.set(frame.left - cornerWidth ,frame.bottom + cornerWidth ,
//                    frame.left + cornerLong - cornerWidth,frame.bottom);
//            cornerRect2.set(frame.left - cornerWidth ,frame.bottom + cornerWidth ,
//                    frame.left , frame.bottom + cornerWidth - cornerLong);
//            canvas.drawRect(cornerRect1,conrerPaint);
//            canvas.drawRect(cornerRect2,conrerPaint);
//            //右上角
//            cornerRect1.set(frame.right + cornerWidth - cornerLong ,frame.top - cornerWidth ,
//                    frame.right + cornerWidth,frame.top);
//            cornerRect2.set(frame.right ,frame.top - cornerWidth ,
//                    frame.right + cornerWidth , frame.top - cornerWidth + cornerLong);
//            canvas.drawRect(cornerRect1,conrerPaint);
//            canvas.drawRect(cornerRect2,conrerPaint);
//            //右下角
//            cornerRect1.set(frame.right + cornerWidth - cornerLong ,frame.bottom + cornerWidth ,
//                    frame.right + cornerWidth,frame.bottom);
//            cornerRect2.set(frame.right ,frame.bottom + cornerWidth - cornerLong,
//                    frame.right + cornerWidth , frame.bottom + cornerWidth);
//            canvas.drawRect(cornerRect1,conrerPaint);
//            canvas.drawRect(cornerRect2,conrerPaint);
//
//            //边框
//            int paddingTop = DispUtil.dp2px(context,15);
//            final Paint framePaint = linePaint;
//            framePaint.reset();
//            framePaint.setColor(context.getColor(R.color.white));
//            framePaint.setStrokeWidth(1.5f);
//            framePaint.setStyle(Paint.Style.STROKE);
//            canvas.drawRect(frame.left+1,frame.top+1,frame.right-1,frame.bottom-1,framePaint);
//
//            //字体
//            Paint.FontMetrics fontMetrics = descPaint.getFontMetrics();
//            float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
//            float baseline=frame.top - distance - paddingTop;
//            canvas.drawText(topDesc,frame.centerX(),baseline,descPaint);
//
//            final float scaleX = frame.width() / (float) previewFrame.width();
//            final float scaleY = frame.height() / (float) previewFrame.height();
//
//            final int frameLeft = frame.left;
//            final int frameTop = frame.top;
//
//            // draw the last possible result points
//            if (!lastPossibleResultPoints.isEmpty()) {
//                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
//                paint.setColor(resultPointColor);
//                float radius = POINT_SIZE / 2.0f;
//                for (final ResultPoint point : lastPossibleResultPoints) {
//                    canvas.drawCircle(
//                            frameLeft + (int) (point.getX() * scaleX),
//                            frameTop + (int) (point.getY() * scaleY),
//                            radius, paint
//                    );
//                }
//                lastPossibleResultPoints.clear();
//            }
//
//            // draw current possible result points
//            if (!possibleResultPoints.isEmpty()) {
//                paint.setAlpha(CURRENT_POINT_OPACITY);
//                paint.setColor(resultPointColor);
//                for (final ResultPoint point : possibleResultPoints) {
//                    canvas.drawCircle(
//                            frameLeft + (int) (point.getX() * scaleX),
//                            frameTop + (int) (point.getY() * scaleY),
//                            POINT_SIZE, paint
//                    );
//                }
//
//                // swap and clear buffers
//                final List<ResultPoint> temp = possibleResultPoints;
//                possibleResultPoints = lastPossibleResultPoints;
//                lastPossibleResultPoints = temp;
//                possibleResultPoints.clear();
//            }
//
//            // Request another update at the animation interval, but only repaint the laser line,
//            // not the entire viewfinder mask.
//            postInvalidateDelayed(ANIMATION_DELAY,
//                    frame.left - POINT_SIZE,
//                    frame.top - POINT_SIZE,
//                    frame.right + POINT_SIZE,
//                    frame.bottom + POINT_SIZE);
//        }
//    }
//
//    public void drawViewfinder() {
//        Bitmap resultBitmap = this.resultBitmap;
//        this.resultBitmap = null;
//        if (resultBitmap != null) {
//            resultBitmap.recycle();
//        }
//        invalidate();
//    }
//
//    /**
//     * Draw a bitmap with the result points highlighted instead of the live scanning display.
//     *
//     * @param result An image of the result.
//     */
//    public void drawResultBitmap(Bitmap result) {
//        resultBitmap = result;
//        invalidate();
//    }
//
//    /**
//     * Only call from the UI thread.
//     *
//     * @param point a point to draw, relative to the preview frame
//     */
//    public void addPossibleResultPoint(ResultPoint point) {
//        if (possibleResultPoints.size() < MAX_RESULT_POINTS)
//            possibleResultPoints.add(point);
//    }
//
//    public void setMaskColor(int maskColor) {
//        this.maskColor = maskColor;
//    }
//}
