package in.sel.indianbabyname;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class ActivityAbout extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initialize();
    }

    public void initialize() {
        mToolbar = (Toolbar) findViewById(R.id.tb_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)findViewById(R.id.tv_about);
        TextView textView2 = (TextView)findViewById(R.id.tv_about_2);
        TextView tvThanx = (TextView)findViewById(R.id.tv_thax);
        textView.setText(Html.fromHtml(getResources().getString(R.string.about_info)));
        textView2.setText(Html.fromHtml(getResources().getString(R.string.about_below)));
        tvThanx.setText(Html.fromHtml(getResources().getString(R.string.thanx)));

    }
}
