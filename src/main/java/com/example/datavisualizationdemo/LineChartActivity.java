package com.example.datavisualizationdemo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

//draw the line chart on this activity when the "Get data" button is tapped
public class LineChartActivity extends AppCompatActivity {

    TextView progressTextView;
    TextView data;
    Map<Integer, Float> map = new LinkedHashMap<>();
    LineChart lineChart;
    static ArrayList<Entry>  entries;
    static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        entries = new ArrayList<>();
        i = 0;

        Resources res = getResources();
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        data = (TextView) findViewById(R.id.data);

        progressTextView.setText("");//implement the "Get data button"
        Button btn = (Button) findViewById(R.id.getDataButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
        });

        lineChart = (LineChart) findViewById(R.id.lineChart);


    }
    //connect to DB and get sql response then parse the data
    private class GetData extends AsyncTask<String, String, String>{

        String msg = "";
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String URL = DbStrings.URL;

        @Override
        protected void onPreExecute(){
            progressTextView.setText("Connecting to database ... ");
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection conn = null;
            Statement stmt = null;
            try{
                Class.forName(JDBC_DRIVER).newInstance();

                ///////////setup connection
                conn = DriverManager.getConnection(URL, DbStrings.USERNAME, DbStrings.PASSWORD);
                ///////////

                stmt = conn.createStatement();
                Intent in = getIntent();

                //get extra data passed from last activity, this data includes index number
                final int index = in.getIntExtra("com.example.datavisualizationdemo.PIC_INDEX", -1);
                String sql = "select * from Sensor.SensorData where SensorId = " + String.valueOf(index) + ";";
                ResultSet rs = stmt.executeQuery(sql);//send sql command
                i = 0;
                while(rs.next()){
                    float quantity = rs.getFloat("Quantity"); //parse the quantity
                    map.put(i, quantity); //this map is used to check empty
                    entries.add(new Entry(i, quantity));//entries used to draw line chart
                    i++;
                }
                //darawing the line chart
                LineDataSet data = new LineDataSet(entries, "Quantity");
                LineData lineData = new LineData(data);
                lineChart.setData(lineData);
                lineChart.invalidate();
                msg = "Process complete.";
                //terminate this connection
                rs.close();
                stmt.close();
                conn.close();

            }catch(SQLException connError){//error checks
                msg = connError.toString();
                connError.printStackTrace();
            } catch (ClassNotFoundException e) {
                msg = "JDBC NOT FOUND.";
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } finally {
                try{
                    if(stmt != null) {
                        stmt.close();
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
                try{
                    if(conn != null) {
                        conn.close();
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String msg){

            progressTextView.setText(this.msg);

            if(map.size() > 0){//check whether the data we wanted exists
                data.setText("Success");
            }else{
                data.setText("Failed");
            }

        }

    }

}//
