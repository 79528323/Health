
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class XAxisRendererRadarChart extends XAxisRenderer {

    private RadarChart mChart;

    public XAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);
        mChart = chart;
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        final MPPointF drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f);

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        float sliceangle = mChart.getSliceAngle();

        // calculate the factor that is needed for transforming the value to
        // pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0,0);
        for (int i = 0; i < mChart.getData().getMaxEntryCountSet().getEntryCount(); i++) {

            String label = mXAxis.getValueFormatter().getFormattedValue(i, mXAxis);

            float angle = (sliceangle * i + mChart.getRotationAngle()) % 360f;

            Utils.getPosition(center, mChart.getYRange() * factor
                    + mXAxis.mLabelRotatedWidth / 2f, angle, pOut);

            float outY = pOut.y - mXAxis.mLabelRotatedHeight / 2.f;
            outY += 30;
            if (mXAxis.isDrawRadarLabelValueEnabled() && i == 0){
                //TODO 加上数值后第一个Label距离图形边距太长导致数值超出显示范围，需要缩短
                drawLabel(c, label, pOut.x, outY,drawLabelAnchor, labelRotationAngleDegrees);
            }else
                drawLabel(c, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2.f,drawLabelAnchor, labelRotationAngleDegrees);

            //TODO 自定义雷图边角Label 加上数值
            if (mXAxis.isDrawRadarLabelValueEnabled()){
                float y = pOut.y - mXAxis.mLabelRotatedHeight / 2.f;
                if (i == 0)
                    y = outY;
                float value = mChart.getData().getDataSets().get(0).getEntryForIndex(i).getValue();
                drawLabel(c, String.valueOf(value), pOut.x, y-Utils.convertDpToPixel(14f),
                        drawLabelAnchor, labelRotationAngleDegrees);
            }
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(drawLabelAnchor);
    }

	/**
	 * XAxis LimitLines on RadarChart not yet supported.
	 *
	 * @param c
	 */
	@Override
	public void renderLimitLines(Canvas c) {
		// this space intentionally left blank
	}
}
