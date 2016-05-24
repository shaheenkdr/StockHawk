package com.sam_chordas.android.stockhawk.ui;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.Collections;



/**
 * Created by oblivion on 5/23/2016.
 * Activity class to graph stock value over time
 */

public class StockGraphActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final int CURSOR_LOADER_ID = 0;
    private Cursor mCursor;
    private LineChartView lineChartView;
    private LineSet mLineSet;
    private int maxRange,minRange;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        mLineSet = new LineSet();
        lineChartView = (LineChartView) findViewById(R.id.linechart);
        initLineChart();
        Intent intent = getIntent();
        Bundle args = new Bundle();
        args.putString(getResources().getString(R.string.string_symbol), intent.getStringExtra(getResources().getString(R.string.string_symbol)));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, args, this);
    }

    /**
     * method to instantiate and return a loader for the given ID
     */
    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + " = ?",
                new String[]{args.getString(getResources().getString(R.string.string_symbol))},
                null);
    }

    /**
     *  method to start graphing once the loader has finished loading
     */
    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        findRange(mCursor);
        fillLineSet();
    }


    /**
     * method to handle if the loader is reset
     */
    @Override public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     *  method to begin sketching the graph in activity
     */
    private void fillLineSet(){
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i++){
            float price = Float.parseFloat(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            mLineSet.addPoint("test " + i, price);
            mCursor.moveToNext();
        }
        mLineSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.line_set))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.line_stroke))
                .setDotsColor(ContextCompat.getColor(getApplicationContext(), R.color.line_dots));
        lineChartView.addData(mLineSet);
        lineChartView.show();
    }

    /**
     *  private method that sets the attributes of the graph
     */
    private void initLineChart() {
        Paint gridPaint = new Paint();
        gridPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.line_paint));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));
        lineChartView.setBorderSpacing(1)
                .setAxisBorderValues(minRange-100, maxRange+100, 50)
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(ContextCompat.getColor(getApplicationContext(), R.color.line_labels))
                .setXAxis(false)
                .setYAxis(false)
                .setBorderSpacing(Tools.fromDpToPx(5))
                .setGrid(ChartView.GridType.HORIZONTAL, gridPaint);
    }


    /**
     * method to find the range of stock values in different days
     */
    private void findRange(Cursor mCursor)
    {
        ArrayList<Float> mArrayList = new ArrayList<>();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            mArrayList.add(Float.parseFloat(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE))));
        }
        maxRange = Math.round(Collections.max(mArrayList));
        minRange = Math.round(Collections.min(mArrayList));
        if(minRange>100)
            minRange = minRange-100;
    }
}



