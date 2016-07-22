package wp.asynctaskdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityAsyncTask extends AppCompatActivity {
    Button mBt;
    TextView mTv;
    TextView mPro;
    MyAsyncTask myAsyncTask;
    ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_async_task);
        mBt = (Button) findViewById(R.id.bt);
        mTv = (TextView) findViewById(R.id.tv);
        mPb = (ProgressBar) findViewById(R.id.pb);
        mPro = (TextView) findViewById(R.id.tv_progress);
        myAsyncTask = new MyAsyncTask();
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动异步任务,需要调用该方法
                myAsyncTask.execute();
                mBt.setEnabled(false);

                //可以用myAsyncTask.cancel()取消异步任务
            }
        });
    }

    /**
     * 异步任务有三个参数:
     * 第一个为 Params:用于给doInBackground方法传递参数,通常传入需要解析的Url地址,这里演示不传参数
     * 第二个为 Progress:用于给onProgressUpdate传递参数,通常用Integer
     * 第三个为 Result:给doInBackground方法设定返回值类型,并将其返回的值传递给onPostExecute方法作为形参
     * 通常都需要调用下面四个方法来实现异步任务,doInBackground方法是抽象的,继承AsyncTask必须重写该方法
     */
    class MyAsyncTask extends AsyncTask<Void, Integer, String> {

        //在开始执行后台任务,也就是doInBackground方法前执行(也是运行在主线程)
        //用于初始化的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTv.setText("开始执行异步任务");
        }

        //唯一一个在子线程中执行的方法
        //用于执行耗时的操作
        @Override
        protected String doInBackground(Void... params) {
            for (int i = 10; i <= 100; i += 10) {
                try {
                    Thread.sleep(1000);

                    //当该方法被调用,会很快调用onProgressUpdate来更新进度条
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //为了显示出百分百的进度条
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "执行完毕";
        }

        //更新进度条,运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mPb.setVisibility(View.VISIBLE);
            mPb.setProgress(values[0]);
            mPro.setText("当前进度:" + values[0] + "%");

        }

        //运行在主线程,用于收尾工作
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTv.setText(s);
            mPro.setVisibility(View.INVISIBLE);
            mPb.setVisibility(View.INVISIBLE);
        }
    }
}
